import React, { useState, useEffect, useMemo } from "react";
import { authFetch } from "../apiClient";
import "./LeadManager.css";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

const emptyForm = {
  tenLead: "",
  tenCongTy: "",
  soDienThoai: "",
  email: "",
  nhanVienPhuTrachId: "",
};

const TINH_TRANG_OPTIONS = [
  { value: "Moi", label: "Mới" },
  { value: "DangChamSoc", label: "Đang chăm sóc" },
  { value: "DaChuyenDoi", label: "Đã chuyển đổi" },
  { value: "NgungChamSoc", label: "Ngừng chăm sóc" },
];

function formatDateTime(value) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat("vi-VN", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(date);
}

function LeadManager() {
  const [items, setItems] = useState([]);
  const [nhanVienList, setNhanVienList] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);

  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [search, setSearch] = useState("");

  const [statusModal, setStatusModal] = useState({
    visible: false,
    lead: null,
    selected: "",
  });
  const [detailModal, setDetailModal] = useState({
    visible: false,
    lead: null,
  });

  // Load Initial Data
  useEffect(() => {
    loadLeads();
    loadNhanVien();
  }, []);

  const loadLeads = async () => {
    setLoading(true);
    try {
      const response = await authFetch(`${API_BASE_URL}/api/leads`);
      if (!response.ok)
        throw new Error(`Không thể tải danh sách Lead (${response.status})`);
      const data = await response.json();
      setItems(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Tải danh sách thất bại");
    } finally {
      setLoading(false);
    }
  };

  const loadNhanVien = async () => {
    try {
      const response = await authFetch(`${API_BASE_URL}/api/nhan-vien`);
      if (!response.ok) throw new Error("HTTP " + response.status);
      const data = await response.json();
      setNhanVienList(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Lỗi khi tải nhân viên:", err);
    }
  };

  // Derived State (Search & Stats)
  const filteredItems = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    if (!keyword) return items;
    return items.filter((item) =>
      [
        item.tenLead,
        item.tenCongTy,
        item.email,
        item.soDienThoai,
        item.tinhTrang,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(keyword)),
    );
  }, [items, search]);

  const stats = useMemo(() => {
    return {
      total: items.length,
      moi: items.filter((i) => i.tinhTrang === "Moi").length,
      dangChamSoc: items.filter((i) => i.tinhTrang === "DangChamSoc").length,
      daChuyenDoi: items.filter((i) => i.tinhTrang === "DaChuyenDoi").length,
    };
  }, [items]);

  // Form Handlers
  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
    setError("");
    setSuccess("");
  };

  const validateForm = () => {
    if (!form.tenLead.trim()) return "Tên Lead không được rỗng";
    if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      return "Email không đúng định dạng";
    if (form.soDienThoai && !/^\d{10,11}$/.test(form.soDienThoai))
      return "Số điện thoại phải từ 10-11 số";
    return "";
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const validationMessage = validateForm();
    if (validationMessage) {
      setError(validationMessage);
      setSuccess("");
      return;
    }

    setSubmitting(true);
    setError("");
    setSuccess("");

    const payload = {
      ...form,
      nhanVienPhuTrachId: form.nhanVienPhuTrachId
        ? parseInt(form.nhanVienPhuTrachId)
        : null,
    };

    try {
      const response = await authFetch(
        editingId
          ? `${API_BASE_URL}/api/leads/${editingId}`
          : `${API_BASE_URL}/api/leads`,
        {
          method: editingId ? "PUT" : "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        },
      );

      if (!response.ok)
        throw new Error(editingId ? "Cập nhật thất bại" : "Tạo mới thất bại");

      await loadLeads();
      resetForm();
      setSuccess(
        editingId ? "Cập nhật Lead thành công!" : "Tạo mới Lead thành công!",
      );
    } catch (err) {
      setError(err.message || "Không thể lưu Lead");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (item) => {
    setEditingId(item.id);
    setForm({
      tenLead: item.tenLead || "",
      tenCongTy: item.tenCongTy || "",
      soDienThoai: item.soDienThoai || "",
      email: item.email || "",
      nhanVienPhuTrachId: item.nhanVienPhuTrachId || "",
    });
    setError("");
    setSuccess("");
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa Lead này không?")) return;
    setError("");
    setSuccess("");
    try {
      const response = await authFetch(`${API_BASE_URL}/api/leads/${id}`, {
        method: "DELETE",
      });
      if (!response.ok) throw new Error("Xóa thất bại");
      await loadLeads();
      if (editingId === id) resetForm();
      setSuccess("Xóa Lead thành công!");
    } catch (err) {
      setError(err.message || "Không thể xóa Lead");
    }
  };

  const handleConvert = async (id) => {
    if (
      !window.confirm(
        "Bạn có chắc chắn muốn chuyển đổi Lead này thành Khách Hàng?",
      )
    )
      return;
    setLoading(true);
    try {
      const response = await authFetch(`${API_BASE_URL}/api/leads/${id}/convert`, {
        method: "POST",
      });
      if (!response.ok) throw new Error("Chuyển đổi thất bại");
      const kh = await response.json();
      await loadLeads();
      setSuccess(`Chuyển đổi thành công! Mã KH: ${kh.maKhachHang}`);
    } catch (err) {
      setError(err.message || "Không thể chuyển đổi Lead");
    } finally {
      setLoading(false);
    }
  };

  const submitStatusChange = async () => {
    if (
      !statusModal.selected ||
      statusModal.selected === statusModal.lead.tinhTrang
    ) {
      setStatusModal({ visible: false, lead: null, selected: "" });
      return;
    }
    setLoading(true);
    try {
      const response = await authFetch(
        `${API_BASE_URL}/api/leads/${statusModal.lead.id}/status`,
        {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ tinhTrangMoi: statusModal.selected }),
        },
      );
      if (!response.ok) throw new Error("Cập nhật trạng thái thất bại");
      await loadLeads();
      setSuccess("Cập nhật trạng thái thành công!");
    } catch (err) {
      setError(err.message || "Không thể đổi trạng thái");
    } finally {
      setLoading(false);
      setStatusModal({ visible: false, lead: null, selected: "" });
    }
  };

  const getStatusLabel = (val) =>
    TINH_TRANG_OPTIONS.find((o) => o.value === val)?.label || val;

  return (
    <main className="lead-page">
      {/* Detail Modal */}
      {detailModal.visible && detailModal.lead && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3>Chi tiết Lead</h3>
              <button
                className="ghost-btn"
                onClick={() => setDetailModal({ visible: false, lead: null })}
              >
                Đóng
              </button>
            </div>
            <div className="detail-grid">
              <div className="detail-item">
                <span>ID</span>
                <strong>#{detailModal.lead.id}</strong>
              </div>
              <div className="detail-item">
                <span>Tên Lead</span>
                <strong>{detailModal.lead.tenLead}</strong>
              </div>
              <div className="detail-item">
                <span>Công ty</span>
                <strong>{detailModal.lead.tenCongTy || "—"}</strong>
              </div>
              <div className="detail-item">
                <span>Email</span>
                <strong>{detailModal.lead.email || "—"}</strong>
              </div>
              <div className="detail-item">
                <span>Số điện thoại</span>
                <strong>{detailModal.lead.soDienThoai || "—"}</strong>
              </div>
              <div className="detail-item">
                <span>Nhân viên PT</span>
                <strong>{detailModal.lead.tenNhanVienPhuTrach || "—"}</strong>
              </div>
              <div className="detail-item">
                <span>Trạng thái</span>
                <strong
                  className={`badge badge-${String(detailModal.lead.tinhTrang).toLowerCase()}`}
                >
                  {getStatusLabel(detailModal.lead.tinhTrang)}
                </strong>
              </div>
              <div className="detail-item">
                <span>Ngày tạo</span>
                <strong>{formatDateTime(detailModal.lead.createdAt)}</strong>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Status Modal */}
      {statusModal.visible && statusModal.lead && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3>Đổi trạng thái Lead</h3>
            </div>
            <div className="status-options">
              {TINH_TRANG_OPTIONS.map((opt) => (
                <label
                  key={opt.value}
                  style={{ opacity: opt.value === "DaChuyenDoi" ? 0.5 : 1 }}
                >
                  <input
                    type="radio"
                    name="status"
                    value={opt.value}
                    checked={statusModal.selected === opt.value}
                    disabled={opt.value === "DaChuyenDoi"}
                    onChange={(e) =>
                      setStatusModal({
                        ...statusModal,
                        selected: e.target.value,
                      })
                    }
                  />
                  {opt.label}
                </label>
              ))}
            </div>
            <div className="actions" style={{ justifyContent: "flex-end" }}>
              <button
                className="ghost-btn"
                onClick={() =>
                  setStatusModal({ visible: false, lead: null, selected: "" })
                }
              >
                Huỷ
              </button>
              <button className="primary-btn" onClick={submitStatusChange}>
                Lưu thay đổi
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Header */}
      <section className="lead-header">
        <div>
          <p className="eyebrow">CRM / Lead</p>
          <h1>Quản lý Lead</h1>
          <p className="subtitle">
            Danh sách tiềm năng kinh doanh và chuyển đổi khách hàng.
          </p>
        </div>
        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="Tìm theo tên, email, sđt..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <button className="secondary-btn" type="button" onClick={loadLeads}>
            Tải lại
          </button>
        </div>
      </section>

      {/* Stats */}
      <section className="stats-row">
        <article className="stat-card">
          <span>Tổng số</span>
          <strong>{stats.total}</strong>
        </article>
        <article className="stat-card">
          <span>Mới</span>
          <strong>{stats.moi}</strong>
        </article>
        <article className="stat-card">
          <span>Đang chăm sóc</span>
          <strong>{stats.dangChamSoc}</strong>
        </article>
        <article className="stat-card">
          <span>Đã chuyển đổi</span>
          <strong>{stats.daChuyenDoi}</strong>
        </article>
      </section>

      {/* Main Content */}
      <section className="content-grid">
        {/* Form Panel */}
        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-head">
            <div>
              <h2>{editingId ? "Cập nhật Lead" : "Thêm mới Lead"}</h2>
              <p>Dữ liệu sẽ gọi vào API `api/leads`.</p>
            </div>
            {editingId && (
              <button className="ghost-btn" type="button" onClick={resetForm}>
                Hủy sửa
              </button>
            )}
          </div>

          <label>
            Tên Lead <span style={{ color: "#d84d5b" }}>*</span>
            <input
              name="tenLead"
              value={form.tenLead}
              onChange={handleChange}
              placeholder="Nhập tên lead..."
            />
          </label>

          <label>
            Tên Công Ty
            <input
              name="tenCongTy"
              value={form.tenCongTy}
              onChange={handleChange}
              placeholder="Tên công ty (nếu có)"
            />
          </label>

          <div className="two-col">
            <label>
              Email
              <input
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="example@email.com"
              />
            </label>
            <label>
              Số điện thoại
              <input
                name="soDienThoai"
                value={form.soDienThoai}
                onChange={handleChange}
                placeholder="0xxxxxxxxx"
              />
            </label>
          </div>

          <label>
            Nhân viên phụ trách
            <select
              name="nhanVienPhuTrachId"
              value={form.nhanVienPhuTrachId}
              onChange={handleChange}
            >
              <option value="">— Chọn nhân viên —</option>
              {nhanVienList.map((nv) => (
                <option key={nv.id} value={nv.id}>
                  {nv.hoTen}
                </option>
              ))}
            </select>
          </label>

          {error && <div className="message error">{error}</div>}
          {success && <div className="message success">{success}</div>}

          <div className="actions">
            <button className="primary-btn" type="submit" disabled={submitting}>
              {submitting ? "Đang lưu..." : editingId ? "Cập nhật" : "Tạo mới"}
            </button>
            <button className="secondary-btn" type="button" onClick={resetForm}>
              Làm mới form
            </button>
          </div>
        </form>

        {/* Table Panel */}
        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sách Lead</h2>
              <p>
                Hiển thị {filteredItems.length}/{items.length} bản ghi.
              </p>
            </div>
            {loading && <span className="loading">Đang tải...</span>}
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Lead</th>
                  <th>Liên hệ</th>
                  <th>Nhân viên PT</th>
                  <th>Trạng thái</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredItems.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="empty-row">
                      {loading
                        ? "Đang tải dữ liệu..."
                        : "Không có dữ liệu phù hợp"}
                    </td>
                  </tr>
                ) : (
                  filteredItems.map((item) => (
                    <tr key={item.id}>
                      <td>
                        <div className="stacked-cell">
                          <strong>{item.tenLead}</strong>
                          <span style={{ color: "#5c6c84", fontSize: "12px" }}>
                            {item.tenCongTy || "—"}
                          </span>
                        </div>
                      </td>
                      <td>
                        <div className="stacked-cell">
                          <span>{item.soDienThoai || "—"}</span>
                          <span style={{ color: "#5c6c84", fontSize: "12px" }}>
                            {item.email || "—"}
                          </span>
                        </div>
                      </td>
                      <td>{item.tenNhanVienPhuTrach || "—"}</td>
                      <td>
                        <span
                          className={`badge badge-${String(item.tinhTrang).toLowerCase()}`}
                        >
                          {getStatusLabel(item.tinhTrang)}
                        </span>
                      </td>
                      <td>
                        <div className="row-actions">
                          <button
                            type="button"
                            className="ghost-btn"
                            onClick={() =>
                              setDetailModal({ visible: true, lead: item })
                            }
                          >
                            Chi tiết
                          </button>
                          <button
                            type="button"
                            className="ghost-btn"
                            onClick={() =>
                              setStatusModal({
                                visible: true,
                                lead: item,
                                selected: item.tinhTrang,
                              })
                            }
                          >
                            Trạng thái
                          </button>
                          <button
                            type="button"
                            className="secondary-btn"
                            onClick={() => handleEdit(item)}
                          >
                            Sửa
                          </button>
                          {item.tinhTrang === "DangChamSoc" && (
                            <button
                              type="button"
                              className="success-btn"
                              onClick={() => handleConvert(item.id)}
                            >
                              Chuyển đổi
                            </button>
                          )}
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

export default LeadManager;
