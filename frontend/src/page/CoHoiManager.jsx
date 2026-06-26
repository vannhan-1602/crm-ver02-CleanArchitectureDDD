import { useState, useEffect, useMemo } from "react";
import { api as ax, canWriteModule } from "../apiClient";
import { ActionIcon } from "../moduleIcons.jsx";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";
import "./Cohoimanager.css"
import "./ManagerForm.css";
const formatVND = (n) =>
    new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(n ?? 0);

const GIAI_DOAN_LIST = [
    { value: "TiepCan",     label: "Tiếp cận",     color: "#e0e7ff", text: "#3730a3" },
    { value: "DeXuat",      label: "Đề xuất",      color: "#fef9c3", text: "#854d0e" },
    { value: "ThuongLuong", label: "Thương lượng", color: "#ffedd5", text: "#c2410c" },
    { value: "ThanhCong",   label: "Thành công",   color: "#dcfce7", text: "#166534" },
    { value: "ThatBai",     label: "Thất bại",     color: "#fee2e2", text: "#991b1b" },
];

const EMPTY_FORM = {
    tenThuongVu: "",
    giaiDoan: "TiepCan",
    tyLeThanhCong: "",
    doanhThuKyVong: "",
    ngayDuKien: "",
    ghiChu: "",
    nhanVienPhuTrach_Id: "",
    lead_Id: "",
    khachHang_Id: "",
};

function gdiInfo(value) {
    return GIAI_DOAN_LIST.find((g) => g.value === value) ?? { label: value, color: "#f3f4f6", text: "#374151" };
}

/* ─── GiaiDoanBadge ─── */
function GiaiDoanBadge({ value }) {
    const g = gdiInfo(value);
    return (
        <span style={{
            display: "inline-block", padding: "2px 10px", borderRadius: 20,
            fontSize: 11, fontWeight: 700, background: g.color, color: g.text, whiteSpace: "nowrap",
        }}>
            {g.label}
        </span>
    );
}

/* ─── TyLeBar ─── */
function TyLeBar({ value }) {
    const pct = Math.min(100, Math.max(0, Number(value) || 0));
    const color = pct >= 70 ? "#22c55e" : pct >= 40 ? "#f59e0b" : "#ef4444";
    return (
        <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
            <div style={{ flex: 1, height: 6, background: "#e5e7eb", borderRadius: 4, overflow: "hidden", minWidth: 50 }}>
                <div style={{ width: `${pct}%`, height: "100%", background: color, borderRadius: 4, transition: "width 0.4s ease" }} />
            </div>
            <span style={{ fontSize: 11, fontWeight: 600, color, minWidth: 28 }}>{pct}%</span>
        </div>
    );
}

/* ─── SelectField ─── */
function SelectField({ label, name, value, onChange, options, placeholder, required }) {
    return (
        <label style={{ display: "flex", flexDirection: "column", gap: 4 }}>
            <span style={{ fontSize: 12, fontWeight: 600, color: "#374151" }}>
                {label} {required && <span style={{ color: "#ef4444" }}>*</span>}
            </span>
            <select
                name={name}
                value={value}
                onChange={onChange}
                style={{
                    padding: "8px 10px", borderRadius: 8, border: "1.5px solid #e5e7eb",
                    fontSize: 13, color: value === "" ? "#9ca3af" : "#111827",
                    background: "#fff", cursor: "pointer", outline: "none",
                    transition: "border-color 0.2s",
                }}
                onFocus={e => e.target.style.borderColor = "#6366f1"}
                onBlur={e => e.target.style.borderColor = "#e5e7eb"}
            >
                <option value="">{placeholder}</option>
                {options.map(o => (
                    <option key={o.value} value={o.value}>{o.label}</option>
                ))}
            </select>
        </label>
    );
}

/* ─── DetailModal ─── */
function DetailModal({ item, onClose, onEdit, nhanViens, leads, khachHangs, canWrite }) {
    useEffect(() => {
        const h = (e) => { if (e.key === "Escape") onClose(); };
        window.addEventListener("keydown", h);
        return () => window.removeEventListener("keydown", h);
    }, [onClose]);

    const g = gdiInfo(item.giaiDoan);

    // Resolve tên từ danh sách nếu response trả về Id (fallback)
    const tenNV = item.tenNhanVienPhuTrach && item.tenNhanVienPhuTrach !== "Không xác định"
        ? item.tenNhanVienPhuTrach
        : nhanViens.find(n => n.value === String(item.nhanVienPhuTrachId))?.label ?? "—";

    const tenLead = item.tenLead && item.tenLead !== "Không xác định"
        ? item.tenLead
        : leads.find(l => l.value === String(item.leadId))?.label ?? "—";

    const tenKH = item.tenKhachHang && item.tenKhachHang !== "Không xác định"
        ? item.tenKhachHang
        : khachHangs.find(k => k.value === String(item.khachHangId))?.label ?? "—";

    return (
        <div className="coho-overlay" onClick={onClose}>
            <div className="coho-modal coho-modal-detail" onClick={(e) => e.stopPropagation()}>
                <div className="coho-modal-header">
                    <div>
                        <p className="coho-eyebrow">Chi tiết cơ hội</p>
                        <h3>{item.tenThuongVu}</h3>
                    </div>
                    <button className="coho-modal-close" onClick={onClose}>×</button>
                </div>
                <div className="coho-detail-body">
                    <div className="coho-stage-banner" style={{ background: g.color, borderColor: g.text + "33" }}>
                        <span style={{ fontWeight: 700, color: g.text, fontSize: 14 }}>Giai đoạn: {g.label}</span>
                        <TyLeBar value={item.tyLeThanhCong} />
                    </div>
                    <div className="coho-info-table">
                        {[
                            ["Mã cơ hội",            `#${item.id}`],
                            ["Doanh thu kỳ vọng",    formatVND(item.doanhThuKyVong)],
                            ["Ngày dự kiến",         item.ngayDuKien ?? "—"],
                            ["Nhân viên phụ trách",  tenNV],
                            ["Lead",                 tenLead],
                            ["Khách hàng",           tenKH],
                            ["Ngày tạo",             new Date(item.createdAt).toLocaleDateString("vi-VN")],
                            ["Cập nhật",             new Date(item.updatedAt).toLocaleDateString("vi-VN")],
                        ].map(([label, val]) => (
                            <div className="coho-info-row" key={label}>
                                <span className="coho-info-label">{label}</span>
                                <span style={{ fontWeight: 500, color: "#111827" }}>{val}</span>
                            </div>
                        ))}
                    </div>
                    {item.ghiChu && (
                        <div className="coho-note-block">
                            <p className="coho-note-label">Ghi chú</p>
                            <p className="coho-note-text">{item.ghiChu}</p>
                        </div>
                    )}
                    <div className="coho-detail-actions">
                        {canWrite ? (
                            <button className="coho-primary-btn btn-icon" onClick={() => { onEdit(item); onClose(); }}>
                                <ActionIcon name="edit" /> Ch?nh s?a
                            </button>
                        ) : null}
                        <button className="coho-ghost-btn btn-icon" onClick={onClose}><ActionIcon name="close" /> Đóng</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

/* ═══════════════════════════════════════
   Main Component
═══════════════════════════════════════ */
export default function CoHoiManager() {
    const canWriteCoHoi = canWriteModule("CO_HOI");
    const [items, setItems]                 = useState([]);
    const [loading, setLoading]             = useState(true);
    const [search, setSearch]               = useState("");
    const [filterGD, setFilterGD]           = useState("");
    const [form, setForm]                   = useState(EMPTY_FORM);
    const [editId, setEditId]               = useState(null);
    const [saving, setSaving]               = useState(false);
    const [message, setMessage]             = useState({ type: "", text: "" });
    const [deleteConfirm, setDeleteConfirm] = useState(null);
    const [detailItem, setDetailItem]       = useState(null);

    // Danh sách cho select
    const [nhanViens, setNhanViens]   = useState([]);
    const [leads, setLeads]           = useState([]);
    const [khachHangs, setKhachHangs] = useState([]);

    /* ── fetch lookup data ── */
    useEffect(() => {
        ax.get("/api/nhan-vien").then(res =>
            setNhanViens(res.data.map(n => ({ value: String(n.id), label: n.hoTen })))
        ).catch(() => {});

        ax.get("/api/leads").then(res =>
            setLeads(res.data.map(l => ({ value: String(l.id), label: `${l.tenLead}${l.tenCongTy ? ` — ${l.tenCongTy}` : ""}` })))
        ).catch(() => {});

        ax.get("/api/khach-hang").then(res =>
            setKhachHangs(res.data.map(k => ({ value: String(k.id), label: `${k.tenKhachHang} (${k.maKhachHang})` })))
        ).catch(() => {});
    }, []);

    /* ── fetch list ── */
    const fetchAll = async () => {
        try {
            setLoading(true);
            const res = await ax.get("/api/cohoi");
            setItems(res.data);
        } catch {
            setMessage({ type: "error", text: "Không thể tải dữ liệu. Kiểm tra kết nối backend." });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchAll(); }, []);

    /* ── derived ── */
    const filtered = useMemo(() => items.filter((p) => {
        const kw = search.trim().toLowerCase();
        const matchSearch = !kw || p.tenThuongVu?.toLowerCase().includes(kw) || String(p.id).includes(kw);
        const matchGD     = !filterGD || p.giaiDoan === filterGD;
        return matchSearch && matchGD && !p.isDeleted;
    }), [items, search, filterGD]);

    const stats = useMemo(() => {
        const active = items.filter((i) => !i.isDeleted);
        return {
            total:       active.length,
            thanhCong:   active.filter((i) => i.giaiDoan === "ThanhCong").length,
            dangChay:    active.filter((i) => !["ThanhCong", "ThatBai"].includes(i.giaiDoan)).length,
            tongDoanhThu: active
                .filter((i) => i.giaiDoan === "ThanhCong")
                .reduce((s, i) => s + (i.doanhThuKyVong ?? 0), 0),
        };
    }, [items]);

    /* ── form helpers ── */
    const resetForm = () => { setForm(EMPTY_FORM); setEditId(null); setMessage({ type: "", text: "" }); };

    const openEdit = (p) => {
        setForm({
            tenThuongVu:         p.tenThuongVu ?? "",
            giaiDoan:            p.giaiDoan ?? "TiepCan",
            tyLeThanhCong:       p.tyLeThanhCong ?? "",
            doanhThuKyVong:      p.doanhThuKyVong ?? "",
            ngayDuKien:          p.ngayDuKien ? p.ngayDuKien.slice(0, 10) : "",
            ghiChu:              p.ghiChu ?? "",
            // API trả về nhanVienPhuTrachId (camelCase) hoặc nhanVienPhuTrach_Id
            nhanVienPhuTrach_Id: String(p.nhanVienPhuTrachId ?? p.nhanVienPhuTrach_Id ?? ""),
            lead_Id:             String(p.leadId ?? p.lead_Id ?? ""),
            khachHang_Id:        String(p.khachHangId ?? p.khachHang_Id ?? ""),
        });
        setEditId(p.id);
        setMessage({ type: "", text: "" });
        window.scrollTo({ top: 0, behavior: "smooth" });
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const validate = () => {
        if (!form.tenThuongVu.trim()) return "Tên thương vụ không được rỗng";
        if (!form.giaiDoan)           return "Vui lòng chọn giai đoạn";
        const tl = Number(form.tyLeThanhCong);
        if (form.tyLeThanhCong !== "" && (tl < 0 || tl > 100)) return "Tỷ lệ thành công phải từ 0–100";
        return "";
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const err = validate();
        if (err) { setMessage({ type: "error", text: err }); return; }
        setSaving(true);
        try {
            const payload = {
                tenThuongVu:         form.tenThuongVu,
                giaiDoan:            form.giaiDoan,
                tyLeThanhCong:       form.tyLeThanhCong !== "" ? Number(form.tyLeThanhCong) : null,
                doanhThuKyVong:      form.doanhThuKyVong !== "" ? Number(form.doanhThuKyVong) : null,
                ngayDuKien:          form.ngayDuKien || null,
                ghiChu:              form.ghiChu || null,
                nhanVienPhuTrach_Id: form.nhanVienPhuTrach_Id !== "" ? Number(form.nhanVienPhuTrach_Id) : null,
                lead_Id:             form.lead_Id !== "" ? Number(form.lead_Id) : null,
                khachHang_Id:        form.khachHang_Id !== "" ? Number(form.khachHang_Id) : null,
            };
            editId
                ? await ax.put(`/api/cohoi/${editId}`, payload)
                : await ax.post("/api/cohoi", payload);
            await fetchAll();
            resetForm();
            setMessage({ type: "success", text: editId ? "Cập nhật thành công!" : "Thêm cơ hội thành công!" });
        } catch {
            setMessage({ type: "error", text: "Lưu thất bại. Kiểm tra lại dữ liệu." });
        } finally {
            setSaving(false);
        }
    };

    const handleDelete = async (p) => {
        try {
            await ax.delete(`/api/cohoi/${p.id}`);
            setDeleteConfirm(null);
            await fetchAll();
            setMessage({ type: "success", text: "Đã xóa cơ hội." });
        } catch {
            setMessage({ type: "error", text: "Xóa thất bại." });
        }
    };

    // Helper hiển thị tên NV trong bảng
    const getTenNV = (item) => {
        if (item.tenNhanVienPhuTrach && item.tenNhanVienPhuTrach !== "Không xác định")
            return item.tenNhanVienPhuTrach;
        const id = item.nhanVienPhuTrachId ?? item.nhanVienPhuTrach_Id;
        return nhanViens.find(n => n.value === String(id))?.label ?? "—";
    };

    /* ═══ RENDER ═══ */
    return (
        <main className="coho-page">

            {/* ── Header ── */}
            <section className="coho-header">
                <div>
                    <p className="coho-eyebrow">CRM / Bán hàng</p>
                    <h1>Quản lý Cơ hội</h1>
                    <p className="coho-subtitle">Theo dõi và quản lý các cơ hội bán hàng theo từng giai đoạn.</p>
                </div>
                <div className="coho-toolbar">
                    <input
                        className="coho-search"
                        type="search"
                        placeholder="Tìm theo tên, mã..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                    <select
                        className="coho-search coho-filter"
                        value={filterGD}
                        onChange={(e) => setFilterGD(e.target.value)}
                    >
                        <option value="">Tất cả giai đoạn</option>
                        {GIAI_DOAN_LIST.map((g) => (
                            <option key={g.value} value={g.value}>{g.label}</option>
                        ))}
                    </select>
                    <button className="coho-secondary-btn btn-icon" type="button" onClick={fetchAll}><ActionIcon name="refresh" /> Tải lại</button>
                </div>
            </section>

            {/* ── Stats ── */}
            <section className="coho-stats-row">
                <article className="coho-stat-card">
                    <span>Tổng cơ hội</span>
                    <strong>{stats.total}</strong>
                </article>
                <article className="coho-stat-card">
                    <span>Đang xử lý</span>
                    <strong>{stats.dangChay}</strong>
                </article>
                <article className="coho-stat-card coho-stat-success">
                    <span>Thành công</span>
                    <strong>{stats.thanhCong}</strong>
                </article>
                <article className="coho-stat-card coho-stat-revenue">
                    <span>Doanh thu đã chốt</span>
                    <strong className="coho-stat-revenue-val">{formatVND(stats.tongDoanhThu)}</strong>
                </article>
            </section>

            {/* ── Pipeline ── */}
            <section className="coho-pipeline">
                {GIAI_DOAN_LIST.map((g) => {
                    const count = items.filter((i) => i.giaiDoan === g.value && !i.isDeleted).length;
                    const total = items.filter((i) => !i.isDeleted).length || 1;
                    const pct   = Math.round((count / total) * 100);
                    return (
                        <div
                            key={g.value}
                            className={`coho-pipeline-stage ${filterGD === g.value ? "active" : ""}`}
                            style={{ "--accent": g.text, "--bg": g.color }}
                            onClick={() => setFilterGD(filterGD === g.value ? "" : g.value)}
                            title={`Lọc: ${g.label}`}
                        >
                            <span className="coho-pipeline-label">{g.label}</span>
                            <strong className="coho-pipeline-count">{count}</strong>
                            <div className="coho-pipeline-bar">
                                <div className="coho-pipeline-fill" style={{ width: `${pct}%` }} />
                            </div>
                        </div>
                    );
                })}
            </section>

            {/* ── Main grid ── */}
            <section className="coho-grid" style={!canWriteCoHoi ? { gridTemplateColumns: "1fr" } : undefined}>

                {/* ── Form panel ── */}
                {canWriteCoHoi ? <div className="coho-panel coho-form-panel">
                    <div className={`coho-panel-head form-panel-head ${editId ? "is-edit" : ""}`}>
                        <div className="form-title-wrap">
                            <div className="form-title-icon" aria-hidden="true">{editId ? "✎" : "+"}</div>
                            <div>
                                <span className="form-mode-badge">{editId ? "Đang chỉnh sửa" : "Tạo mới"}</span>
                            <h2>{editId ? "Cập nhật cơ hội" : "Thêm cơ hội mới"}</h2>
                                <p>Nhập thông tin thương vụ, doanh thu và người phụ trách.</p>
                            </div>
                        </div>
                        {editId && (
                            <button className="coho-ghost-btn form-cancel-btn btn-icon" type="button" onClick={resetForm}><ActionIcon name="close" /> Hủy sửa</button>
                        )}
                    </div>

                    <form className="coho-form-inner" onSubmit={handleSubmit}>
                        <div className="manager-form-body">
                            <div className="form-section">
                                <div className="section-title">Thông tin thương vụ</div>
                                <label className="field">
                                    Tên thương vụ <span className="coho-req">*</span>
                                    <input
                                        name="tenThuongVu"
                                        value={form.tenThuongVu}
                                        onChange={handleChange}
                                        placeholder="VD: Bán gói CRM Pro cho công ty ABC"
                                    />
                                </label>

                                <div className="coho-two-col">
                                    <label className="field">
                                        Giai đoạn <span className="coho-req">*</span>
                                        <select name="giaiDoan" value={form.giaiDoan} onChange={handleChange}>
                                            {GIAI_DOAN_LIST.map((g) => (
                                                <option key={g.value} value={g.value}>{g.label}</option>
                                            ))}
                                        </select>
                                    </label>
                                    <label className="field">
                                        Tỷ lệ thành công (%)
                                        <input
                                            name="tyLeThanhCong"
                                            type="number"
                                            min={0} max={100}
                                            value={form.tyLeThanhCong}
                                            onChange={handleChange}
                                            placeholder="0 – 100"
                                        />
                                    </label>
                                </div>

                                {form.tyLeThanhCong !== "" && (
                                    <div style={{ padding: "4px 0" }}>
                                        <TyLeBar value={form.tyLeThanhCong} />
                                    </div>
                                )}
                            </div>

                            <div className="form-section">
                                <div className="section-title">Doanh thu và thời gian</div>
                                <div className="coho-two-col">
                                    <label className="field">
                                        Doanh thu kỳ vọng
                                        <div className="input-affix">
                                            <span>VNĐ</span>
                                            <input
                                                name="doanhThuKyVong"
                                                type="number" min={0}
                                                value={form.doanhThuKyVong}
                                                onChange={handleChange}
                                                placeholder="0"
                                            />
                                        </div>
                                    </label>
                                    <label className="field">
                                        Ngày dự kiến
                                        <input
                                            name="ngayDuKien"
                                            type="date"
                                            value={form.ngayDuKien}
                                            onChange={handleChange}
                                        />
                                    </label>
                                </div>
                            </div>

                            <div className="form-section">
                                <div className="section-title">Liên kết và phụ trách</div>
                                <SelectField
                                    label="Nhân viên phụ trách"
                                    name="nhanVienPhuTrach_Id"
                                    value={form.nhanVienPhuTrach_Id}
                                    onChange={handleChange}
                                    options={nhanViens}
                                    placeholder="— Chọn nhân viên —"
                                />

                                <div className="coho-two-col">
                                    <SelectField
                                        label="Lead"
                                        name="lead_Id"
                                        value={form.lead_Id}
                                        onChange={handleChange}
                                        options={leads}
                                        placeholder="— Chọn lead —"
                                    />
                                    <SelectField
                                        label="Khách hàng"
                                        name="khachHang_Id"
                                        value={form.khachHang_Id}
                                        onChange={handleChange}
                                        options={khachHangs}
                                        placeholder="— Chọn khách hàng —"
                                    />
                                </div>
                            </div>

                            <div className="form-section">
                                <div className="section-title">Ghi chú</div>
                                <label className="field">
                                    Ghi chú
                                    <textarea
                                        name="ghiChu"
                                        value={form.ghiChu}
                                        onChange={handleChange}
                                        placeholder="Ghi chú thêm về thương vụ..."
                                        rows={3}
                                    />
                                </label>
                            </div>

                            {message.text && (
                                <div className={`coho-message coho-message-${message.type}`}>{message.text}</div>
                            )}

                            <div className="coho-actions">
                                <button className="coho-secondary-btn btn-icon" type="button" onClick={resetForm}>
                                    <ActionIcon name="refresh" /> Làm mới
                                </button>
                                <button className="coho-primary-btn btn-icon" type="submit" disabled={saving}>
                      <ActionIcon name="save" />
                                    {saving ? "Đang lưu..." : editId ? "Cập nhật cơ hội" : "Thêm cơ hội"}
                                </button>
                            </div>
                        </div>
                    </form>
                </div> : null}

                {/* ── Table panel ── */}
                <div className="coho-panel coho-table-panel">
                    <div className="coho-panel-head">
                        <div>
                            <h2>Danh sách cơ hội</h2>
                            <p>Hiển thị {filtered.length}/{items.filter((i) => !i.isDeleted).length} bản ghi.</p>
                        </div>
                        {loading && <span className="coho-loading">Đang tải...</span>}
                    </div>

                    <div className="coho-table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Tên thương vụ</th>
                                <th>Giai đoạn</th>
                                <th>Tỷ lệ</th>
                                <th className="right">Doanh thu KV</th>
                                <th>Ngày DK</th>
                                <th>NV phụ trách</th>
                                <th className="right">Hành động</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.length === 0 ? (
                                <tr>
                                    <td colSpan={8} className="coho-empty-row">
                                        {loading ? "Đang tải..." : "Không có cơ hội phù hợp"}
                                    </td>
                                </tr>
                            ) : filtered.map((p) => (
                                <tr key={p.id}>
                                    <td><span className="coho-id-badge">#{p.id}</span></td>
                                    <td>
                                        <span
                                            style={{ fontWeight: 600, color: "#111827", cursor: "pointer" }}
                                            onClick={() => setDetailItem(p)}
                                            title="Xem chi tiết"
                                        >
                                            {p.tenThuongVu}
                                        </span>
                                        {p.ghiChu && (
                                            <p style={{ fontSize: 11, color: "#9ca3af", margin: "2px 0 0", lineHeight: 1.4 }}>
                                                {p.ghiChu.length > 48 ? p.ghiChu.slice(0, 48) + "…" : p.ghiChu}
                                            </p>
                                        )}
                                    </td>
                                    <td><GiaiDoanBadge value={p.giaiDoan} /></td>
                                    <td style={{ minWidth: 110 }}><TyLeBar value={p.tyLeThanhCong} /></td>
                                    <td className="right coho-price-cell">{formatVND(p.doanhThuKyVong)}</td>
                                    <td style={{ color: "#6b7280", fontSize: 12 }}>
                                        {p.ngayDuKien ? new Date(p.ngayDuKien).toLocaleDateString("vi-VN") : "—"}
                                    </td>
                                    <td>
                                        {/* ✅ Hiển thị tên thay vì ID */}
                                        {getTenNV(p)
                                            ? <span className="coho-nv-badge">{getTenNV(p)}</span>
                                            : <span style={{ color: "#d1d5db" }}>—</span>
                                        }
                                    </td>
                                    <td>
                                        <div className="coho-row-actions">
                                            <button className="coho-ghost-btn btn-icon" type="button" onClick={() => setDetailItem(p)}>
                                                <ActionIcon name="search" /> Chi tiết
                                            </button>
                                            {canWriteCoHoi ? (
                                                <>
                                            <button className="coho-ghost-btn btn-icon" type="button" onClick={() => openEdit(p)}>
                                                <ActionIcon name="edit" /> Sửa
                                            </button>
                                            <button className="coho-danger-btn btn-icon" type="button" onClick={() => setDeleteConfirm(p)}>
                                                <ActionIcon name="delete" /> X?a
                                            </button>
                                                </>
                                            ) : null}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>

            {/* ── Delete confirm ── */}
            {canWriteCoHoi && deleteConfirm && (
                <div className="coho-overlay">
                    <div className="coho-modal coho-modal-sm">
                        <div className="coho-modal-header">
                            <h3>Xác nhận xóa</h3>
                            <button className="coho-modal-close" onClick={() => setDeleteConfirm(null)}>×</button>
                        </div>
                        <div className="coho-modal-body">
                            <p>
                                Bạn có chắc muốn xóa cơ hội{" "}
                                <strong style={{ color: "#111827" }}>{deleteConfirm.tenThuongVu}</strong>?
                                Hành động này không thể hoàn tác.
                            </p>
                        </div>
                        <div className="coho-modal-footer">
                            <button className="coho-secondary-btn btn-icon" onClick={() => setDeleteConfirm(null)}><ActionIcon name="close" /> Hủy</button>
                            <button className="coho-delete-confirm-btn btn-icon" onClick={() => handleDelete(deleteConfirm)}>
                                <ActionIcon name="delete" /> Xóa cơ hội
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* ── Detail modal ── */}
            {detailItem && (
                <DetailModal
                    item={detailItem}
                    onClose={() => setDetailItem(null)}
                    onEdit={openEdit}
                    nhanViens={nhanViens}
                    leads={leads}
                    khachHangs={khachHangs}
                    canWrite={canWriteCoHoi}
                />
            )}
        </main>
    );
}
