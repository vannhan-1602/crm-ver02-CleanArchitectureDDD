import { useEffect, useMemo, useState } from "react";
import { authFetch } from "../apiClient";
import "./HopDong.css";
import "./KhachHang.css";
import "./HoatDong.css";
import "./ManagerForm.css";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

// ─── constants ──────────────────────────────────────────────────────────────
const LOAI_HOAT_DONG_OPTIONS = [
  { value: "Call", label: "Gọi điện" },
  { value: "Meeting", label: "Cuộc gặp" },
  { value: "Email", label: "Email" },
  { value: "Zalo", label: "Zalo" },
];

const LOAI_BADGE_MAP = {
  Call: "badge-call",
  Meeting: "badge-meeting",
  Email: "badge-email",
  Zalo: "badge-zalo",
};

const TARGET_OPTIONS = [
  { value: "khachhang", label: "Khách hàng" },
  { value: "lead", label: "Lead" },
];

const emptyForm = {
  targetType: "khachhang",
  khachHangId: "",
  leadId: "",
  loaiHoatDong: "Call",
  noiDung: "",
  thoiGianThucHien: "",
  nhanVienId: "",
};

function formatDateTime(value) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat("vi-VN", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(date);
}

// chuyển "2026-05-02T14:15:00" (datetime-local) <-> ISO cho input value
function toInputDateTime(value) {
  if (!value) return "";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "";
  const pad = (n) => String(n).padStart(2, "0");
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

// ─── component ──────────────────────────────────────────────────────────────
function HoatDongManager() {
  const [items, setItems] = useState([]);
  const [khachHangList, setKhachHangList] = useState([]);
  const [leadList, setLeadList] = useState([]);
  const [nhanVienList, setNhanVienList] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [search, setSearch] = useState("");
  const [filterLoai, setFilterLoai] = useState("");

  // map id → tên nhanh
  const nhanVienMap = useMemo(
    () =>
      new Map(
        nhanVienList.map((nv) => [
          String(nv.id),
          nv.hoTen ?? nv.tenNhanVien ?? `NV #${nv.id}`,
        ]),
      ),
    [nhanVienList],
  );

  const khachHangMap = useMemo(
    () =>
      new Map(
        khachHangList.map((kh) => [String(kh.id), kh.tenKhachHang ?? `KH #${kh.id}`]),
      ),
    [khachHangList],
  );

  const leadMap = useMemo(
    () => new Map(leadList.map((l) => [String(l.id), l.tenLead ?? `Lead #${l.id}`])),
    [leadList],
  );

  const tenNhanVien = (id) =>
    id != null ? (nhanVienMap.get(String(id)) ?? `NV #${id}`) : "—";

  const tenDoiTuong = (item) => {
    if (item.khachHangId != null) {
      return {
        label: khachHangMap.get(String(item.khachHangId)) ?? `KH #${item.khachHangId}`,
        type: "Khách hàng",
      };
    }
    if (item.leadId != null) {
      return {
        label: leadMap.get(String(item.leadId)) ?? `Lead #${item.leadId}`,
        type: "Lead",
      };
    }
    return { label: "—", type: "" };
  };

  // ── fetch ──
  const loadHoatDong = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await authFetch(`${API_BASE_URL}/api/hoat-dong`);
      if (!res.ok)
        throw new Error(`Không thể tải danh sách hoạt động (${res.status})`);
      const data = await res.json();
      setItems(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Tải danh sách thất bại");
    } finally {
      setLoading(false);
    }
  };

  const loadKhachHang = async () => {
    try {
      const res = await authFetch(`${API_BASE_URL}/api/khach-hang`);
      if (!res.ok) return;
      const data = await res.json();
      setKhachHangList(Array.isArray(data) ? data : []);
    } catch {
      // optional
    }
  };

  const loadLead = async () => {
    try {
      const res = await authFetch(`${API_BASE_URL}/api/leads`);
      if (!res.ok) return;
      const data = await res.json();
      setLeadList(Array.isArray(data) ? data : []);
    } catch {
      // optional
    }
  };

  const loadNhanVien = async () => {
    try {
      const res = await authFetch(`${API_BASE_URL}/api/nhan-vien`);
      if (!res.ok) return;
      const data = await res.json();
      setNhanVienList(Array.isArray(data) ? data : []);
    } catch {
      // optional
    }
  };

  useEffect(() => {
    loadHoatDong();
    loadKhachHang();
    loadLead();
    loadNhanVien();
  }, []);

  // ── filter ──
  const filteredItems = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    return items.filter((item) => {
      const { label } = tenDoiTuong(item);
      const matchSearch =
        !keyword ||
        [item.noiDung, label]
          .filter(Boolean)
          .some((v) => String(v).toLowerCase().includes(keyword));
      const matchLoai = !filterLoai || item.loaiHoatDong === filterLoai;
      return matchSearch && matchLoai;
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [items, search, filterLoai, khachHangMap, leadMap]);

  // ── stats ──
  const stats = useMemo(
    () => ({
      total: items.length,
      call: items.filter((i) => i.loaiHoatDong === "Call").length,
      meeting: items.filter((i) => i.loaiHoatDong === "Meeting").length,
      email: items.filter((i) => i.loaiHoatDong === "Email").length,
    }),
    [items],
  );

  // ── form handlers ──
  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
    setError("");
    setSuccess("");
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleTargetTypeChange = (e) => {
    const value = e.target.value;
    setForm((prev) => ({
      ...prev,
      targetType: value,
      khachHangId: "",
      leadId: "",
    }));
  };

  const validateForm = () => {
    if (form.targetType === "khachhang" && !form.khachHangId)
      return "Vui lòng chọn khách hàng";
    if (form.targetType === "lead" && !form.leadId)
      return "Vui lòng chọn lead";
    if (!form.loaiHoatDong) return "Vui lòng chọn loại hoạt động";
    if (!form.thoiGianThucHien) return "Vui lòng chọn thời gian thực hiện";
    return "";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const msg = validateForm();
    if (msg) {
      setError(msg);
      setSuccess("");
      return;
    }

    setSubmitting(true);
    setError("");
    setSuccess("");

    const toInt = (v) => (v === "" || v == null ? null : Number(v));
    const payload = {
      khachHangId: form.targetType === "khachhang" ? toInt(form.khachHangId) : null,
      leadId: form.targetType === "lead" ? toInt(form.leadId) : null,
      loaiHoatDong: form.loaiHoatDong,
      noiDung: form.noiDung.trim() || null,
      thoiGianThucHien: form.thoiGianThucHien || null,
      nhanVienId: toInt(form.nhanVienId),
    };

    try {
      const res = await authFetch(
        editingId
          ? `${API_BASE_URL}/api/hoat-dong/${editingId}`
          : `${API_BASE_URL}/api/hoat-dong`,
        {
          method: editingId ? "PUT" : "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        },
      );
      if (!res.ok)
        throw new Error(
          editingId
            ? `Cập nhật thất bại (${res.status})`
            : `Tạo mới thất bại (${res.status})`,
        );
      await loadHoatDong();
      resetForm();
      setSuccess(
        editingId
          ? "Cập nhật hoạt động thành công"
          : "Thêm hoạt động thành công",
      );
    } catch (err) {
      setError(err.message || "Không thể lưu hoạt động");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (item) => {
    setEditingId(item.id);
    setForm({
      targetType: item.khachHangId != null ? "khachhang" : "lead",
      khachHangId: item.khachHangId ?? "",
      leadId: item.leadId ?? "",
      loaiHoatDong: item.loaiHoatDong ?? "Call",
      noiDung: item.noiDung ?? "",
      thoiGianThucHien: toInputDateTime(item.thoiGianThucHien),
      nhanVienId: item.nhanVienId ?? "",
    });
    setError("");
    setSuccess("");
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc muốn xóa hoạt động này không?")) return;
    setError("");
    setSuccess("");
    try {
      const res = await authFetch(`${API_BASE_URL}/api/hoat-dong/${id}`, {
        method: "DELETE",
      });
      if (!res.ok && res.status !== 204)
        throw new Error(`Xóa thất bại (${res.status})`);
      await loadHoatDong();
      if (editingId === id) resetForm();
      setSuccess("Xóa hoạt động thành công");
    } catch (err) {
      setError(err.message || "Không thể xóa hoạt động");
    }
  };

  // ─── render ─────────────────────────────────────────────────────────────
  return (
    <main className="hopdong-page kh-page">
      {/* HEADER */}
      <section className="hopdong-header">
        <div>
          <p className="eyebrow">CRM / Chăm sóc khách hàng</p>
          <h1>Quản lý hoạt động chăm sóc</h1>
        </div>
        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="Tìm theo nội dung, khách hàng, lead..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <select
            className="search kh-select-sm"
            value={filterLoai}
            onChange={(e) => setFilterLoai(e.target.value)}
          >
            <option value="">Tất cả loại</option>
            {LOAI_HOAT_DONG_OPTIONS.map((o) => (
              <option key={o.value} value={o.value}>
                {o.label}
              </option>
            ))}
          </select>
          <button
            className="secondary-btn"
            type="button"
            onClick={loadHoatDong}
          >
            Tải lại
          </button>
        </div>
      </section>

      {/* STATS */}
      <section className="stats-row">
        <article className="stat-card">
          <span>Tổng hoạt động</span>
          <strong>{stats.total}</strong>
        </article>
        <article className="stat-card">
          <span>Gọi điện</span>
          <strong>{stats.call}</strong>
        </article>
        <article className="stat-card">
          <span>Cuộc gặp</span>
          <strong>{stats.meeting}</strong>
        </article>
        <article className="stat-card">
          <span>Email</span>
          <strong>{stats.email}</strong>
        </article>
      </section>

      {/* CONTENT */}
      <section className="content-grid">
        {/* FORM */}
        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className={`panel-head form-panel-head ${editingId ? "is-edit" : ""}`}>
            <div className="form-title-wrap">
              <div className="form-title-icon" aria-hidden="true">{editingId ? "✎" : "+"}</div>
              <div>
                <span className="form-mode-badge">{editingId ? "Đang chỉnh sửa" : "Tạo mới"}</span>
              <h2>
                {editingId ? "Cập nhật hoạt động" : "Thêm hoạt động mới"}
              </h2>
                <p>Ghi nhận lịch sử tương tác với khách hàng hoặc lead.</p>
              </div>
            </div>
            {editingId ? (
              <button className="ghost-btn form-cancel-btn" type="button" onClick={resetForm}>
                Hủy sửa
              </button>
            ) : null}
          </div>

          <div className="manager-form-body">
            <div className="form-section">
              <div className="section-title">Đối tượng</div>
              <div className="two-col">
                <label className="field">
                  Đối tượng <span className="kh-req">*</span>
                  <select
                    name="targetType"
                    value={form.targetType}
                    onChange={handleTargetTypeChange}
                    disabled={editingId != null}
                  >
                    {TARGET_OPTIONS.map((o) => (
                      <option key={o.value} value={o.value}>
                        {o.label}
                      </option>
                    ))}
                  </select>
                </label>

                {form.targetType === "khachhang" ? (
                  <label className="field">
                    Khách hàng <span className="kh-req">*</span>
                    <select
                      name="khachHangId"
                      value={form.khachHangId}
                      onChange={handleChange}
                      disabled={editingId != null}
                    >
                      <option value="">-- Chọn khách hàng --</option>
                      {khachHangList.map((kh) => (
                        <option key={kh.id} value={kh.id}>
                          {kh.tenKhachHang}
                        </option>
                      ))}
                    </select>
                  </label>
                ) : (
                  <label className="field">
                    Lead <span className="kh-req">*</span>
                    <select
                      name="leadId"
                      value={form.leadId}
                      onChange={handleChange}
                      disabled={editingId != null}
                    >
                      <option value="">-- Chọn lead --</option>
                      {leadList.map((l) => (
                        <option key={l.id} value={l.id}>
                          {l.tenLead}
                        </option>
                      ))}
                    </select>
                  </label>
                )}
              </div>
            </div>

            <div className="form-section">
              <div className="section-title">Nội dung hoạt động</div>
              <div className="two-col">
                <label className="field">
                  Loại hoạt động <span className="kh-req">*</span>
                  <select
                    name="loaiHoatDong"
                    value={form.loaiHoatDong}
                    onChange={handleChange}
                  >
                    {LOAI_HOAT_DONG_OPTIONS.map((o) => (
                      <option key={o.value} value={o.value}>
                        {o.label}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="field">
                  Thời gian thực hiện <span className="kh-req">*</span>
                  <input
                    name="thoiGianThucHien"
                    type="datetime-local"
                    value={form.thoiGianThucHien}
                    onChange={handleChange}
                  />
                </label>
              </div>

              <label className="field">
                Nội dung
                <textarea
                  name="noiDung"
                  rows={3}
                  value={form.noiDung}
                  onChange={handleChange}
                  placeholder="Ghi chú nội dung trao đổi..."
                />
              </label>
            </div>

            <div className="form-section">
              <div className="section-title">Thực hiện</div>
              <label className="field">
                Nhân viên thực hiện
                {nhanVienList.length > 0 ? (
                  <select
                    name="nhanVienId"
                    value={form.nhanVienId}
                    onChange={handleChange}
                  >
                    <option value="">-- Chọn nhân viên --</option>
                    {nhanVienList.map((nv) => (
                      <option key={nv.id} value={nv.id}>
                        {nv.hoTen ?? nv.tenNhanVien ?? `NV #${nv.id}`}
                      </option>
                    ))}
                  </select>
                ) : (
                  <input
                    name="nhanVienId"
                    type="number"
                    min="1"
                    value={form.nhanVienId}
                    onChange={handleChange}
                    placeholder="ID nhân viên"
                  />
                )}
              </label>
            </div>

            {error ? <div className="message error">{error}</div> : null}
            {success ? <div className="message success">{success}</div> : null}

            <div className="actions">
              <button className="secondary-btn" type="button" onClick={resetForm}>
                Làm mới
              </button>
              <button className="primary-btn" type="submit" disabled={submitting}>
                {submitting ? "Đang lưu..." : editingId ? "Cập nhật hoạt động" : "Thêm hoạt động"}
              </button>
            </div>
          </div>
        </form>

        {/* TABLE */}
        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sách hoạt động</h2>
              <p>
                Hiển thị {filteredItems.length}/{items.length} bản ghi.
              </p>
            </div>
            {loading ? <span className="loading">Đang tải...</span> : null}
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Loại</th>
                  <th>Đối tượng</th>
                  <th>Nội dung</th>
                  <th>Thời gian thực hiện</th>
                  <th>Người thực hiện</th>
                  <th>Cập nhật</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredItems.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="empty-row">
                      {loading
                        ? "Đang tải dữ liệu..."
                        : "Không có dữ liệu phù hợp"}
                    </td>
                  </tr>
                ) : (
                  filteredItems.map((item) => {
                    const doiTuong = tenDoiTuong(item);
                    return (
                      <tr key={item.id}>
                        <td>
                          <span
                            className={`badge ${LOAI_BADGE_MAP[item.loaiHoatDong] ?? "badge"}`}
                          >
                            {LOAI_HOAT_DONG_OPTIONS.find(
                              (o) => o.value === item.loaiHoatDong,
                            )?.label ?? item.loaiHoatDong}
                          </span>
                        </td>
                        <td>
                          <div className="stacked-cell">
                            <strong>{doiTuong.label}</strong>
                            {doiTuong.type && (
                              <span style={{ color: "#6d7c91", fontSize: 12 }}>
                                {doiTuong.type}
                              </span>
                            )}
                          </div>
                        </td>
                        <td>
                          <div className="stacked-cell">
                            {item.noiDung ? (
                              <span>{item.noiDung}</span>
                            ) : (
                              <span style={{ color: "#aaa" }}>—</span>
                            )}
                          </div>
                        </td>
                        <td>{formatDateTime(item.thoiGianThucHien)}</td>
                        <td>
                          <span className="kh-nv">
                            {item.tenNhanVien
                              ? item.tenNhanVien
                              : tenNhanVien(item.nhanVienId)}
                          </span>
                        </td>
                        <td>{formatDateTime(item.updatedAt)}</td>
                        <td>
                          <div className="row-actions">
                            <button
                              type="button"
                              className="ghost-btn"
                              onClick={() => handleEdit(item)}
                            >
                              Sửa
                            </button>
                            <button
                              type="button"
                              className="danger-btn"
                              onClick={() => handleDelete(item.id)}
                            >
                              Xóa
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </section>
      </section>
    </main>
  );
}

export default HoatDongManager;
