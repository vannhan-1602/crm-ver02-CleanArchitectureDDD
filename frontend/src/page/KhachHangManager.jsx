import { useEffect, useMemo, useState } from "react";
import { authFetch, canWriteModule } from "../apiClient";
import "./HopDong.css";
import "./KhachHang.css";
import "./ManagerForm.css";
import { ActionIcon } from "../moduleIcons.jsx";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

// ─── constants ──────────────────────────────────────────────────────────────
const LOAI_KHACH_HANG_OPTIONS = [
  { value: 1, label: "Cá nhân" },
  { value: 2, label: "Doanh nghiệp" },
  { value: 3, label: "VIP" },
];

const TINH_TRANG_OPTIONS = [
  { value: 1, label: "Đang hoạt động" },
  { value: 2, label: "Tiềm năng" },
  { value: 3, label: "Không hoạt động" },
];

const LOAI_BADGE_MAP = {
  1: "badge-canhan",
  2: "badge-doanhnghiep",
  3: "badge-vip",
};

const TINH_TRANG_BADGE_MAP = {
  1: "badge-hoatdong",
  2: "badge-tiemnang",
  3: "badge-khonghoatdong",
};

const emptyForm = {
  tenKhachHang: "",
  email: "",
  soDienThoai: "",
  loaiKhachHangId: "",
  tinhTrangId: "",
  maSoThue: "",
  nhanVienPhuTrachId: "",
};
function formatDiaChi(diaChiList) {
  if (!Array.isArray(diaChiList) || diaChiList.length === 0) return null;
  const dc = diaChiList.find((d) => d.isDefault) ?? diaChiList[0];
  return [dc.diaChiChiTiet, dc.phuongXa, dc.quanHuyen, dc.tinhThanh]
    .filter(Boolean)
    .join(", ");
}
function formatDateTime(value) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat("vi-VN", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(date);
}

// ─── component ──────────────────────────────────────────────────────────────
function KhachHangManager() {
  const canWriteKhachHang = canWriteModule("KHACH_HANG");
  const [items, setItems] = useState([]);
  const [nhanVienList, setNhanVienList] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [search, setSearch] = useState("");
  const [filterLoai, setFilterLoai] = useState("");
  const [filterTinhTrang, setFilterTinhTrang] = useState("");

  // map id → họ tên nhanh
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

  const tenNhanVien = (id) =>
    id != null ? (nhanVienMap.get(String(id)) ?? `NV #${id}`) : "—";

  // ── fetch ──
  const loadKhachHang = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await authFetch(`${API_BASE_URL}/api/khach-hang`);
      if (!res.ok)
        throw new Error(`Không thể tải danh sách khách hàng (${res.status})`);
      const data = await res.json();
      setItems(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Tải danh sách thất bại");
    } finally {
      setLoading(false);
    }
  };

  const loadNhanVien = async () => {
    try {
      const res = await authFetch(`${API_BASE_URL}/api/nhan-vien`);
      if (!res.ok) return;
      const data = await res.json();
      setNhanVienList(Array.isArray(data) ? data : []);
    } catch {
      // silent – NV list không bắt buộc để page hoạt động
    }
  };

  useEffect(() => {
    loadKhachHang();
    loadNhanVien();
  }, []);

  // ── filter ──
  const filteredItems = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    return items.filter((item) => {
      const matchSearch =
        !keyword ||
        [
          item.maKhachHang,
          item.tenKhachHang,
          item.email,
          item.soDienThoai,
          item.maSoThue,
        ]
          .filter(Boolean)
          .some((v) => String(v).toLowerCase().includes(keyword));
      const matchLoai =
        !filterLoai || String(item.loaiKhachHangId) === filterLoai;
      const matchTinhTrang =
        !filterTinhTrang || String(item.tinhTrangId) === filterTinhTrang;
      return matchSearch && matchLoai && matchTinhTrang;
    });
  }, [items, search, filterLoai, filterTinhTrang]);

  // ── stats ──
  const stats = useMemo(
    () => ({
      total: items.length,
      doanhnghiep: items.filter((i) => i.loaiKhachHangId === 2).length,
      canhan: items.filter((i) => i.loaiKhachHangId === 1).length,
      vip: items.filter((i) => i.loaiKhachHangId === 3).length,
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

  const validateForm = () => {
    if (!form.tenKhachHang.trim()) return "Tên khách hàng không được rỗng";
    if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      return "Email không đúng định dạng";
    if (form.soDienThoai && !/^\d{10,11}$/.test(form.soDienThoai))
      return "Số điện thoại phải có 10–11 chữ số";
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
      tenKhachHang: form.tenKhachHang.trim(),
      email: form.email.trim() || null,
      soDienThoai: form.soDienThoai.trim() || null,
      loaiKhachHangId: toInt(form.loaiKhachHangId),
      tinhTrangId: toInt(form.tinhTrangId),
      maSoThue: form.maSoThue.trim() || null,
      nhanVienPhuTrachId: toInt(form.nhanVienPhuTrachId),
    };

    try {
      const res = await authFetch(
        editingId
          ? `${API_BASE_URL}/api/khach-hang/${editingId}`
          : `${API_BASE_URL}/api/khach-hang`,
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
      await loadKhachHang();
      resetForm();
      setSuccess(
        editingId
          ? "Cập nhật khách hàng thành công"
          : "Thêm khách hàng thành công",
      );
    } catch (err) {
      setError(err.message || "Không thể lưu khách hàng");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (item) => {
    setEditingId(item.id);
    setForm({
      tenKhachHang: item.tenKhachHang ?? "",
      email: item.email ?? "",
      soDienThoai: item.soDienThoai ?? "",
      loaiKhachHangId: item.loaiKhachHangId ?? "",
      tinhTrangId: item.tinhTrangId ?? "",
      maSoThue: item.maSoThue ?? "",
      nhanVienPhuTrachId: item.nhanVienPhuTrachId ?? "",
    });
    setError("");
    setSuccess("");
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc muốn xóa khách hàng này không?")) return;
    setError("");
    setSuccess("");
    try {
      const res = await authFetch(`${API_BASE_URL}/api/khach-hang/${id}`, {
        method: "DELETE",
      });
      if (!res.ok && res.status !== 204)
        throw new Error(`Xóa thất bại (${res.status})`);
      await loadKhachHang();
      if (editingId === id) resetForm();
      setSuccess("Xóa khách hàng thành công");
    } catch (err) {
      setError(err.message || "Không thể xóa khách hàng");
    }
  };

  // ─── render ─────────────────────────────────────────────────────────────
  return (
    <main className="hopdong-page kh-page">
      {/* HEADER */}
      <section className="hopdong-header">
        <div>
          <p className="eyebrow">CRM / Khách hàng</p>
          <h1>Quản lý khách hàng</h1>
        </div>
        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="Tìm theo mã, tên, email, SĐT..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <select
            className="search kh-select-sm"
            value={filterLoai}
            onChange={(e) => setFilterLoai(e.target.value)}
          >
            <option value="">Tất cả loại</option>
            {LOAI_KHACH_HANG_OPTIONS.map((o) => (
              <option key={o.value} value={o.value}>
                {o.label}
              </option>
            ))}
          </select>
          <select
            className="search kh-select-sm"
            value={filterTinhTrang}
            onChange={(e) => setFilterTinhTrang(e.target.value)}
          >
            <option value="">Tất cả trạng thái</option>
            {TINH_TRANG_OPTIONS.map((o) => (
              <option key={o.value} value={o.value}>
                {o.label}
              </option>
            ))}
          </select>
          <button
            className="secondary-btn btn-icon"
            type="button"
            onClick={loadKhachHang}
          >
            <ActionIcon name="refresh" /> Tải lại
          </button>
        </div>
      </section>

      {/* STATS */}
      <section className="stats-row">
        <article className="stat-card">
          <span>Tổng khách hàng</span>
          <strong>{stats.total}</strong>
        </article>
        <article className="stat-card">
          <span>Cá nhân</span>
          <strong>{stats.canhan}</strong>
        </article>
        <article className="stat-card">
          <span>Doanh nghiệp</span>
          <strong>{stats.doanhnghiep}</strong>
        </article>
        <article className="stat-card">
          <span>VIP</span>
          <strong>{stats.vip}</strong>
        </article>
      </section>

      {/* CONTENT */}
      <section className="content-grid" style={!canWriteKhachHang ? { gridTemplateColumns: "1fr" } : undefined}>
        {/* FORM */}
        {canWriteKhachHang ? <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className={`panel-head form-panel-head ${editingId ? "is-edit" : ""}`}>
            <div className="form-title-wrap">
              <div className="form-title-icon" aria-hidden="true">{editingId ? "✎" : "+"}</div>
              <div>
                <span className="form-mode-badge">{editingId ? "Đang chỉnh sửa" : "Tạo mới"}</span>
              <h2>
                {editingId ? "Cập nhật khách hàng" : "Thêm khách hàng mới"}
              </h2>
                <p>Điền hồ sơ, phân loại và người phụ trách.</p>
              </div>
            </div>
            {editingId ? (
              <button className="ghost-btn form-cancel-btn btn-icon" type="button" onClick={resetForm}>
                <ActionIcon name="close" /> Hủy sửa
              </button>
            ) : null}
          </div>

          <div className="manager-form-body">
            <div className="form-section">
              <div className="section-title">Hồ sơ khách hàng</div>
              <label className="field">
                Tên khách hàng <span className="kh-req">*</span>
                <input
                  name="tenKhachHang"
                  value={form.tenKhachHang}
                  onChange={handleChange}
                  placeholder="Nguyễn Văn A"
                />
              </label>

              <div className="two-col">
                <label className="field">
                  Email
                  <input
                    name="email"
                    type="email"
                    value={form.email}
                    onChange={handleChange}
                    placeholder="example@mail.com"
                  />
                </label>
                <label className="field">
                  Số điện thoại
                  <input
                    name="soDienThoai"
                    value={form.soDienThoai}
                    onChange={handleChange}
                    placeholder="0901234567"
                  />
                </label>
              </div>

              <label className="field">
                Mã số thuế
                <input
                  name="maSoThue"
                  value={form.maSoThue}
                  onChange={handleChange}
                  placeholder="0123456789"
                />
              </label>
            </div>

            <div className="form-section">
              <div className="section-title">Phân loại</div>
              <div className="two-col">
                <label className="field">
                  Loại khách hàng
                  <select
                    name="loaiKhachHangId"
                    value={form.loaiKhachHangId}
                    onChange={handleChange}
                  >
                    <option value="">-- Chọn loại --</option>
                    {LOAI_KHACH_HANG_OPTIONS.map((o) => (
                      <option key={o.value} value={o.value}>
                        {o.label}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="field">
                  Tình trạng
                  <select
                    name="tinhTrangId"
                    value={form.tinhTrangId}
                    onChange={handleChange}
                  >
                    <option value="">-- Chọn tình trạng --</option>
                    {TINH_TRANG_OPTIONS.map((o) => (
                      <option key={o.value} value={o.value}>
                        {o.label}
                      </option>
                    ))}
                  </select>
                </label>
              </div>
            </div>

            <div className="form-section">
              <div className="section-title">Phân công</div>
              <label className="field">
                Nhân viên phụ trách
                {nhanVienList.length > 0 ? (
                  <select
                    name="nhanVienPhuTrachId"
                    value={form.nhanVienPhuTrachId}
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
                    name="nhanVienPhuTrachId"
                    type="number"
                    min="1"
                    value={form.nhanVienPhuTrachId}
                    onChange={handleChange}
                    placeholder="ID nhân viên"
                  />
                )}
              </label>
            </div>

            {error ? <div className="message error">{error}</div> : null}
            {success ? <div className="message success">{success}</div> : null}

            <div className="actions">
              <button className="secondary-btn btn-icon" type="button" onClick={resetForm}>
                <ActionIcon name="refresh" /> Làm mới
              </button>
              <button className="primary-btn btn-icon" type="submit" disabled={submitting}>
                      <ActionIcon name="save" />
                {submitting ? "Đang lưu..." : editingId ? "Cập nhật khách hàng" : "Thêm khách hàng"}
              </button>
            </div>
          </div>
        </form> : null}

        {/* TABLE */}
        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sách khách hàng</h2>
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
                  <th>Mã KH</th>
                  <th>Tên khách hàng</th>
                  <th>Liên hệ</th>
                  <th>Địa chỉ</th>
                  <th>Loại</th>
                  <th>Tình trạng</th>
                  <th>NV phụ trách</th>
                  <th>Cập nhật</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredItems.length === 0 ? (
                  <tr>
                    <td colSpan="9" className="empty-row">
                      {loading
                        ? "Đang tải dữ liệu..."
                        : "Không có dữ liệu phù hợp"}
                    </td>
                  </tr>
                ) : (
                  filteredItems.map((item) => (
                    <tr key={item.id}>
                      <td>
                        <span className="kh-ma">
                          {item.maKhachHang || `#${item.id}`}
                        </span>
                      </td>
                      <td>
                        <div className="stacked-cell">
                          <strong>{item.tenKhachHang}</strong>
                          {item.maSoThue && (
                            <span style={{ color: "#6d7c91", fontSize: 12 }}>
                              MST: {item.maSoThue}
                            </span>
                          )}
                        </div>
                      </td>
                      <td>
                        <div className="stacked-cell">
                          {item.email && <span>{item.email}</span>}
                          {item.soDienThoai && (
                            <span style={{ color: "#6d7c91", fontSize: 12 }}>
                              {item.soDienThoai}
                            </span>
                          )}
                          {!item.email && !item.soDienThoai && (
                            <span style={{ color: "#aaa" }}>—</span>
                          )}
                        </div>
                      </td>
                      <td>
                        <div className="stacked-cell">
                          {formatDiaChi(item.diaChiList) ? (
                            <span>{formatDiaChi(item.diaChiList)}</span>
                          ) : (
                            <span style={{ color: "#aaa" }}>—</span>
                          )}
                          {item.diaChiList?.length > 1 && (
                            <span style={{ color: "#6d7c91", fontSize: 12 }}>
                              +{item.diaChiList.length - 1} địa chỉ khác
                            </span>
                          )}
                        </div>
                      </td>
                      <td>
                        {item.loaiKhachHangId ? (
                          <span
                            className={`badge ${LOAI_BADGE_MAP[item.loaiKhachHangId] ?? "badge"}`}
                          >
                            {LOAI_KHACH_HANG_OPTIONS.find(
                              (o) => o.value === item.loaiKhachHangId,
                            )?.label ?? `Loại ${item.loaiKhachHangId}`}
                          </span>
                        ) : (
                          "—"
                        )}
                      </td>
                      <td>
                        {item.tinhTrangId ? (
                          <span
                            className={`badge ${TINH_TRANG_BADGE_MAP[item.tinhTrangId] ?? "badge"}`}
                          >
                            {TINH_TRANG_OPTIONS.find(
                              (o) => o.value === item.tinhTrangId,
                            )?.label ?? `Trạng thái ${item.tinhTrangId}`}
                          </span>
                        ) : (
                          "—"
                        )}
                      </td>
                      <td>
                        {/* Ưu tiên tenNhanVienPhuTrach từ API, fallback về map local */}
                        <span className="kh-nv">
                          {item.tenNhanVienPhuTrach
                            ? item.tenNhanVienPhuTrach
                            : tenNhanVien(item.nhanVienPhuTrachId)}
                        </span>
                      </td>
                      <td>{formatDateTime(item.updatedAt)}</td>
                      <td>
                        <div className="row-actions">
                          {canWriteKhachHang ? (
                            <>
                          <button
                            type="button"
                            className="ghost-btn btn-icon"
                            onClick={() => handleEdit(item)}
                          >
                            <ActionIcon name="edit" /> Sửa
                          </button>
                          <button
                            type="button"
                            className="danger-btn btn-icon"
                            onClick={() => handleDelete(item.id)}
                          >
                            <ActionIcon name="delete" /> Xóa
                          </button>
                            </>
                          ) : null}
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </section>
      </section>
    </main>
  );
}

export default KhachHangManager;
