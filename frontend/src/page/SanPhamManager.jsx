import { useState, useEffect, useRef, useMemo, useCallback } from "react";
import axios from "axios";
import "./SanPham.css";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";
const ax = axios.create({ baseURL: API_BASE_URL });

const formatVND = (n) =>
    new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(n);

const EMPTY_FORM = {
  maSanPham: "", tenSanPham: "", donVi: "",
  giaBan: "", slTon: "", trangThai: 1, loaiSanPham: "",
};

/* ─────────────────────────────────────────────
   Lightbox component
───────────────────────────────────────────── */
function Lightbox({ images, startIndex, baseUrl, onClose }) {
  const [idx, setIdx] = useState(startIndex ?? 0);
  const img = images[idx];

  useEffect(() => {
    const handler = (e) => {
      if (e.key === "Escape")      onClose();
      if (e.key === "ArrowRight")  setIdx((i) => Math.min(i + 1, images.length - 1));
      if (e.key === "ArrowLeft")   setIdx((i) => Math.max(i - 1, 0));
    };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [images.length, onClose]);

  return (
      <div className="lightbox-overlay" onClick={onClose}>
        <button className="lightbox-close" onClick={onClose}>×</button>
        <img
            className="lightbox-img"
            src={`${baseUrl}${img.url}`}
            alt={`Ảnh ${idx + 1}`}
            onClick={(e) => e.stopPropagation()}
        />
        <div className="lightbox-nav" onClick={(e) => e.stopPropagation()}>
          <button className="lightbox-nav-btn" disabled={idx === 0}
                  onClick={() => setIdx((i) => i - 1)}>‹</button>
          <span className="lightbox-counter">{idx + 1} / {images.length}</span>
          <button className="lightbox-nav-btn" disabled={idx === images.length - 1}
                  onClick={() => setIdx((i) => i + 1)}>›</button>
        </div>
      </div>
  );
}

/* ─────────────────────────────────────────────
   ImageModal component
───────────────────────────────────────────── */
function ImageModal({ product, baseUrl, onClose, onRefresh, setGlobalMsg }) {
  const [uploading, setUploading]       = useState(false);
  const [dragOver, setDragOver]         = useState(false);
  const [previews, setPreviews]         = useState([]);   // { file, objectUrl }
  const [deleteTarget, setDeleteTarget] = useState(null); // imgId to confirm
  const [lightboxIdx, setLightboxIdx]   = useState(null);
  const fileRef = useRef();

  const images = product.hinhAnh ?? [];

  /* Preview selected files */
  const addFiles = (fileList) => {
    const files = Array.from(fileList).filter((f) => f.type.startsWith("image/"));
    const items = files.map((f) => ({ file: f, objectUrl: URL.createObjectURL(f) }));
    setPreviews((prev) => [...prev, ...items]);
  };

  const removePreview = (idx) => {
    setPreviews((prev) => {
      URL.revokeObjectURL(prev[idx].objectUrl);
      return prev.filter((_, i) => i !== idx);
    });
  };

  /* Upload all queued files */
  const handleUpload = async () => {
    if (previews.length === 0 || uploading) return;
    setUploading(true);
    try {
      for (let i = 0; i < previews.length; i++) {
        const fd = new FormData();
        fd.append("file", previews[i].file);
        fd.append("isMain", images.length === 0 && i === 0 ? 1 : 0);
        await ax.post(`/api/sanpham/${product.sanPhamId}/hinhanh`, fd, {
          headers: { "Content-Type": "multipart/form-data" },
        });
      }
      previews.forEach((p) => URL.revokeObjectURL(p.objectUrl));
      setPreviews([]);
      await onRefresh(product.sanPhamId);
      setGlobalMsg({ type: "success", text: `Đã upload ${previews.length} ảnh thành công` });
    } catch {
      setGlobalMsg({ type: "error", text: "Upload ảnh thất bại." });
    } finally {
      setUploading(false);
      if (fileRef.current) fileRef.current.value = "";
    }
  };

  /* Set main image */
  const handleSetMain = async (imgId) => {
    try {
      await ax.patch(`/api/sanpham/${product.sanPhamId}/hinhanh/${imgId}/setMain`);
      await onRefresh(product.sanPhamId);
    } catch {
      setGlobalMsg({ type: "error", text: "Không thể đặt ảnh chính." });
    }
  };

  /* Delete image */
  const handleDeleteImage = async (imgId) => {
    try {
      await ax.delete(`/api/sanpham/${product.sanPhamId}/hinhanh/${imgId}`);
      setDeleteTarget(null);
      await onRefresh(product.sanPhamId);
      setGlobalMsg({ type: "success", text: "Đã xóa ảnh" });
    } catch {
      setGlobalMsg({ type: "error", text: "Xóa ảnh thất bại." });
    }
  };

  return (
      <>
        <div className="overlay">
          <div className="modal modal-img" onClick={(e) => e.stopPropagation()}>

            {/* Header */}
            <div className="modal-header">
              <div>
                <h3>Hình ảnh sản phẩm</h3>
                <p>
                  {product.tenSanPham}
                  <span style={{ marginLeft: 8, fontWeight: 600, color: "#2563eb" }}>
                  {images.length} ảnh
                </span>
                </p>
              </div>
              <button className="modal-close" type="button" onClick={onClose}>×</button>
            </div>

            <div className="img-modal-body">
              {/* Gallery */}
              {images.length > 0 ? (
                  <div className="img-gallery">
                    {images.map((img, i) => (
                        <div
                            key={img.id}
                            className={`img-card ${img.isMain === 1 ? "is-main" : ""}`}
                        >
                          {/* Thumbnail */}
                          <img
                              className="img-card-thumb"
                              src={`${baseUrl}${img.url}`}
                              alt={`Ảnh ${i + 1}`}
                              onClick={() => setLightboxIdx(i)}
                              onError={(e) => { e.target.style.display = "none"; }}
                          />

                          {/* Badges */}
                          {img.isMain === 1 && <span className="img-main-badge">✓ Chính</span>}
                          <span className="img-idx-badge">{i + 1}</span>

                          {/* Hover actions */}
                          <div className="img-card-actions">
                            {img.isMain !== 1 && (
                                <button
                                    className="img-action-btn img-action-set-main"
                                    onClick={() => handleSetMain(img.id)}
                                >
                                  Đặt chính
                                </button>
                            )}
                            <button
                                className="img-action-btn img-action-delete"
                                onClick={(e) => { e.stopPropagation(); setDeleteTarget(img.id); }}
                            >
                              Xóa
                            </button>
                          </div>

                          {/* Inline delete confirm */}
                          {deleteTarget === img.id && (
                              <div className="img-delete-confirm" onClick={(e) => e.stopPropagation()}>
                                <p>Xóa ảnh này?</p>
                                <div className="img-delete-confirm-btns">
                                  <button className="img-confirm-yes" onClick={() => handleDeleteImage(img.id)}>Xóa</button>
                                  <button className="img-confirm-no"  onClick={() => setDeleteTarget(null)}>Hủy</button>
                                </div>
                              </div>
                          )}
                        </div>
                    ))}
                  </div>
              ) : (
                  <div className="img-empty">
                    <span className="img-empty-icon">🖼️</span>
                    <p className="img-empty-text">Sản phẩm chưa có hình ảnh nào.<br />Thêm ảnh bên dưới để hiển thị.</p>
                  </div>
              )}

              {/* Queue previews */}
              {previews.length > 0 && (
                  <div className="img-preview-strip">
                    {previews.map((p, i) => (
                        <div key={i} className="img-preview-item">
                          <img src={p.objectUrl} alt="" />
                          <button className="img-preview-remove" onClick={() => removePreview(i)}>×</button>
                        </div>
                    ))}
                  </div>
              )}

              {/* Upload progress bar */}
              {uploading && (
                  <div className="img-upload-progress">
                    <span>Đang upload...</span>
                    <div className="img-progress-bar-wrap">
                      <div className="img-progress-bar" />
                    </div>
                  </div>
              )}

              {/* Upload zone */}
              <div className="img-upload-zone">
                <span className="img-upload-label">Thêm ảnh mới</span>
                <div
                    className={`img-drop-area ${dragOver ? "drag-over" : ""}`}
                    onDragOver={(e) => { e.preventDefault(); setDragOver(true); }}
                    onDragLeave={() => setDragOver(false)}
                    onDrop={(e) => { e.preventDefault(); setDragOver(false); addFiles(e.dataTransfer.files); }}
                >
                  <input
                      ref={fileRef}
                      type="file"
                      accept="image/*"
                      multiple
                      onChange={(e) => addFiles(e.target.files)}
                  />
                  <span className="img-drop-icon">📁</span>
                  <p className="img-drop-text">Kéo thả ảnh vào đây hoặc <strong>chọn file</strong></p>
                  <p className="img-drop-hint">JPG, PNG, WEBP — nhiều ảnh cùng lúc</p>
                </div>
              </div>

              {/* Footer actions */}
              <div className="img-upload-footer">
              <span style={{ fontSize: 12, color: "#9ca3af" }}>
                {previews.length > 0
                    ? `${previews.length} ảnh đã chọn, chưa upload`
                    : "Chọn ảnh để bắt đầu upload"}
              </span>
                <div style={{ display: "flex", gap: 8 }}>
                  <button className="secondary-btn" type="button" onClick={onClose}>Đóng</button>
                  <button
                      className="primary-btn"
                      type="button"
                      disabled={previews.length === 0 || uploading}
                      onClick={handleUpload}
                  >
                    {uploading ? "Đang upload..." : `Upload ${previews.length > 0 ? `(${previews.length})` : ""}`}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Lightbox */}
        {lightboxIdx !== null && images.length > 0 && (
            <Lightbox
                images={images}
                startIndex={lightboxIdx}
                baseUrl={baseUrl}
                onClose={() => setLightboxIdx(null)}
            />
        )}
      </>
  );
}
/* ─────────────────────────────────────────────
   ProductDetailModal component
───────────────────────────────────────────── */
function ProductDetailModal({ product, categories, baseUrl, onClose, onEdit, onManageImages }) {
  const [activeIdx, setActiveIdx] = useState(0);
  const images = product.hinhAnh ?? [];
  const mainImg = images[activeIdx];

  const catName = (id) =>
      categories.find((c) => String(c.id) === String(id))?.tenLoai ?? `Loại ${id}`;

  const formatVND = (n) =>
      new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(n);

  useEffect(() => {
    const handler = (e) => {
      if (e.key === "Escape") onClose();
      if (e.key === "ArrowRight") setActiveIdx((i) => Math.min(i + 1, images.length - 1));
      if (e.key === "ArrowLeft")  setActiveIdx((i) => Math.max(i - 1, 0));
    };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [images.length, onClose]);

  return (
      <div className="overlay" onClick={onClose}>
        <div className="modal modal-detail" onClick={(e) => e.stopPropagation()}>

          {/* Header */}
          <div className="modal-header">
            <div>
              <p style={{ fontSize: 11, color: "#6b7280", margin: "0 0 2px", textTransform: "uppercase", letterSpacing: "0.05em" }}>
                Chi tiết sản phẩm
              </p>
              <h3 style={{ margin: 0 }}>{product.tenSanPham}</h3>
            </div>
            <button className="modal-close" type="button" onClick={onClose}>×</button>
          </div>

          <div className="detail-body">

            {/* ── Left: Image area ── */}
            <div className="detail-image-col">

              {/* Main image */}
              <div className="detail-main-img-wrap">
                {mainImg ? (
                    <img
                        className="detail-main-img"
                        src={`${baseUrl}${mainImg.url}`}
                        alt={product.tenSanPham}
                        onError={(e) => { e.target.style.display = "none"; }}
                    />
                ) : (
                    <div className="detail-no-img">
                      <span>🖼️</span>
                      <p>Chưa có hình ảnh</p>
                    </div>
                )}

                {/* Nav arrows */}
                {images.length > 1 && (
                    <>
                      <button
                          className="detail-img-arrow detail-img-arrow-left"
                          disabled={activeIdx === 0}
                          onClick={() => setActiveIdx((i) => i - 1)}
                      >‹</button>
                      <button
                          className="detail-img-arrow detail-img-arrow-right"
                          disabled={activeIdx === images.length - 1}
                          onClick={() => setActiveIdx((i) => i + 1)}
                      >›</button>
                      <span className="detail-img-counter">{activeIdx + 1} / {images.length}</span>
                    </>
                )}

                {/* Main badge */}
                {mainImg?.isMain === 1 && (
                    <span className="detail-main-badge">✓ Ảnh chính</span>
                )}
              </div>

              {/* Thumbnail strip */}
              {images.length > 1 && (
                  <div className="detail-thumb-row">
                    {images.map((img, i) => (
                        <button
                            key={img.id}
                            className={`detail-thumb-btn ${i === activeIdx ? "active" : ""}`}
                            onClick={() => setActiveIdx(i)}
                        >
                          <img
                              src={`${baseUrl}${img.url}`}
                              alt={`Ảnh ${i + 1}`}
                              onError={(e) => { e.target.style.display = "none"; }}
                          />
                        </button>
                    ))}
                  </div>
              )}

              {/* Image count */}
              <p style={{ textAlign: "center", fontSize: 12, color: "#9ca3af", margin: "8px 0 0" }}>
                {images.length > 0 ? `${images.length} hình ảnh` : "Chưa có hình ảnh nào"}
              </p>
            </div>

            {/* ── Right: Info area ── */}
            <div className="detail-info-col">

              {/* Badges */}
              <div style={{ display: "flex", gap: 8, flexWrap: "wrap", marginBottom: 16 }}>
                <span className="badge badge-cat">{catName(product.loaiSanPham)}</span>
                <span className={`badge ${product.trangThai === 1 ? "badge-active" : "badge-inactive"}`}>
                {product.trangThai === 1 ? "Đang bán" : "Ngừng bán"}
              </span>
                {product.slTon < 10 && (
                    <span className="badge" style={{ background: "#fef9c3", color: "#854d0e", border: "1px solid #fde047" }}>
                  ⚠ Tồn kho thấp
                </span>
                )}
              </div>

              {/* Info table */}
              <div className="detail-info-table">
                <div className="detail-info-row">
                  <span className="detail-info-label">Mã sản phẩm</span>
                  <span className="product-code">{product.maSanPham}</span>
                </div>
                <div className="detail-info-row">
                  <span className="detail-info-label">Loại sản phẩm</span>
                  <span>{catName(product.loaiSanPham)}</span>
                </div>
                <div className="detail-info-row">
                  <span className="detail-info-label">Đơn vị tính</span>
                  <span>{product.donVi || "—"}</span>
                </div>
                <div className="detail-info-row">
                  <span className="detail-info-label">Số lượng tồn</span>
                  <span className={product.slTon < 10 ? "stock-low" : "stock-ok"} style={{ fontWeight: 600 }}>
                  {product.slTon}
                </span>
                </div>
                <div className="detail-info-row">
                  <span className="detail-info-label">Trạng thái</span>
                  <span className={`badge ${product.trangThai === 1 ? "badge-active" : "badge-inactive"}`}>
                  {product.trangThai === 1 ? "Đang bán" : "Ngừng bán"}
                </span>
                </div>
              </div>

              {/* Price highlight */}
              <div className="detail-price-block">
                <span className="detail-price-label">Giá bán</span>
                <span className="detail-price-value">{formatVND(product.giaBan)}</span>
              </div>

              {/* Actions */}
              <div className="detail-actions">
                <button
                    className="primary-btn"
                    type="button"
                    onClick={() => { onEdit(product); onClose(); }}
                >
                  ✏️ Chỉnh sửa
                </button>
                <button
                    className="secondary-btn"
                    type="button"
                    onClick={() => { onManageImages(product); onClose(); }}
                >
                  🖼 Quản lý ảnh
                </button>
                <button className="ghost-btn" type="button" onClick={onClose}>
                  Đóng
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
}
/* ─────────────────────────────────────────────
   Main component
───────────────────────────────────────────── */
export default function SanPhamManager() {
  const [products, setProducts]           = useState([]);
  const [categories, setCategories]       = useState([]);
  const [loading, setLoading]             = useState(true);
  const [search, setSearch]               = useState("");
  const [filterCat, setFilterCat]         = useState("");
  const [filterStatus, setFilterStatus]   = useState("");
  const [form, setForm]                   = useState(EMPTY_FORM);
  const [editId, setEditId]               = useState(null);
  const [saving, setSaving]               = useState(false);
  const [message, setMessage]             = useState({ type: "", text: "" });
  const [imgProduct, setImgProduct]       = useState(null);
  const [deleteConfirm, setDeleteConfirm] = useState(null);
  const [detailProduct, setDetailProduct] = useState(null);
  /* ── fetch ── */
  const fetchAll = async () => {
    try {
      setLoading(true);
      const [pRes, cRes] = await Promise.all([
        ax.get("/api/sanpham"),
        ax.get("/api/loaisanpham"),
      ]);
      setProducts(pRes.data);
      setCategories(cRes.data);
    } catch {
      setMessage({ type: "error", text: "Không thể tải dữ liệu. Kiểm tra kết nối backend." });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAll(); }, []);

  /* Refresh a single product's images in state */
  const refreshProduct = useCallback(async (sanPhamId) => {
    const res = await ax.get("/api/sanpham");
    setProducts(res.data);
    setImgProduct((prev) => prev ? res.data.find((p) => p.sanPhamId === sanPhamId) ?? prev : null);
  }, []);

  /* ── derived ── */
  const filtered = useMemo(() => products.filter((p) => {
    const kw = search.trim().toLowerCase();
    const matchSearch = !kw || p.tenSanPham?.toLowerCase().includes(kw) || p.maSanPham?.toLowerCase().includes(kw);
    const matchCat    = !filterCat    || String(p.loaiSanPham) === filterCat;
    const matchStatus = filterStatus === "" || String(p.trangThai) === filterStatus;
    return matchSearch && matchCat && matchStatus;
  }), [products, search, filterCat, filterStatus]);

  const stats = useMemo(() => ({
    total:    products.length,
    active:   products.filter((p) => p.trangThai === 1).length,
    lowStock: products.filter((p) => p.slTon < 10).length,
    cats:     categories.length,
  }), [products, categories]);

  const catName = (id) => categories.find((c) => String(c.id) === String(id))?.tenLoai ?? `Loại ${id}`;

  /* ── form ── */
  const resetForm = () => { setForm(EMPTY_FORM); setEditId(null); setMessage({ type: "", text: "" }); };

  const openEdit = (p) => {
    setForm({ maSanPham: p.maSanPham, tenSanPham: p.tenSanPham, donVi: p.donVi,
      giaBan: p.giaBan, slTon: p.slTon, trangThai: p.trangThai, loaiSanPham: p.loaiSanPham });
    setEditId(p.sanPhamId);
    setMessage({ type: "", text: "" });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const validate = () => {
    if (!form.maSanPham.trim()) return "Mã sản phẩm không được rỗng";
    if (!form.tenSanPham.trim()) return "Tên sản phẩm không được rỗng";
    if (!form.loaiSanPham)       return "Vui lòng chọn loại sản phẩm";
    return "";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const err = validate();
    if (err) { setMessage({ type: "error", text: err }); return; }
    setSaving(true);
    try {
      const payload = { ...form, giaBan: Number(form.giaBan), slTon: Number(form.slTon) };
      editId ? await ax.put(`/api/sanpham/${editId}`, payload) : await ax.post("/api/sanpham", payload);
      await fetchAll();
      resetForm();
      setMessage({ type: "success", text: editId ? "Cập nhật thành công" : "Thêm sản phẩm thành công" });
    } catch {
      setMessage({ type: "error", text: "Lưu thất bại. Kiểm tra lại dữ liệu." });
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (p) => {
    try {
      await ax.delete(`/api/sanpham/${p.sanPhamId}`);
      setDeleteConfirm(null);
      await fetchAll();
      setMessage({ type: "success", text: "Xóa sản phẩm thành công" });
    } catch {
      setMessage({ type: "error", text: "Xóa thất bại." });
    }
  };

  /* thumbnail helper — show up to 3 mini images */
  const renderThumbs = (p) => {
    const imgs = p.hinhAnh ?? [];
    if (imgs.length === 0) return <span className="thumb-empty">—</span>;
    const shown = imgs.slice(0, 3);
    return (
        <div className="thumb-strip">
          {shown.map((img, i) => (
              <img
                  key={img.id}
                  className="thumb-mini"
                  src={`${API_BASE_URL}${img.url}`}
                  alt=""
                  title={`Ảnh ${i + 1}`}
                  onClick={() => setImgProduct(p)}
                  onError={(e) => { e.target.style.display = "none"; }}
              />
          ))}
          {imgs.length > 3 && (
              <span className="thumb-more" onClick={() => setImgProduct(p)}>
            +{imgs.length - 3}
          </span>
          )}
        </div>
    );
  };

  return (
      <main className="sanpham-page">

        {/* ── Header ── */}
        <section className="hopdong-header">
          <div>
            <p className="eyebrow">Danh mục / Sản phẩm</p>
            <h1>Quản lý Sản phẩm</h1>
            <p className="subtitle">Thêm, cập nhật và quản lý hình ảnh cho từng sản phẩm.</p>
          </div>
          <div className="toolbar">
            <input className="search" type="search" placeholder="Tìm theo tên, mã..." value={search} onChange={(e) => setSearch(e.target.value)} />
            <select className="search sanpham-filter" value={filterCat} onChange={(e) => setFilterCat(e.target.value)}>
              <option value="">Tất cả loại</option>
              {categories.map((c) => <option key={c.id} value={c.id}>{c.tenLoai}</option>)}
            </select>
            <select className="search sanpham-filter" value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
              <option value="">Tất cả trạng thái</option>
              <option value="1">Đang bán</option>
              <option value="0">Ngừng bán</option>
            </select>
            <button className="secondary-btn" type="button" onClick={fetchAll}>Tải lại</button>
          </div>
        </section>

        {/* ── Stats ── */}
        <section className="stats-row">
          <article className="stat-card"><span>Tổng sản phẩm</span><strong>{stats.total}</strong></article>
          <article className="stat-card"><span>Đang bán</span><strong>{stats.active}</strong></article>
          <article className="stat-card"><span>Tồn kho thấp</span><strong>{stats.lowStock}</strong></article>
          <article className="stat-card"><span>Loại sản phẩm</span><strong>{stats.cats}</strong></article>
        </section>

        {/* ── Grid ── */}
        <section className="sanpham-grid">

          {/* Form panel */}
          <div className="panel form-panel">
            <div className="panel-head">
              <div>
                <h2>{editId ? "Cập nhật sản phẩm" : "Thêm sản phẩm mới"}</h2>
                <p>Nhập thông tin chi tiết sản phẩm.</p>
              </div>
              {editId && <button className="ghost-btn" type="button" onClick={resetForm}>Hủy sửa</button>}
            </div>
            <form className="sanpham-form-inner" onSubmit={handleSubmit}>
              <div className="two-col">
                <label>Mã sản phẩm <span className="req">*</span>
                  <input name="maSanPham" value={form.maSanPham} onChange={handleChange} disabled={Boolean(editId)} placeholder="SP-0001" />
                </label>
                <label>Đơn vị
                  <input name="donVi" value={form.donVi} onChange={handleChange} placeholder="Hộp, Cái, Kg..." />
                </label>
              </div>
              <label>Tên sản phẩm <span className="req">*</span>
                <input name="tenSanPham" value={form.tenSanPham} onChange={handleChange} placeholder="Nhập tên đầy đủ sản phẩm" />
              </label>
              <div className="two-col">
                <label>Giá bán (VNĐ)
                  <input name="giaBan" type="number" min="0" value={form.giaBan} onChange={handleChange} placeholder="0" />
                </label>
                <label>Số lượng tồn
                  <input name="slTon" type="number" min="0" value={form.slTon} onChange={handleChange} placeholder="0" />
                </label>
              </div>
              <div className="two-col">
                <label>Loại sản phẩm <span className="req">*</span>
                  <select name="loaiSanPham" value={form.loaiSanPham} onChange={handleChange}>
                    <option value="">-- Chọn loại --</option>
                    {categories.map((c) => <option key={c.id} value={c.id}>{c.tenLoai}</option>)}
                  </select>
                </label>
                <label>Trạng thái
                  <select name="trangThai" value={form.trangThai} onChange={handleChange}>
                    <option value={1}>Đang bán</option>
                    <option value={0}>Ngừng bán</option>
                  </select>
                </label>
              </div>
              {message.text && <div className={`message ${message.type}`}>{message.text}</div>}
              <div className="actions">
                <button className="primary-btn" type="submit" disabled={saving}>
                  {saving ? "Đang lưu..." : editId ? "Cập nhật" : "Thêm mới"}
                </button>
                <button className="secondary-btn" type="button" onClick={resetForm}>Làm mới</button>
              </div>
            </form>
          </div>

          {/* Table panel */}
          <div className="panel table-panel">
            <div className="panel-head">
              <div>
                <h2>Danh sách sản phẩm</h2>
                <p>Hiển thị {filtered.length}/{products.length} bản ghi.</p>
              </div>
              {loading && <span className="loading">Đang tải...</span>}
            </div>
            <div className="table-wrap">
              <table>
                <thead>
                <tr>
                  <th>Mã SP</th>
                  <th>Tên sản phẩm</th>
                  <th>Loại</th>
                  <th>Đơn vị</th>
                  <th className="right">Giá bán</th>
                  <th className="right">Tồn</th>
                  <th>Trạng thái</th>
                  <th>Hình ảnh</th>
                  <th className="right">Hành động</th>
                </tr>
                </thead>
                <tbody>
                {filtered.length === 0 ? (
                    <tr><td colSpan={9} className="empty-row">{loading ? "Đang tải..." : "Không có sản phẩm phù hợp"}</td></tr>
                ) : filtered.map((p) => (
                    <tr key={p.sanPhamId}>
                      <td><span className="product-code">{p.maSanPham}</span></td>
                      <td style={{ fontWeight: 500, color: "#111827" }}>{p.tenSanPham}</td>
                      <td><span className="badge badge-cat">{catName(p.loaiSanPham)}</span></td>
                      <td style={{ color: "#6b7280" }}>{p.donVi}</td>
                      <td className="right price-cell">{formatVND(p.giaBan)}</td>
                      <td className={`right ${p.slTon < 10 ? "stock-low" : "stock-ok"}`}>{p.slTon}</td>
                      <td>
                      <span className={`badge ${p.trangThai === 1 ? "badge-active" : "badge-inactive"}`}>
                        {p.trangThai === 1 ? "Đang bán" : "Ngừng bán"}
                      </span>
                      </td>
                      <td>{renderThumbs(p)}</td>
                      <td>
                        <div className="row-actions">
                          <button className="ghost-btn" type="button" onClick={() => setDetailProduct(p)}>
                            🔍 Chi tiết
                          </button>
                          <button className="ghost-btn" type="button" onClick={() => setImgProduct(p)}>
                            🖼 Ảnh
                          </button>
                          <button className="ghost-btn"  type="button" onClick={() => openEdit(p)}>Sửa</button>
                          <button className="danger-btn" type="button" onClick={() => setDeleteConfirm(p)}>Xóa</button>
                        </div>
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
            </div>
          </div>
        </section>

        {/* ── Image modal ── */}
        {imgProduct && (
            <ImageModal
                product={imgProduct}
                baseUrl={API_BASE_URL}
                onClose={() => setImgProduct(null)}
                onRefresh={refreshProduct}
                setGlobalMsg={setMessage}
            />
        )}

        {/* ── Delete confirm ── */}
        {deleteConfirm && (
            <div className="overlay">
              <div className="modal modal-sm">
                <div className="modal-header">
                  <h3>Xác nhận xóa</h3>
                  <button className="modal-close" type="button" onClick={() => setDeleteConfirm(null)}>×</button>
                </div>
                <div className="modal-body">
                  <p style={{ margin: 0, fontSize: 14, color: "#374151", lineHeight: 1.6 }}>
                    Bạn có chắc muốn xóa sản phẩm <strong style={{ color: "#111827" }}>{deleteConfirm.tenSanPham}</strong>?
                    Hành động này không thể hoàn tác.
                  </p>
                </div>
                <div className="modal-footer">
                  <button className="secondary-btn" type="button" onClick={() => setDeleteConfirm(null)}>Hủy</button>
                  <button
                      type="button"
                      style={{ padding: "8px 18px", background: "#dc2626", color: "#fff", border: "none", borderRadius: 8, fontSize: 13, fontWeight: 600, cursor: "pointer" }}
                      onClick={() => handleDelete(deleteConfirm)}
                  >
                    Xóa sản phẩm
                  </button>
                </div>
              </div>
            </div>
        )}
        {detailProduct && (
            <ProductDetailModal
                product={detailProduct}
                categories={categories}
                baseUrl={API_BASE_URL}
                onClose={() => setDetailProduct(null)}
                onEdit={openEdit}
                onManageImages={setImgProduct}
            />
        )}
      </main>
  );
}