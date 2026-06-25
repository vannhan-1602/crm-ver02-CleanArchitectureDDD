import { useState, useEffect, useRef, useMemo, useCallback } from "react";
import { API_BASE_URL, api as ax, getCurrentUser, getPermissions } from "../apiClient";
import "./SanPham.css";
import { ActionIcon } from "../moduleIcons.jsx";

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
  const [previews, setPreviews]         = useState([]);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [lightboxIdx, setLightboxIdx]   = useState(null);
  const fileRef = useRef();

  const images = product.hinhAnh ?? [];

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

  const handleSetMain = async (imgId) => {
    try {
      await ax.patch(`/api/sanpham/${product.sanPhamId}/hinhanh/${imgId}/setMain`);
      await onRefresh(product.sanPhamId);
    } catch {
      setGlobalMsg({ type: "error", text: "Không thể đặt ảnh chính." });
    }
  };

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
            <div className="modal-header">
              <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
                <div style={{
                  width: 38, height: 38, borderRadius: 10,
                  background: "linear-gradient(135deg,#eff6ff,#dbeafe)",
                  border: "1px solid #bfdbfe",
                  display: "flex", alignItems: "center", justifyContent: "center", fontSize: 18,
                }}>🖼️</div>
                <div>
                  <h3>Hình ảnh sản phẩm</h3>
                  <p>
                    {product.tenSanPham}
                    <span style={{ marginLeft: 8, fontWeight: 600, color: "#2563eb",
                      background: "#eff6ff", padding: "1px 7px", borderRadius: 12, fontSize: 11 }}>
                      {images.length} ảnh
                    </span>
                  </p>
                </div>
              </div>
              <button className="modal-close" type="button" onClick={onClose}>×</button>
            </div>

            <div className="img-modal-body">
              {images.length > 0 ? (
                  <div className="img-gallery">
                    {images.map((img, i) => (
                        <div key={img.id} className={`img-card ${img.isMain === 1 ? "is-main" : ""}`}>
                          <img
                              className="img-card-thumb"
                              src={`${baseUrl}${img.url}`}
                              alt={`Ảnh ${i + 1}`}
                              onClick={() => setLightboxIdx(i)}
                              onError={(e) => { e.target.style.display = "none"; }}
                          />
                          {img.isMain === 1 && <span className="img-main-badge">✓ Chính</span>}
                          <span className="img-idx-badge">{i + 1}</span>
                          <div className="img-card-actions">
                            {img.isMain !== 1 && (
                                <button className="img-action-btn img-action-set-main btn-icon" onClick={() => handleSetMain(img.id)}>
                                  Đặt chính
                                </button>
                            )}
                            <button
                                className="img-action-btn img-action-delete btn-icon"
                                onClick={(e) => { e.stopPropagation(); setDeleteTarget(img.id); }}
                            ><ActionIcon name="delete" /> Xóa</button>
                          </div>
                          {deleteTarget === img.id && (
                              <div className="img-delete-confirm" onClick={(e) => e.stopPropagation()}>
                                <p>Xóa ảnh này?</p>
                                <div className="img-delete-confirm-btns">
                                  <button className="img-confirm-yes btn-icon" onClick={() => handleDeleteImage(img.id)}><ActionIcon name="delete" /> Xóa</button>
                                  <button className="img-confirm-no btn-icon" onClick={() => setDeleteTarget(null)}><ActionIcon name="close" /> Hủy</button>
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

              {uploading && (
                  <div className="img-upload-progress">
                    <span>Đang upload...</span>
                    <div className="img-progress-bar-wrap">
                      <div className="img-progress-bar" />
                    </div>
                  </div>
              )}

              <div className="img-upload-zone">
                <span className="img-upload-label">Thêm ảnh mới</span>
                <div
                    className={`img-drop-area ${dragOver ? "drag-over" : ""}`}
                    onDragOver={(e) => { e.preventDefault(); setDragOver(true); }}
                    onDragLeave={() => setDragOver(false)}
                    onDrop={(e) => { e.preventDefault(); setDragOver(false); addFiles(e.dataTransfer.files); }}
                >
                  <input ref={fileRef} type="file" accept="image/*" multiple onChange={(e) => addFiles(e.target.files)} />
                  <span className="img-drop-icon">📁</span>
                  <p className="img-drop-text">Kéo thả ảnh vào đây hoặc <strong>chọn file</strong></p>
                  <p className="img-drop-hint">JPG, PNG, WEBP — nhiều ảnh cùng lúc</p>
                </div>
              </div>

              <div className="img-upload-footer">
                <span style={{ fontSize: 12, color: "#9ca3af" }}>
                  {previews.length > 0 ? `${previews.length} ảnh đã chọn, chưa upload` : "Chọn ảnh để bắt đầu upload"}
                </span>
                <div style={{ display: "flex", gap: 8 }}>
                  <button className="secondary-btn btn-icon" type="button" onClick={onClose}><ActionIcon name="close" /> Đóng</button>
                  <button className="primary-btn btn-icon" type="button" disabled={previews.length === 0 || uploading} onClick={handleUpload}>
                      <ActionIcon name="add" />
                    {uploading ? "Đang upload..." : `Upload${previews.length > 0 ? ` (${previews.length})` : ""}`}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        {lightboxIdx !== null && images.length > 0 && (
            <Lightbox images={images} startIndex={lightboxIdx} baseUrl={baseUrl} onClose={() => setLightboxIdx(null)} />
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

  const catName = (id) => categories.find((c) => String(c.id) === String(id))?.tenLoai ?? `Loại ${id}`;

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
          <div className="modal-header">
            <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
              <div style={{
                width: 40, height: 40, borderRadius: 10,
                background: "linear-gradient(135deg,#eff6ff,#dbeafe)",
                border: "1px solid #bfdbfe",
                display: "flex", alignItems: "center", justifyContent: "center", fontSize: 20,
              }}>📦</div>
              <div>
                <p style={{ fontSize: 11, color: "#2563eb", margin: "0 0 2px",
                  textTransform: "uppercase", letterSpacing: "0.07em", fontWeight: 600 }}>
                  Chi tiết sản phẩm
                </p>
                <h3 style={{ margin: 0 }}>{product.tenSanPham}</h3>
              </div>
            </div>
            <button className="modal-close" type="button" onClick={onClose}>×</button>
          </div>

          <div className="detail-body">
            {/* Left: images */}
            <div className="detail-image-col">
              <div className="detail-main-img-wrap">
                {mainImg ? (
                    <img className="detail-main-img" src={`${baseUrl}${mainImg.url}`} alt={product.tenSanPham}
                         onError={(e) => { e.target.style.display = "none"; }} />
                ) : (
                    <div className="detail-no-img">
                      <span>🖼️</span>
                      <p>Chưa có hình ảnh</p>
                    </div>
                )}
                {images.length > 1 && (
                    <>
                      <button className="detail-img-arrow detail-img-arrow-left" disabled={activeIdx === 0}
                              onClick={() => setActiveIdx((i) => i - 1)}>‹</button>
                      <button className="detail-img-arrow detail-img-arrow-right" disabled={activeIdx === images.length - 1}
                              onClick={() => setActiveIdx((i) => i + 1)}>›</button>
                      <span className="detail-img-counter">{activeIdx + 1} / {images.length}</span>
                    </>
                )}
                {mainImg?.isMain === 1 && <span className="detail-main-badge">✓ Ảnh chính</span>}
              </div>

              {images.length > 1 && (
                  <div className="detail-thumb-row">
                    {images.map((img, i) => (
                        <button key={img.id} className={`detail-thumb-btn ${i === activeIdx ? "active" : ""}`}
                                onClick={() => setActiveIdx(i)}>
                          <img src={`${baseUrl}${img.url}`} alt={`Ảnh ${i + 1}`}
                               onError={(e) => { e.target.style.display = "none"; }} />
                        </button>
                    ))}
                  </div>
              )}

              <p style={{ textAlign: "center", fontSize: 12, color: "#9ca3af", margin: "10px 0 0" }}>
                {images.length > 0 ? `${images.length} hình ảnh` : "Chưa có hình ảnh nào"}
              </p>
            </div>

            {/* Right: info */}
            <div className="detail-info-col">
              <div style={{ display: "flex", gap: 8, flexWrap: "wrap", marginBottom: 18 }}>
                <span className="badge badge-cat">{catName(product.loaiSanPham)}</span>
                <span className={`badge ${product.trangThai === 1 ? "badge-active" : "badge-inactive"}`}>
                  {product.trangThai === 1 ? "Đang bán" : "Ngừng bán"}
                </span>
                {product.slTon < 10 && (
                    <span className="badge" style={{ background: "#fefce8", color: "#92400e", border: "1px solid #fde68a" }}>
                      ⚠ Tồn kho thấp
                    </span>
                )}
              </div>

              <div className="detail-info-table">
                {[
                  { label: "Mã sản phẩm", value: <span className="product-code">{product.maSanPham}</span> },
                  { label: "Loại sản phẩm", value: catName(product.loaiSanPham) },
                  { label: "Đơn vị tính", value: product.donVi || "—" },
                  {
                    label: "Số lượng tồn",
                    value: <span className={product.slTon < 10 ? "stock-low" : "stock-ok"} style={{ fontWeight: 700 }}>{product.slTon}</span>
                  },
                  {
                    label: "Trạng thái",
                    value: <span className={`badge ${product.trangThai === 1 ? "badge-active" : "badge-inactive"}`}>
                      {product.trangThai === 1 ? "Đang bán" : "Ngừng bán"}
                    </span>
                  },
                ].map(({ label, value }) => (
                    <div key={label} className="detail-info-row">
                      <span className="detail-info-label">{label}</span>
                      <span>{value}</span>
                    </div>
                ))}
              </div>

              <div className="detail-price-block">
                <div>
                  <div style={{ fontSize: 12, color: "#3b82f6", fontWeight: 500, marginBottom: 4 }}>Giá bán</div>
                  <div className="detail-price-value">{formatVND(product.giaBan)}</div>
                </div>
                <div style={{ fontSize: 32, opacity: 0.25 }}>💰</div>
              </div>

              <div className="detail-actions">
                {onEdit && (
                    <button className="primary-btn btn-icon" type="button" onClick={() => { onEdit(product); onClose(); }}>
                      <ActionIcon name="edit" /> Chỉnh sửa
                    </button>
                )}
                {onManageImages && (
                    <button className="secondary-btn btn-icon" type="button" onClick={() => { onManageImages(product); onClose(); }}>
                      <ActionIcon name="image" /> Quản lý ảnh
                    </button>
                )}
                <button className="ghost-btn btn-icon" type="button" onClick={onClose}><ActionIcon name="close" /> Đóng</button>
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
  const currentUser = getCurrentUser();
  const userPermissions = getPermissions();
  const isAdmin = Boolean(currentUser?.admin || currentUser?.roleId === 1 || currentUser?.roleName?.toLowerCase() === "admin");
  const productPermission = userPermissions.find((item) => item.moduleKey === "SAN_PHAM");
  const canWriteProducts = isAdmin || Boolean(productPermission?.canWrite);

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

  const refreshProduct = useCallback(async (sanPhamId) => {
    const res = await ax.get("/api/sanpham");
    setProducts(res.data);
    setImgProduct((prev) => prev ? res.data.find((p) => p.sanPhamId === sanPhamId) ?? prev : null);
  }, []);

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

  const resetForm = () => { setForm(EMPTY_FORM); setEditId(null); setMessage({ type: "", text: "" }); };

  const openEdit = (p) => {
    if (!canWriteProducts) return;
    setForm({
      maSanPham: p.maSanPham, tenSanPham: p.tenSanPham, donVi: p.donVi,
      giaBan: p.giaBan, slTon: p.slTon, trangThai: p.trangThai, loaiSanPham: p.loaiSanPham,
    });
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
    if (!canWriteProducts) { setMessage({ type: "error", text: "Bạn không có quyền ghi sản phẩm." }); return; }
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
    if (!canWriteProducts) return;
    try {
      await ax.delete(`/api/sanpham/${p.sanPhamId}`);
      setDeleteConfirm(null);
      await fetchAll();
      setMessage({ type: "success", text: "Xóa sản phẩm thành công" });
    } catch {
      setMessage({ type: "error", text: "Xóa thất bại." });
    }
  };

  const openImageManager = (p) => { if (canWriteProducts) setImgProduct(p); };

  const renderThumbs = (p) => {
    const imgs = p.hinhAnh ?? [];
    if (imgs.length === 0) return <span className="thumb-empty">—</span>;
    const shown = imgs.slice(0, 3);
    return (
        <div className="thumb-strip">
          {shown.map((img, i) => (
              <img key={img.id} className="thumb-mini" src={`${API_BASE_URL}${img.url}`} alt=""
                   title={`Ảnh ${i + 1}`} onClick={() => openImageManager(p)}
                   style={!canWriteProducts ? { cursor: "default" } : undefined}
                   onError={(e) => { e.target.style.display = "none"; }} />
          ))}
          {imgs.length > 3 && (
              <span className="thumb-more" onClick={() => openImageManager(p)}
                    style={!canWriteProducts ? { cursor: "default" } : undefined}>
                +{imgs.length - 3}
              </span>
          )}
        </div>
    );
  };

  /* Stat card config */
  const STAT_CARDS = [
    { label: "Tổng sản phẩm", value: stats.total,    icon: "📦", color: "#2563eb", bg: "#eff6ff", border: "#dbeafe" },
    { label: "Đang bán",      value: stats.active,   icon: "✅", color: "#16a34a", bg: "#f0fdf4", border: "#bbf7d0" },
    { label: "Tồn kho thấp",  value: stats.lowStock, icon: "⚠️", color: "#dc2626", bg: "#fef2f2", border: "#fecaca" },
    { label: "Loại sản phẩm", value: stats.cats,     icon: "🏷️", color: "#7c3aed", bg: "#faf5ff", border: "#e9d5ff" },
  ];

  return (
      <main className="sanpham-page">

        {/* ── Header ── */}
        <section className="hopdong-header">
          <div>
            <p className="eyebrow">Danh mục / Sản phẩm</p>
            <h1>Quản lý Sản phẩm</h1>
            <p className="subtitle">
              {canWriteProducts ? "Thêm, cập nhật và quản lý hình ảnh cho từng sản phẩm." : "Xem danh sách sản phẩm."}
            </p>
          </div>
          <div className="toolbar">
            <div style={{ position: "relative" }}>
              <span style={{ position: "absolute", left: 11, top: "50%", transform: "translateY(-50%)", color: "#9ca3af", pointerEvents: "none" }}><ActionIcon name="search" size={15} /></span>
              <input className="search" type="search" placeholder="Tìm theo tên, mã..."
                     value={search} onChange={(e) => setSearch(e.target.value)}
                     style={{ paddingLeft: 34 }} />
            </div>
            <select className="search sanpham-filter" value={filterCat} onChange={(e) => setFilterCat(e.target.value)}>
              <option value="">Tất cả loại</option>
              {categories.map((c) => <option key={c.id} value={c.id}>{c.tenLoai}</option>)}
            </select>
            <select className="search sanpham-filter" value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
              <option value="">Tất cả trạng thái</option>
              <option value="1">Đang bán</option>
              <option value="0">Ngừng bán</option>
            </select>
            <button className="secondary-btn btn-icon" type="button" onClick={fetchAll}
                    style={{ display: "flex", alignItems: "center", gap: 6 }}>
              <ActionIcon name="refresh" /> Tải lại
            </button>
          </div>
        </section>

        {/* ── Stats ── */}
        {canWriteProducts && (
            <section className="stats-row">
              {STAT_CARDS.map(({ label, value, icon, color, bg, border }) => (
                  <article key={label} className="stat-card" style={{ borderTop: `3px solid ${color}` }}>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
                      <span style={{ fontSize: 12, color: "#6b7280", fontWeight: 600, textTransform: "uppercase", letterSpacing: "0.05em" }}>
                        {label}
                      </span>
                      <div style={{
                        width: 34, height: 34, borderRadius: 8,
                        background: bg, border: `1px solid ${border}`,
                        display: "flex", alignItems: "center", justifyContent: "center", fontSize: 16,
                      }}>{icon}</div>
                    </div>
                    <strong style={{ fontSize: 30, fontWeight: 800, color, lineHeight: 1, marginTop: 4 }}>{value}</strong>
                  </article>
              ))}
            </section>
        )}

        {/* ── Grid ── */}
        <section className={`sanpham-grid ${canWriteProducts ? "" : "read-only"}`}>

          {/* Form panel */}
          {canWriteProducts && (
              <div className="panel form-panel">
                <div className={`panel-head form-panel-head ${editId ? "is-edit" : ""}`}>
                  <div className="form-title-wrap">
                    <div className="form-title-icon" aria-hidden="true">{editId ? "✎" : "+"}</div>
                    <div>
                      <span className="form-mode-badge">{editId ? "Đang chỉnh sửa" : "Tạo mới"}</span>
                      <h2>{editId ? "Cập nhật sản phẩm" : "Thêm sản phẩm mới"}</h2>
                      <p>{editId ? "Điều chỉnh thông tin bán hàng và tồn kho." : "Nhập thông tin cơ bản để đưa sản phẩm vào danh mục."}</p>
                    </div>
                  </div>
                  {editId && (
                      <button className="ghost-btn form-cancel-btn btn-icon" type="button" onClick={resetForm}><ActionIcon name="close" /> Hủy sửa</button>
                  )}
                </div>

                <form className="sanpham-form-inner" onSubmit={handleSubmit}>
                  <div className="form-section">
                    <div className="section-title">Thông tin sản phẩm</div>
                    <label className="field span-2">Tên sản phẩm <span className="req">*</span>
                      <input name="tenSanPham" value={form.tenSanPham} onChange={handleChange} placeholder="Nhập tên đầy đủ sản phẩm" />
                    </label>
                    <div className="two-col">
                      <label className="field">Mã sản phẩm <span className="req">*</span>
                        <input name="maSanPham" value={form.maSanPham} onChange={handleChange}
                               disabled={Boolean(editId)} placeholder="SP-0001" />
                      </label>
                      <label className="field">Đơn vị
                        <input name="donVi" value={form.donVi} onChange={handleChange} placeholder="Hộp, Cái, Kg..." />
                      </label>
                    </div>
                    <div className="two-col">
                      <label className="field">Loại sản phẩm <span className="req">*</span>
                        <select name="loaiSanPham" value={form.loaiSanPham} onChange={handleChange}>
                          <option value="">-- Chọn loại --</option>
                          {categories.map((c) => <option key={c.id} value={c.id}>{c.tenLoai}</option>)}
                        </select>
                      </label>
                      <label className="field">Trạng thái
                        <select name="trangThai" value={form.trangThai} onChange={handleChange}>
                          <option value={1}>Đang bán</option>
                          <option value={0}>Ngừng bán</option>
                        </select>
                      </label>
                    </div>
                  </div>

                  <div className="form-section">
                    <div className="section-title">Giá bán và tồn kho</div>
                    <div className="two-col">
                      <label className="field">Giá bán
                        <div className="input-affix">
                          <span>VNĐ</span>
                          <input name="giaBan" type="number" min="0" value={form.giaBan} onChange={handleChange} placeholder="0" />
                        </div>
                      </label>
                      <label className="field">Số lượng tồn
                        <input name="slTon" type="number" min="0" value={form.slTon} onChange={handleChange} placeholder="0" />
                      </label>
                    </div>
                  </div>

                  {message.text && (
                      <div className={`message ${message.type}`}>
                        {message.type === "success" ? "✅ " : "❌ "}{message.text}
                      </div>
                  )}

                  <div className="actions">
                    <button className="secondary-btn btn-icon" type="button" onClick={resetForm}><ActionIcon name="refresh" /> Làm mới</button>
                    <button className="primary-btn btn-icon" type="submit" disabled={saving}>
                      <ActionIcon name="save" />
                      {saving ? "Đang lưu..." : editId ? "Cập nhật sản phẩm" : "Thêm sản phẩm"}
                    </button>
                  </div>
                </form>
              </div>
          )}

          {/* Table panel */}
          <div className="panel table-panel">
            <div className="panel-head">
              <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                <div style={{
                  width: 34, height: 34, borderRadius: 8,
                  background: "linear-gradient(135deg,#f0fdf4,#dcfce7)",
                  border: "1px solid #bbf7d0",
                  display: "flex", alignItems: "center", justifyContent: "center", fontSize: 16,
                }}>📋</div>
                <div>
                  <h2>Danh sách sản phẩm</h2>
                  <p>Hiển thị <strong style={{ color: "#2563eb" }}>{filtered.length}</strong> / {products.length} bản ghi</p>
                </div>
              </div>
              {loading && (
                  <span className="loading" style={{ display: "flex", alignItems: "center", gap: 6 }}>
                    <span style={{ display: "inline-block", animation: "spin 1s linear infinite" }}>⏳</span> Đang tải...
                  </span>
              )}
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
                  <th className="right">Tồn kho</th>
                  <th>Trạng thái</th>
                  <th>Hình ảnh</th>
                  {canWriteProducts && <th className="right">Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {filtered.length === 0 ? (
                    <tr>
                      <td colSpan={canWriteProducts ? 9 : 8} className="empty-row">
                        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 8 }}>
                          <span style={{ fontSize: 32 }}>{loading ? "⏳" : "🔍"}</span>
                          <span>{loading ? "Đang tải dữ liệu..." : "Không có sản phẩm phù hợp"}</span>
                        </div>
                      </td>
                    </tr>
                ) : filtered.map((p) => (
                    <tr key={p.sanPhamId}>
                      <td><span className="product-code">{p.maSanPham}</span></td>
                      <td>
                        <div style={{ fontWeight: 600, color: "#111827" }}>{p.tenSanPham}</div>
                      </td>
                      <td><span className="badge badge-cat">{catName(p.loaiSanPham)}</span></td>
                      <td style={{ color: "#6b7280", fontSize: 12 }}>{p.donVi || "—"}</td>
                      <td className="right price-cell">{formatVND(p.giaBan)}</td>
                      <td className={`right ${p.slTon < 10 ? "stock-low" : "stock-ok"}`} style={{ fontWeight: 600 }}>
                        {p.slTon < 10 && <span style={{ marginRight: 3 }}>⚠</span>}
                        {p.slTon}
                      </td>
                      <td>
                        <span className={`badge ${p.trangThai === 1 ? "badge-active" : "badge-inactive"}`}>
                          {p.trangThai === 1 ? "● Đang bán" : "○ Ngừng bán"}
                        </span>
                      </td>
                      <td>{renderThumbs(p)}</td>
                      {canWriteProducts && (
                          <td>
                            <div className="row-actions">
                              <button className="ghost-btn btn-icon" type="button" onClick={() => setDetailProduct(p)}
                                      title="Xem chi tiết"><ActionIcon name="search" /></button>
                              <button className="ghost-btn btn-icon" type="button" onClick={() => setImgProduct(p)}
                                      title="Quản lý ảnh"><ActionIcon name="image" /></button>
                              <button className="ghost-btn btn-icon" type="button" onClick={() => openEdit(p)}
                                      title="Chỉnh sửa"><ActionIcon name="edit" /></button>
                              <button className="danger-btn btn-icon" type="button" onClick={() => setDeleteConfirm(p)}
                                      title="Xóa"><ActionIcon name="delete" /></button>
                            </div>
                          </td>
                      )}
                    </tr>
                ))}
                </tbody>
              </table>
            </div>
          </div>
        </section>

        {/* ── Image modal ── */}
        {canWriteProducts && imgProduct && (
            <ImageModal product={imgProduct} baseUrl={API_BASE_URL}
                        onClose={() => setImgProduct(null)} onRefresh={refreshProduct} setGlobalMsg={setMessage} />
        )}

        {/* ── Delete confirm ── */}
        {canWriteProducts && deleteConfirm && (
            <div className="overlay">
              <div className="modal modal-sm">
                <div className="modal-header">
                  <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                    <div style={{
                      width: 36, height: 36, borderRadius: 8,
                      background: "#fef2f2", border: "1px solid #fecaca",
                      display: "flex", alignItems: "center", justifyContent: "center", fontSize: 18,
                    }}>🗑️</div>
                    <h3 style={{ color: "#991b1b" }}>Xác nhận xóa</h3>
                  </div>
                  <button className="modal-close" type="button" onClick={() => setDeleteConfirm(null)}>×</button>
                </div>
                <div className="modal-body">
                  <div style={{
                    background: "#fef2f2", border: "1px solid #fecaca", borderRadius: 10,
                    padding: "14px 16px", fontSize: 14, color: "#374151", lineHeight: 1.7,
                  }}>
                    Bạn có chắc muốn xóa sản phẩm{" "}
                    <strong style={{ color: "#991b1b" }}>"{deleteConfirm.tenSanPham}"</strong>?
                    <br />
                    <span style={{ fontSize: 12, color: "#6b7280" }}>Hành động này không thể hoàn tác.</span>
                  </div>
                </div>
                <div className="modal-footer">
                  <button className="secondary-btn btn-icon" type="button" onClick={() => setDeleteConfirm(null)}><ActionIcon name="close" /> Hủy bỏ</button>
                  <button
                      type="button"
                      style={{
                        padding: "9px 20px", background: "#dc2626", color: "#fff",
                        border: "none", borderRadius: 9, fontSize: 13, fontWeight: 600,
                        cursor: "pointer", boxShadow: "0 2px 6px rgba(220,38,38,0.3)",
                      }}
                      onClick={() => handleDelete(deleteConfirm)}
                  >
                    <ActionIcon name="delete" /> Xóa sản phẩm
                  </button>
                </div>
              </div>
            </div>
        )}

        {/* ── Detail modal ── */}
        {detailProduct && (
            <ProductDetailModal
                product={detailProduct} categories={categories} baseUrl={API_BASE_URL}
                onClose={() => setDetailProduct(null)}
                onEdit={canWriteProducts ? openEdit : null}
                onManageImages={canWriteProducts ? setImgProduct : null}
            />
        )}
      </main>
  );
}
