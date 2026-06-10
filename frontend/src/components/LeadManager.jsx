import React, { useState, useEffect, useMemo, useCallback } from "react";

const API_URL = "http://localhost:8081/api/leads";
const NHAN_VIEN_API = "http://localhost:8081/api/nhan-vien";

const TINH_TRANG_OPTIONS = [
  { value: "Moi", label: "Mới" },
  { value: "DangChamSoc", label: "Đang chăm sóc" },
  { value: "DaChuyenDoi", label: "Đã chuyển đổi" },
  { value: "NgungChamSoc", label: "Ngừng chăm sóc" },
];

const STATUS_STYLE = {
  Moi: { bg: "#EBF5FF", color: "#1565C0", border: "#BBDEFB" },
  DangChamSoc: { bg: "#E8F5E9", color: "#2E7D32", border: "#C8E6C9" },
  DaChuyenDoi: { bg: "#F3E5F5", color: "#6A1B9A", border: "#E1BEE7" },
  NgungChamSoc: { bg: "#FFF3E0", color: "#E65100", border: "#FFE0B2" },
};

const STATUS_LABEL = {
  Moi: "Mới",
  DangChamSoc: "Đang chăm sóc",
  DaChuyenDoi: "Đã chuyển đổi",
  NgungChamSoc: "Ngừng chăm sóc",
};

const PAGE_SIZE = 10;

function validate(form) {
  const errs = {};
  if (!form.tenLead.trim()) errs.tenLead = "Tên Lead là bắt buộc.";
  if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
    errs.email = "Email không đúng định dạng.";
  if (form.soDienThoai && !/^\d{10,11}$/.test(form.soDienThoai))
    errs.soDienThoai = "Số điện thoại phải có 10–11 chữ số.";
  return errs;
}

function Toast({ toasts, removeToast }) {
  return (
    <div
      style={{
        position: "fixed",
        top: 20,
        right: 20,
        zIndex: 9999,
        display: "flex",
        flexDirection: "column",
        gap: 8,
      }}
    >
      {toasts.map((t) => {
        const styles = {
          success: { bg: "#E8F5E9", color: "#2E7D32", border: "#A5D6A7" },
          error: { bg: "#FFEBEE", color: "#C62828", border: "#EF9A9A" },
          warning: { bg: "#FFF8E1", color: "#F57F17", border: "#FFE082" },
        }[t.type] || { bg: "#E3F2FD", color: "#1565C0", border: "#90CAF9" };
        return (
          <div
            key={t.id}
            style={{
              minWidth: 280,
              maxWidth: 380,
              padding: "12px 16px",
              background: styles.bg,
              color: styles.color,
              border: `1px solid ${styles.border}`,
              borderRadius: 6,
              fontSize: 13,
              fontWeight: 500,
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              boxShadow: "0 2px 8px rgba(0,0,0,0.10)",
            }}
          >
            <span>{t.message}</span>
            <button
              onClick={() => removeToast(t.id)}
              style={{
                background: "none",
                border: "none",
                cursor: "pointer",
                color: styles.color,
                fontSize: 16,
                marginLeft: 12,
                lineHeight: 1,
                padding: 0,
              }}
            >
              ×
            </button>
          </div>
        );
      })}
    </div>
  );
}

function ConfirmModal({ visible, title, message, onConfirm, onCancel }) {
  if (!visible) return null;
  return (
    <div
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,0.35)",
        zIndex: 1000,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div
        style={{
          background: "#fff",
          borderRadius: 8,
          padding: "28px 32px",
          minWidth: 340,
          maxWidth: 440,
          boxShadow: "0 4px 24px rgba(0,0,0,0.15)",
        }}
      >
        <p
          style={{
            fontWeight: 600,
            fontSize: 15,
            margin: "0 0 8px",
            color: "#1a1a2e",
          }}
        >
          {title}
        </p>
        <p style={{ fontSize: 13, color: "#555", margin: "0 0 24px" }}>
          {message}
        </p>
        <div style={{ display: "flex", gap: 10, justifyContent: "flex-end" }}>
          <button onClick={onCancel} style={btnOutline}>
            Huỷ
          </button>
          <button onClick={onConfirm} style={btnDanger}>
            Xác nhận
          </button>
        </div>
      </div>
    </div>
  );
}

function StatusModal({ visible, lead, onConfirm, onCancel }) {
  const [selected, setSelected] = useState("");
  useEffect(() => {
    if (lead) setSelected(lead.tinhTrang || "");
  }, [lead]);
  if (!visible || !lead) return null;
  return (
    <div
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,0.35)",
        zIndex: 1000,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div
        style={{
          background: "#fff",
          borderRadius: 8,
          padding: "28px 32px",
          minWidth: 340,
          maxWidth: 440,
          boxShadow: "0 4px 24px rgba(0,0,0,0.15)",
        }}
      >
        <p
          style={{
            fontWeight: 600,
            fontSize: 15,
            margin: "0 0 16px",
            color: "#1a1a2e",
          }}
        >
          Đổi trạng thái: {lead.tenLead}
        </p>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            gap: 8,
            marginBottom: 24,
          }}
        >
          {TINH_TRANG_OPTIONS.map((opt) => (
            <label
              key={opt.value}
              style={{
                display: "flex",
                alignItems: "center",
                gap: 10,
                padding: "8px 12px",
                borderRadius: 5,
                cursor: opt.value === "DaChuyenDoi" ? "not-allowed" : "pointer",
                border:
                  selected === opt.value
                    ? "1.5px solid #1565C0"
                    : "1px solid #e0e0e0",
                background: selected === opt.value ? "#EBF5FF" : "#fafafa",
                opacity: opt.value === "DaChuyenDoi" ? 0.5 : 1,
              }}
            >
              <input
                type="radio"
                name="tinhTrang"
                value={opt.value}
                checked={selected === opt.value}
                disabled={opt.value === "DaChuyenDoi"}
                onChange={() => setSelected(opt.value)}
                style={{ accentColor: "#1565C0" }}
              />
              <span style={{ fontSize: 13, color: "#333" }}>{opt.label}</span>
            </label>
          ))}
        </div>
        <div style={{ display: "flex", gap: 10, justifyContent: "flex-end" }}>
          <button onClick={onCancel} style={btnOutline}>
            Huỷ
          </button>
          <button onClick={() => onConfirm(selected)} style={btnPrimary}>
            Lưu thay đổi
          </button>
        </div>
      </div>
    </div>
  );
}

function DetailModal({ visible, lead, onClose }) {
  if (!visible || !lead) return null;
  const s = STATUS_STYLE[lead.tinhTrang] || {};
  return (
    <div
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,0.35)",
        zIndex: 1000,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div
        style={{
          background: "#fff",
          borderRadius: 8,
          padding: "28px 32px",
          minWidth: 400,
          maxWidth: 520,
          boxShadow: "0 4px 24px rgba(0,0,0,0.15)",
          width: "90%",
        }}
      >
        <div
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            marginBottom: 20,
          }}
        >
          <p
            style={{
              fontWeight: 600,
              fontSize: 16,
              margin: 0,
              color: "#1a1a2e",
            }}
          >
            Chi tiết Lead
          </p>
          <button
            onClick={onClose}
            style={{
              background: "none",
              border: "none",
              cursor: "pointer",
              fontSize: 20,
              color: "#888",
            }}
          >
            ×
          </button>
        </div>
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "1fr 1fr",
            gap: "12px 20px",
          }}
        >
          {[
            ["ID", lead.id],
            ["Tên Lead", lead.tenLead],
            ["Công ty", lead.tenCongTy || "—"],
            ["Email", lead.email || "—"],
            ["Số điện thoại", lead.soDienThoai || "—"],
            ["Nhân viên phụ trách", lead.tenNhanVienPhuTrach || "—"],
            [
              "Ngày tạo",
              lead.createdAt
                ? new Date(lead.createdAt).toLocaleDateString("vi-VN")
                : "—",
            ],
            [
              "Cập nhật",
              lead.updatedAt
                ? new Date(lead.updatedAt).toLocaleDateString("vi-VN")
                : "—",
            ],
          ].map(([k, v]) => (
            <div key={k}>
              <p
                style={{
                  fontSize: 11,
                  color: "#888",
                  margin: "0 0 3px",
                  textTransform: "uppercase",
                  letterSpacing: "0.05em",
                }}
              >
                {k}
              </p>
              <p
                style={{
                  fontSize: 13,
                  color: "#222",
                  margin: 0,
                  fontWeight: 500,
                }}
              >
                {v}
              </p>
            </div>
          ))}
          <div style={{ gridColumn: "1 / -1" }}>
            <p
              style={{
                fontSize: 11,
                color: "#888",
                margin: "0 0 6px",
                textTransform: "uppercase",
                letterSpacing: "0.05em",
              }}
            >
              Trạng thái
            </p>
            <span
              style={{
                padding: "3px 10px",
                borderRadius: 4,
                fontSize: 12,
                fontWeight: 600,
                background: s.bg,
                color: s.color,
                border: `1px solid ${s.border}`,
              }}
            >
              {STATUS_LABEL[lead.tinhTrang] || lead.tinhTrang}
            </span>
          </div>
        </div>
        <div style={{ marginTop: 24, textAlign: "right" }}>
          <button onClick={onClose} style={btnOutline}>
            Đóng
          </button>
        </div>
      </div>
    </div>
  );
}

export default function LeadManager() {
  const [leads, setLeads] = useState([]);
  const [nhanVienList, setNhanVienList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [toasts, setToasts] = useState([]);
  const [toastId, setToastId] = useState(0);

  const [search, setSearch] = useState({
    tenLead: "",
    tenCongTy: "",
    email: "",
    soDienThoai: "",
    tinhTrang: "",
  });
  const [tempSearch, setTempSearch] = useState({
    tenLead: "",
    tenCongTy: "",
    email: "",
    soDienThoai: "",
    tinhTrang: "",
  });

  const [currentPage, setCurrentPage] = useState(1);

  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    tenLead: "",
    tenCongTy: "",
    soDienThoai: "",
    email: "",
    nhanVienPhuTrachId: "",
  });
  const [formErrors, setFormErrors] = useState({});

  const [confirmModal, setConfirmModal] = useState({
    visible: false,
    title: "",
    message: "",
    onConfirm: null,
  });
  const [statusModal, setStatusModal] = useState({
    visible: false,
    lead: null,
  });
  const [detailModal, setDetailModal] = useState({
    visible: false,
    lead: null,
  });

  const addToast = useCallback((message, type = "success") => {
    const id = Date.now();
    setToastId((p) => p + 1);
    setToasts((prev) => [...prev, { id, message, type }]);
    setTimeout(
      () => setToasts((prev) => prev.filter((t) => t.id !== id)),
      4000,
    );
  }, []);

  const removeToast = useCallback(
    (id) => setToasts((prev) => prev.filter((t) => t.id !== id)),
    [],
  );

  const fetchLeads = useCallback(async () => {
    setLoading(true);
    try {
      const res = await fetch(API_URL);
      if (!res.ok) throw new Error("HTTP " + res.status);
      setLeads(await res.json());
    } catch (e) {
      addToast("Lỗi khi tải danh sách Lead: " + e.message, "error");
    } finally {
      setLoading(false);
    }
  }, [addToast]);

  const fetchNhanVien = useCallback(async () => {
    try {
      const res = await fetch(NHAN_VIEN_API);
      if (!res.ok) throw new Error("HTTP " + res.status);
      setNhanVienList(await res.json());
    } catch (e) {
      addToast("Lỗi khi tải danh sách nhân viên: " + e.message, "warning");
    }
  }, [addToast]);

  useEffect(() => {
    fetchLeads();
    fetchNhanVien();
  }, [fetchLeads, fetchNhanVien]);

  const filteredLeads = useMemo(() => {
    const s = search;
    return leads.filter((l) => {
      if (
        s.tenLead &&
        !l.tenLead?.toLowerCase().includes(s.tenLead.toLowerCase())
      )
        return false;
      if (
        s.tenCongTy &&
        !l.tenCongTy?.toLowerCase().includes(s.tenCongTy.toLowerCase())
      )
        return false;
      if (s.email && !l.email?.toLowerCase().includes(s.email.toLowerCase()))
        return false;
      if (s.soDienThoai && !l.soDienThoai?.includes(s.soDienThoai))
        return false;
      if (s.tinhTrang && l.tinhTrang !== s.tinhTrang) return false;
      return true;
    });
  }, [leads, search]);

  const totalPages = Math.max(1, Math.ceil(filteredLeads.length / PAGE_SIZE));
  const pagedLeads = filteredLeads.slice(
    (currentPage - 1) * PAGE_SIZE,
    currentPage * PAGE_SIZE,
  );

  const stats = useMemo(
    () => ({
      total: leads.length,
      moi: leads.filter((l) => l.tinhTrang === "Moi").length,
      dangChamSoc: leads.filter((l) => l.tinhTrang === "DangChamSoc").length,
      daChuyenDoi: leads.filter((l) => l.tinhTrang === "DaChuyenDoi").length,
    }),
    [leads],
  );

  const handleSearchInput = (e) =>
    setTempSearch((p) => ({ ...p, [e.target.name]: e.target.value }));

  const handleSearch = () => {
    setSearch({ ...tempSearch });
    setCurrentPage(1);
  };

  const handleReset = () => {
    const empty = {
      tenLead: "",
      tenCongTy: "",
      email: "",
      soDienThoai: "",
      tinhTrang: "",
    };
    setTempSearch(empty);
    setSearch(empty);
    setCurrentPage(1);
  };

  const openForm = (lead = null) => {
    if (lead) {
      setFormData({
        tenLead: lead.tenLead || "",
        tenCongTy: lead.tenCongTy || "",
        soDienThoai: lead.soDienThoai || "",
        email: lead.email || "",
        nhanVienPhuTrachId: lead.nhanVienPhuTrachId || "",
      });
      setEditingId(lead.id);
    } else {
      setFormData({
        tenLead: "",
        tenCongTy: "",
        soDienThoai: "",
        email: "",
        nhanVienPhuTrachId: "",
      });
      setEditingId(null);
    }
    setFormErrors({});
    setShowForm(true);
  };

  const closeForm = () => {
    setShowForm(false);
    setEditingId(null);
    setFormErrors({});
  };

  const handleFormInput = (e) =>
    setFormData((p) => ({ ...p, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate(formData);
    if (Object.keys(errs).length) {
      setFormErrors(errs);
      return;
    }
    setLoading(true);
    const payload = {
      ...formData,
      nhanVienPhuTrachId: formData.nhanVienPhuTrachId
        ? parseInt(formData.nhanVienPhuTrachId)
        : null,
    };
    try {
      if (editingId) {
        const res = await fetch(`${API_URL}/${editingId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (!res.ok) throw new Error("HTTP " + res.status);
        addToast("Cập nhật Lead thành công!", "success");
      } else {
        const res = await fetch(API_URL, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (!res.ok) throw new Error("HTTP " + res.status);
        addToast("Thêm mới Lead thành công!", "success");
      }
      closeForm();
      fetchLeads();
    } catch (e) {
      addToast("Lỗi: " + e.message, "error");
    } finally {
      setLoading(false);
    }
  };

  const confirmDelete = (lead) => {
    setConfirmModal({
      visible: true,
      title: "Xác nhận xoá",
      message: `Bạn có chắc chắn muốn xóa Lead "${lead.tenLead}"?`,
      onConfirm: async () => {
        setConfirmModal((p) => ({ ...p, visible: false }));
        setLoading(true);
        try {
          const res = await fetch(`${API_URL}/${lead.id}`, {
            method: "DELETE",
          });
          if (!res.ok) throw new Error("HTTP " + res.status);
          addToast("Xóa Lead thành công!", "success");
          fetchLeads();
        } catch (e) {
          addToast("Lỗi khi xóa: " + e.message, "error");
        } finally {
          setLoading(false);
        }
      },
    });
  };

  const confirmConvert = (lead) => {
    setConfirmModal({
      visible: true,
      title: "Xác nhận chuyển đổi",
      message: `Bạn có chắc chắn muốn chuyển đổi Lead "${lead.tenLead}" thành Khách Hàng?`,
      onConfirm: async () => {
        setConfirmModal((p) => ({ ...p, visible: false }));
        setLoading(true);
        try {
          const res = await fetch(`${API_URL}/${lead.id}/convert`, {
            method: "POST",
          });
          if (!res.ok) throw new Error("HTTP " + res.status);
          const kh = await res.json();
          addToast(
            `Chuyển đổi thành công! Mã KH: ${kh.maKhachHang}`,
            "success",
          );
          fetchLeads();
        } catch (e) {
          addToast("Lỗi khi chuyển đổi: " + e.message, "error");
        } finally {
          setLoading(false);
        }
      },
    });
  };

  const handleChangeStatus = async (tinhTrangMoi) => {
    const lead = statusModal.lead;
    setStatusModal({ visible: false, lead: null });
    if (!tinhTrangMoi || tinhTrangMoi === lead.tinhTrang) return;
    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/${lead.id}/status`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ tinhTrangMoi }),
      });
      if (!res.ok) throw new Error("HTTP " + res.status);
      addToast("Cập nhật trạng thái thành công!", "success");
      fetchLeads();
    } catch (e) {
      addToast("Lỗi đổi trạng thái: " + e.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <style>{CSS}</style>
      <Toast toasts={toasts} removeToast={removeToast} />
      <ConfirmModal
        {...confirmModal}
        onCancel={() => setConfirmModal((p) => ({ ...p, visible: false }))}
      />
      <StatusModal
        visible={statusModal.visible}
        lead={statusModal.lead}
        onConfirm={handleChangeStatus}
        onCancel={() => setStatusModal({ visible: false, lead: null })}
      />
      <DetailModal
        visible={detailModal.visible}
        lead={detailModal.lead}
        onClose={() => setDetailModal({ visible: false, lead: null })}
      />

      <div className="crm-wrap">
        {/* PAGE HEADER */}
        <div className="page-header">
          <div>
            <h1 className="page-title">Quản lý Lead</h1>
            <p className="page-sub">Danh sách tiềm năng kinh doanh</p>
          </div>
          <button className="btn-primary" onClick={() => openForm()}>
            + Thêm mới Lead
          </button>
        </div>

        {/* STAT CARDS */}
        <div className="stat-grid">
          {[
            { label: "Tổng Lead", value: stats.total, accent: "#1565C0" },
            { label: "Mới", value: stats.moi, accent: "#1E88E5" },
            {
              label: "Đang chăm sóc",
              value: stats.dangChamSoc,
              accent: "#2E7D32",
            },
            {
              label: "Đã chuyển đổi",
              value: stats.daChuyenDoi,
              accent: "#6A1B9A",
            },
          ].map((s) => (
            <div key={s.label} className="stat-card">
              <div className="stat-accent" style={{ background: s.accent }} />
              <div className="stat-body">
                <p className="stat-label">{s.label}</p>
                <p className="stat-value" style={{ color: s.accent }}>
                  {s.value}
                </p>
              </div>
            </div>
          ))}
        </div>

        {/* FORM */}
        {showForm && (
          <div className="card form-card">
            <div className="card-header">
              <p className="card-title">
                {editingId ? "Cập nhật Lead" : "Thêm mới Lead"}
              </p>
              <button className="btn-ghost" onClick={closeForm}>
                × Đóng
              </button>
            </div>
            <form onSubmit={handleSubmit} noValidate>
              <div className="form-grid">
                <div className="field">
                  <label className="field-label">
                    Tên Lead <span className="req">*</span>
                  </label>
                  <input
                    className={`field-input${formErrors.tenLead ? " input-err" : ""}`}
                    name="tenLead"
                    value={formData.tenLead}
                    onChange={handleFormInput}
                    placeholder="Nhập tên lead"
                  />
                  {formErrors.tenLead && (
                    <p className="err-msg">{formErrors.tenLead}</p>
                  )}
                </div>
                <div className="field">
                  <label className="field-label">Tên Công Ty</label>
                  <input
                    className="field-input"
                    name="tenCongTy"
                    value={formData.tenCongTy}
                    onChange={handleFormInput}
                    placeholder="Nhập tên công ty"
                  />
                </div>
                <div className="field">
                  <label className="field-label">Email</label>
                  <input
                    className={`field-input${formErrors.email ? " input-err" : ""}`}
                    name="email"
                    value={formData.email}
                    onChange={handleFormInput}
                    placeholder="example@email.com"
                  />
                  {formErrors.email && (
                    <p className="err-msg">{formErrors.email}</p>
                  )}
                </div>
                <div className="field">
                  <label className="field-label">Số Điện Thoại</label>
                  <input
                    className={`field-input${formErrors.soDienThoai ? " input-err" : ""}`}
                    name="soDienThoai"
                    value={formData.soDienThoai}
                    onChange={handleFormInput}
                    placeholder="0xxxxxxxxx"
                  />
                  {formErrors.soDienThoai && (
                    <p className="err-msg">{formErrors.soDienThoai}</p>
                  )}
                </div>
                <div className="field" style={{ gridColumn: "1 / -1" }}>
                  <label className="field-label">Nhân Viên Phụ Trách</label>
                  <select
                    className="field-input"
                    name="nhanVienPhuTrachId"
                    value={formData.nhanVienPhuTrachId}
                    onChange={handleFormInput}
                  >
                    <option value="">— Chọn nhân viên —</option>
                    {nhanVienList.map((nv) => (
                      <option key={nv.id} value={nv.id}>
                        {nv.hoTen}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
              <div className="form-actions">
                <button
                  type="button"
                  className="btn-outline"
                  onClick={closeForm}
                >
                  Huỷ
                </button>
                <button
                  type="submit"
                  className="btn-primary"
                  disabled={loading}
                >
                  {loading
                    ? "Đang lưu..."
                    : editingId
                      ? "Cập nhật"
                      : "Thêm mới"}
                </button>
              </div>
            </form>
          </div>
        )}

        {/* FILTER */}
        <div className="card filter-card">
          <p className="card-title" style={{ marginBottom: 12 }}>
            Tìm kiếm & Lọc
          </p>
          <div className="filter-grid">
            <input
              className="field-input"
              name="tenLead"
              value={tempSearch.tenLead}
              onChange={handleSearchInput}
              placeholder="Tên Lead"
            />
            <input
              className="field-input"
              name="tenCongTy"
              value={tempSearch.tenCongTy}
              onChange={handleSearchInput}
              placeholder="Công ty"
            />
            <input
              className="field-input"
              name="email"
              value={tempSearch.email}
              onChange={handleSearchInput}
              placeholder="Email"
            />
            <input
              className="field-input"
              name="soDienThoai"
              value={tempSearch.soDienThoai}
              onChange={handleSearchInput}
              placeholder="Số điện thoại"
            />
            <select
              className="field-input"
              name="tinhTrang"
              value={tempSearch.tinhTrang}
              onChange={handleSearchInput}
            >
              <option value="">Tất cả trạng thái</option>
              {TINH_TRANG_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>
                  {o.label}
                </option>
              ))}
            </select>
          </div>
          <div className="filter-actions">
            <button className="btn-primary" onClick={handleSearch}>
              Tìm kiếm
            </button>
            <button className="btn-outline" onClick={handleReset}>
              Đặt lại
            </button>
          </div>
        </div>

        {/* TABLE */}
        <div className="card table-card">
          <div className="table-header-row">
            <p className="card-title">
              Danh sách Lead{" "}
              <span className="count-badge">{filteredLeads.length}</span>
            </p>
          </div>
          {loading ? (
            <div className="loading-row">Đang tải dữ liệu...</div>
          ) : (
            <div className="table-wrap">
              <table className="crm-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Tên Lead</th>
                    <th>Công Ty</th>
                    <th>Email</th>
                    <th>Số Điện Thoại</th>
                    <th>Nhân Viên PT</th>
                    <th>Trạng Thái</th>
                    <th>Ngày Tạo</th>
                    <th>Hành Động</th>
                  </tr>
                </thead>
                <tbody>
                  {pagedLeads.length === 0 ? (
                    <tr>
                      <td
                        colSpan={9}
                        style={{
                          textAlign: "center",
                          padding: "32px",
                          color: "#999",
                          fontSize: 13,
                        }}
                      >
                        Không có dữ liệu phù hợp
                      </td>
                    </tr>
                  ) : (
                    pagedLeads.map((lead) => {
                      const s = STATUS_STYLE[lead.tinhTrang] || {};
                      return (
                        <tr key={lead.id}>
                          <td className="td-id">#{lead.id}</td>
                          <td className="td-name">{lead.tenLead}</td>
                          <td>
                            {lead.tenCongTy || (
                              <span className="empty-cell">—</span>
                            )}
                          </td>
                          <td>
                            {lead.email || (
                              <span className="empty-cell">—</span>
                            )}
                          </td>
                          <td>
                            {lead.soDienThoai || (
                              <span className="empty-cell">—</span>
                            )}
                          </td>
                          <td>
                            {lead.tenNhanVienPhuTrach || (
                              <span className="empty-cell">—</span>
                            )}
                          </td>
                          <td>
                            <span
                              className="status-badge"
                              style={{
                                background: s.bg,
                                color: s.color,
                                border: `1px solid ${s.border}`,
                              }}
                            >
                              {STATUS_LABEL[lead.tinhTrang] || lead.tinhTrang}
                            </span>
                          </td>
                          <td style={{ fontSize: 12, color: "#666" }}>
                            {lead.createdAt
                              ? new Date(lead.createdAt).toLocaleDateString(
                                  "vi-VN",
                                )
                              : "—"}
                          </td>
                          <td>
                            <div className="action-group">
                              <button
                                className="act-btn act-info"
                                onClick={() =>
                                  setDetailModal({ visible: true, lead })
                                }
                              >
                                Chi tiết
                              </button>
                              <button
                                className="act-btn act-edit"
                                onClick={() => openForm(lead)}
                              >
                                Sửa
                              </button>
                              <button
                                className="act-btn act-status"
                                onClick={() =>
                                  setStatusModal({ visible: true, lead })
                                }
                              >
                                Trạng thái
                              </button>
                              <button
                                className="act-btn act-del"
                                onClick={() => confirmDelete(lead)}
                              >
                                Xoá
                              </button>
                              {lead.tinhTrang === "DangChamSoc" && (
                                <button
                                  className="act-btn act-convert"
                                  onClick={() => confirmConvert(lead)}
                                >
                                  Chuyển đổi
                                </button>
                              )}
                            </div>
                          </td>
                        </tr>
                      );
                    })
                  )}
                </tbody>
              </table>
            </div>
          )}

          {/* PAGINATION */}
          {!loading && totalPages > 1 && (
            <div className="pagination">
              <span className="page-info">
                Trang {currentPage} / {totalPages} — {filteredLeads.length} bản
                ghi
              </span>
              <div className="page-btns">
                <button
                  className="page-btn"
                  disabled={currentPage === 1}
                  onClick={() => setCurrentPage(1)}
                >
                  Đầu
                </button>
                <button
                  className="page-btn"
                  disabled={currentPage === 1}
                  onClick={() => setCurrentPage((p) => p - 1)}
                >
                  Trước
                </button>
                {Array.from({ length: totalPages }, (_, i) => i + 1)
                  .filter(
                    (p) =>
                      p === 1 ||
                      p === totalPages ||
                      Math.abs(p - currentPage) <= 1,
                  )
                  .reduce((acc, p, i, arr) => {
                    if (i > 0 && p - arr[i - 1] > 1) acc.push("...");
                    acc.push(p);
                    return acc;
                  }, [])
                  .map((item, i) =>
                    item === "..." ? (
                      <span key={`el-${i}`} className="page-ellipsis">
                        …
                      </span>
                    ) : (
                      <button
                        key={item}
                        className={`page-btn${currentPage === item ? " active" : ""}`}
                        onClick={() => setCurrentPage(item)}
                      >
                        {item}
                      </button>
                    ),
                  )}
                <button
                  className="page-btn"
                  disabled={currentPage === totalPages}
                  onClick={() => setCurrentPage((p) => p + 1)}
                >
                  Sau
                </button>
                <button
                  className="page-btn"
                  disabled={currentPage === totalPages}
                  onClick={() => setCurrentPage(totalPages)}
                >
                  Cuối
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </>
  );
}

/* ─── BUTTON BASE STYLES ─── */
const btnPrimary = {
  padding: "9px 20px",
  background: "#1565C0",
  color: "#fff",
  border: "none",
  borderRadius: 5,
  cursor: "pointer",
  fontSize: 13,
  fontWeight: 600,
};
const btnOutline = {
  padding: "9px 20px",
  background: "#fff",
  color: "#1565C0",
  border: "1.5px solid #1565C0",
  borderRadius: 5,
  cursor: "pointer",
  fontSize: 13,
  fontWeight: 500,
};
const btnDanger = {
  padding: "9px 20px",
  background: "#C62828",
  color: "#fff",
  border: "none",
  borderRadius: 5,
  cursor: "pointer",
  fontSize: 13,
  fontWeight: 600,
};

/* ─── CSS ─── */
const CSS = `
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif; background: #F4F6F9; color: #1a1a2e; }

.crm-wrap { max-width: 1400px; margin: 0 auto; padding: 28px 24px; }

.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1a1a2e; }
.page-sub { font-size: 13px; color: #888; margin-top: 3px; }

.btn-primary { padding: 9px 20px; background: #1565C0; color: #fff; border: none; border-radius: 5px; cursor: pointer; font-size: 13px; font-weight: 600; }
.btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
.btn-primary:hover:not(:disabled) { background: #1976D2; }
.btn-outline { padding: 9px 20px; background: #fff; color: #1565C0; border: 1.5px solid #1565C0; border-radius: 5px; cursor: pointer; font-size: 13px; font-weight: 500; }
.btn-outline:hover { background: #EBF5FF; }
.btn-ghost { background: none; border: none; cursor: pointer; color: #666; font-size: 14px; padding: 4px 8px; border-radius: 4px; }
.btn-ghost:hover { background: #f0f0f0; }

/* STAT */
.stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 14px; margin-bottom: 20px; }
.stat-card { background: #fff; border-radius: 7px; border: 1px solid #E8ECF0; display: flex; overflow: hidden; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.stat-accent { width: 5px; flex-shrink: 0; }
.stat-body { padding: 16px 18px; }
.stat-label { font-size: 11px; color: #888; text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 6px; }
.stat-value { font-size: 28px; font-weight: 700; }

/* CARD */
.card { background: #fff; border-radius: 7px; border: 1px solid #E8ECF0; margin-bottom: 16px; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.card-header { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; border-bottom: 1px solid #F0F2F5; }
.card-title { font-size: 14px; font-weight: 600; color: #1a1a2e; }
.form-card { padding: 0; }
.form-card form { padding: 20px; }

/* FORM */
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.field { display: flex; flex-direction: column; gap: 5px; }
.field-label { font-size: 12px; font-weight: 600; color: #444; letter-spacing: 0.03em; }
.req { color: #C62828; }
.field-input { height: 36px; padding: 0 10px; border: 1px solid #D1D9E0; border-radius: 5px; font-size: 13px; color: #222; outline: none; background: #fff; width: 100%; }
.field-input:focus { border-color: #1565C0; box-shadow: 0 0 0 2px rgba(21,101,192,0.12); }
select.field-input { height: 36px; }
.input-err { border-color: #C62828 !important; }
.err-msg { font-size: 11px; color: #C62828; margin-top: 2px; }
.form-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px; padding-top: 16px; border-top: 1px solid #F0F2F5; }

/* FILTER */
.filter-card { padding: 18px 20px; }
.filter-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 10px; margin-bottom: 12px; }
.filter-actions { display: flex; gap: 10px; }

/* TABLE */
.table-card { padding: 0; overflow: hidden; }
.table-header-row { padding: 14px 20px; border-bottom: 1px solid #F0F2F5; }
.count-badge { display: inline-block; margin-left: 8px; padding: 1px 8px; background: #EBF5FF; color: #1565C0; border-radius: 10px; font-size: 11px; font-weight: 600; }
.table-wrap { overflow-x: auto; }
.crm-table { width: 100%; border-collapse: collapse; font-size: 13px; min-width: 960px; }
.crm-table thead tr { background: #F7F9FC; border-bottom: 1.5px solid #E8ECF0; }
.crm-table th { padding: 10px 14px; text-align: left; font-size: 11px; font-weight: 700; color: #666; text-transform: uppercase; letter-spacing: 0.05em; white-space: nowrap; }
.crm-table tbody tr { border-bottom: 1px solid #F0F2F5; transition: background 0.1s; }
.crm-table tbody tr:hover { background: #F7F9FC; }
.crm-table td { padding: 11px 14px; color: #333; vertical-align: middle; }
.td-id { color: #999; font-size: 12px; }
.td-name { font-weight: 600; color: #1a1a2e; }
.empty-cell { color: #bbb; }
.loading-row { text-align: center; padding: 40px; color: #888; font-size: 13px; }

/* STATUS BADGE */
.status-badge { padding: 3px 9px; border-radius: 4px; font-size: 11px; font-weight: 600; white-space: nowrap; }

/* ACTION BUTTONS */
.action-group { display: flex; gap: 5px; flex-wrap: wrap; }
.act-btn { padding: 4px 10px; border-radius: 4px; font-size: 11px; font-weight: 600; cursor: pointer; border: 1px solid; white-space: nowrap; }
.act-info { background: #F3F6FF; color: #1565C0; border-color: #BBDEFB; }
.act-info:hover { background: #DDEEFF; }
.act-edit { background: #FFF8E1; color: #F57F17; border-color: #FFE082; }
.act-edit:hover { background: #FFF3CD; }
.act-status { background: #F3E5F5; color: #6A1B9A; border-color: #E1BEE7; }
.act-status:hover { background: #EDD5F9; }
.act-del { background: #FFEBEE; color: #C62828; border-color: #EF9A9A; }
.act-del:hover { background: #FDDEDE; }
.act-convert { background: #E8F5E9; color: #2E7D32; border-color: #A5D6A7; }
.act-convert:hover { background: #D4EDC9; }

/* PAGINATION */
.pagination { display: flex; align-items: center; justify-content: space-between; padding: 12px 20px; border-top: 1px solid #F0F2F5; }
.page-info { font-size: 12px; color: #888; }
.page-btns { display: flex; gap: 4px; align-items: center; }
.page-btn { min-width: 32px; height: 32px; padding: 0 8px; border: 1px solid #D1D9E0; border-radius: 4px; background: #fff; font-size: 12px; cursor: pointer; color: #444; }
.page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.page-btn:hover:not(:disabled):not(.active) { background: #F0F4FF; border-color: #1565C0; color: #1565C0; }
.page-btn.active { background: #1565C0; color: #fff; border-color: #1565C0; font-weight: 600; }
.page-ellipsis { padding: 0 4px; color: #aaa; font-size: 13px; }

@media (max-width: 1100px) {
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
  .filter-grid { grid-template-columns: repeat(3, 1fr); }
  .form-grid { grid-template-columns: 1fr; }
}
@media (max-width: 700px) {
  .stat-grid { grid-template-columns: 1fr 1fr; }
  .filter-grid { grid-template-columns: 1fr 1fr; }
  .page-header { flex-direction: column; align-items: flex-start; gap: 10px; }
}
`;
