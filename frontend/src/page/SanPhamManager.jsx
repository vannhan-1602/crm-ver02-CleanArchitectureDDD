import { useState, useEffect, useRef } from "react";
import axios from "axios";

const API = "http://localhost:8081/api/sanpham";

const ax = axios.create({ baseURL: API });

const formatVND = (n) =>
  new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(n);

const STATUS_MAP = { 1: { label: "Đang bán", color: "#166534", bg: "#dcfce7" }, 0: { label: "Ngừng bán", color: "#991b1b", bg: "#fee2e2" } };

const EMPTY_FORM = { maSanPham: "", tenSanPham: "", donVi: "", giaBan: "", slTon: "", trangThai: 1, loaiSanPham: "" };

export default function SanPhamManager() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const [filterCat, setFilterCat] = useState("all");
  const [modal, setModal] = useState(null); // null | "add" | "edit" | "images"
  const [form, setForm] = useState(EMPTY_FORM);
  const [editId, setEditId] = useState(null);
  const [imgProduct, setImgProduct] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState(null);
  const fileRef = useRef();

  const fetchAll = async () => {
    try {
      setLoading(true);
      const [pRes, cRes] = await Promise.all([ax.get("/sanpham"), ax.get("/loaisanpham")]);
      setProducts(pRes.data);
      setCategories(cRes.data);
    } catch {
      setError("Không thể tải dữ liệu. Kiểm tra kết nối backend.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAll(); }, []);

  const filtered = products.filter((p) => {
    const matchSearch = p.tenSanPham?.toLowerCase().includes(search.toLowerCase()) || p.maSanPham?.toLowerCase().includes(search.toLowerCase());
    const matchCat = filterCat === "all" || String(p.loaiSanPham) === filterCat;
    return matchSearch && matchCat;
  });

  const catName = (id) => categories.find((c) => c.id === id)?.tenLoai || `Loại ${id}`;

  const openAdd = () => { setForm(EMPTY_FORM); setEditId(null); setModal("add"); };
  const openEdit = (p) => {
    setForm({ maSanPham: p.maSanPham, tenSanPham: p.tenSanPham, donVi: p.donVi, giaBan: p.giaBan, slTon: p.slTon, trangThai: p.trangThai, loaiSanPham: p.loaiSanPham });
    setEditId(p.sanPhamId);
    setModal("edit");
  };
  const openImages = (p) => { setImgProduct(p); setModal("images"); };
  const closeModal = () => { setModal(null); setImgProduct(null); setError(""); };

  const handleSave = async () => {
    if (!form.maSanPham || !form.tenSanPham || !form.loaiSanPham) { setError("Vui lòng điền đầy đủ thông tin bắt buộc."); return; }
    setSaving(true);
    try {
      if (modal === "add") {
        await ax.post("/sanpham", { ...form, giaBan: Number(form.giaBan), slTon: Number(form.slTon) });
      } else {
        await ax.put(`/sanpham/${editId}`, { ...form, giaBan: Number(form.giaBan), slTon: Number(form.slTon) });
      }
      await fetchAll();
      closeModal();
    } catch {
      setError("Lưu thất bại. Kiểm tra lại dữ liệu.");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await ax.delete(`/sanpham/${id}`);
      setDeleteConfirm(null);
      await fetchAll();
    } catch {
      setError("Xóa thất bại.");
    }
  };

  const handleUploadImage = async (e) => {
    const file = e.target.files[0];
    if (!file || !imgProduct) return;
    setUploading(true);
    const fd = new FormData();
    fd.append("file", file);
    fd.append("isMain", 0);
    try {
      await ax.post(`/sanpham/${imgProduct.sanPhamId}/hinhanh`, fd, {
        headers: { "Content-Type": "multipart/form-data" }
      });      const res = await ax.get("/sanpham");
      setProducts(res.data);
      setImgProduct(res.data.find((p) => p.sanPhamId === imgProduct.sanPhamId));
    } catch {
      setError("Upload ảnh thất bại.");
    } finally {
      setUploading(false);
      fileRef.current.value = "";
    }
  };

  const handleDeleteImage = async (imgId) => {
    try {
      await ax.delete(`/sanpham/${imgProduct.sanPhamId}/hinhanh/${imgId}`);
      const res = await ax.get("/sanpham");
      setProducts(res.data);
      setImgProduct(res.data.find((p) => p.sanPhamId === imgProduct.sanPhamId));
    } catch {
      setError("Xóa ảnh thất bại.");
    }
  };

  const inp = "width:100%;padding:8px 10px;border:0.5px solid #d1d5db;border-radius:8px;font-size:14px;outline:none;box-sizing:border-box;background:#fff;color:#111";
  const lbl = "display:block;font-size:13px;color:#6b7280;margin-bottom:4px;font-weight:500";

  return (
    <div style={{ fontFamily: "'Inter','Segoe UI',sans-serif", padding: "24px", maxWidth: 1100, margin: "0 auto" }}>
      <h2 style={{ fontStyle: "normal", fontFamily: "inherit", fontSize: 20, fontWeight: 600, color: "#111827", margin: "0 0 4px" }}>
        Quản lý sản phẩm
      </h2>
      <p style={{ fontSize: 13, color: "#6b7280", margin: "0 0 20px" }}>
        {products.length} sản phẩm • {categories.length} loại
      </p>

      {error && (
        <div style={{ background: "#fee2e2", border: "0.5px solid #fca5a5", borderRadius: 8, padding: "10px 14px", fontSize: 13, color: "#991b1b", marginBottom: 16, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          {error}
          <button onClick={() => setError("")} style={{ background: "none", border: "none", cursor: "pointer", color: "#991b1b", fontSize: 16, lineHeight: 1 }}>×</button>
        </div>
      )}

      <div style={{ display: "flex", gap: 10, marginBottom: 16, flexWrap: "wrap" }}>
        <input
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Tìm theo tên, mã sản phẩm..."
          style={{ ...inp.split(";").reduce((a, s) => { const [k, v] = s.split(":"); if (k?.trim()) a[k.trim().replace(/-./g, m => m[1].toUpperCase())] = v?.trim(); return a; }, {}), flex: 1, minWidth: 200 }}
        />
        <select value={filterCat} onChange={(e) => setFilterCat(e.target.value)}
          style={{ padding: "8px 10px", border: "0.5px solid #d1d5db", borderRadius: 8, fontSize: 14, background: "#fff", color: "#111", minWidth: 160 }}>
          <option value="all">Tất cả loại</option>
          {categories.map((c) => <option key={c.id} value={c.id}>{c.tenLoai}</option>)}
        </select>
        <button onClick={openAdd} style={{ padding: "8px 16px", background: "#2563eb", color: "#fff", border: "none", borderRadius: 8, fontSize: 14, fontWeight: 500, cursor: "pointer", whiteSpace: "nowrap" }}>
          + Thêm sản phẩm
        </button>
      </div>

      {loading ? (
        <div style={{ textAlign: "center", padding: "48px 0", color: "#6b7280", fontSize: 14 }}>Đang tải...</div>
      ) : (
        <div style={{ border: "0.5px solid #e5e7eb", borderRadius: 12, overflow: "hidden" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", fontSize: 14 }}>
            <thead>
              <tr style={{ background: "#f9fafb" }}>
                {["Mã SP", "Tên sản phẩm", "Loại", "Đơn vị", "Giá bán", "Tồn kho", "Trạng thái", "Ảnh", ""].map((h, i) => (
                  <th key={i} style={{ padding: "10px 12px", textAlign: i >= 4 ? "right" : "left", fontWeight: 500, color: "#6b7280", fontSize: 12, borderBottom: "0.5px solid #e5e7eb", whiteSpace: "nowrap" }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {filtered.length === 0 ? (
                <tr><td colSpan={9} style={{ textAlign: "center", padding: "32px", color: "#9ca3af", fontSize: 13 }}>Không tìm thấy sản phẩm nào</td></tr>
              ) : filtered.map((p, i) => (
                <tr key={p.sanPhamId} style={{ borderBottom: "0.5px solid #f3f4f6", background: i % 2 === 0 ? "#fff" : "#fafafa" }}>
                  <td style={{ padding: "10px 12px", fontFamily: "monospace", fontSize: 12, color: "#374151" }}>{p.maSanPham}</td>
                  <td style={{ padding: "10px 12px", fontWeight: 500, color: "#111827", maxWidth: 200 }}>{p.tenSanPham}</td>
                  <td style={{ padding: "10px 12px" }}>
                    <span style={{ background: "#eff6ff", color: "#1d4ed8", borderRadius: 6, padding: "2px 8px", fontSize: 12 }}>{catName(p.loaiSanPham)}</span>
                  </td>
                  <td style={{ padding: "10px 12px", color: "#6b7280" }}>{p.donVi}</td>
                  <td style={{ padding: "10px 12px", textAlign: "right", fontWeight: 500, color: "#059669" }}>{formatVND(p.giaBan)}</td>
                  <td style={{ padding: "10px 12px", textAlign: "right", color: p.slTon < 10 ? "#dc2626" : "#374151" }}>{p.slTon}</td>
                  <td style={{ padding: "10px 12px", textAlign: "right" }}>
                    <span style={{ background: STATUS_MAP[p.trangThai]?.bg, color: STATUS_MAP[p.trangThai]?.color, borderRadius: 6, padding: "2px 8px", fontSize: 12, whiteSpace: "nowrap" }}>
                      {STATUS_MAP[p.trangThai]?.label}
                    </span>
                  </td>
                  <td style={{ padding: "10px 12px", textAlign: "right" }}>
                    <button onClick={() => openImages(p)} style={{ background: "none", border: "0.5px solid #d1d5db", borderRadius: 6, padding: "4px 8px", fontSize: 12, cursor: "pointer", color: "#6b7280" }}>
                      🖼 {p.hinhAnh?.length ?? 0}
                    </button>
                  </td>
                  <td style={{ padding: "10px 12px", textAlign: "right", whiteSpace: "nowrap" }}>
                    <button onClick={() => openEdit(p)} style={{ background: "none", border: "0.5px solid #d1d5db", borderRadius: 6, padding: "4px 8px", fontSize: 12, cursor: "pointer", color: "#374151", marginRight: 6 }}>Sửa</button>
                    <button onClick={() => setDeleteConfirm(p)} style={{ background: "none", border: "0.5px solid #fca5a5", borderRadius: 6, padding: "4px 8px", fontSize: 12, cursor: "pointer", color: "#dc2626" }}>Xóa</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {(modal === "add" || modal === "edit") && (
        <div style={{ position: "fixed", inset: 0, background: "rgba(0,0,0,0.4)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000, padding: 16 }}>
          <div style={{ background: "#fff", borderRadius: 14, padding: 28, width: "100%", maxWidth: 480, maxHeight: "90vh", overflowY: "auto" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 }}>
              <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>{modal === "add" ? "Thêm sản phẩm mới" : "Chỉnh sửa sản phẩm"}</h3>
              <button onClick={closeModal} style={{ background: "none", border: "none", fontSize: 20, cursor: "pointer", color: "#6b7280", lineHeight: 1 }}>×</button>
            </div>
            {error && <div style={{ background: "#fee2e2", color: "#991b1b", borderRadius: 8, padding: "8px 12px", fontSize: 13, marginBottom: 14 }}>{error}</div>}
            <div style={{ display: "grid", gap: 14 }}>
              {[
                { label: "Mã sản phẩm *", key: "maSanPham", type: "text" },
                { label: "Tên sản phẩm *", key: "tenSanPham", type: "text" },
                { label: "Đơn vị", key: "donVi", type: "text" },
                { label: "Giá bán (VNĐ)", key: "giaBan", type: "number" },
                { label: "Số lượng tồn", key: "slTon", type: "number" },
              ].map(({ label, key, type }) => (
                <div key={key}>
                  <label style={lbl}>{label}</label>
                  <input type={type} value={form[key]} onChange={(e) => setForm({ ...form, [key]: e.target.value })}
                    style={{ width: "100%", padding: "8px 10px", border: "0.5px solid #d1d5db", borderRadius: 8, fontSize: 14, outline: "none", boxSizing: "border-box" }} />
                </div>
              ))}
              <div>
                <label style={lbl}>Loại sản phẩm *</label>
                <select value={form.loaiSanPham} onChange={(e) => setForm({ ...form, loaiSanPham: e.target.value })}
                  style={{ width: "100%", padding: "8px 10px", border: "0.5px solid #d1d5db", borderRadius: 8, fontSize: 14, background: "#fff", boxSizing: "border-box" }}>
                  <option value="">-- Chọn loại --</option>
                  {categories.map((c) => <option key={c.id} value={c.id}>{c.tenLoai}</option>)}
                </select>
              </div>
              <div>
                <label style={lbl}>Trạng thái</label>
                <select value={form.trangThai} onChange={(e) => setForm({ ...form, trangThai: Number(e.target.value) })}
                  style={{ width: "100%", padding: "8px 10px", border: "0.5px solid #d1d5db", borderRadius: 8, fontSize: 14, background: "#fff", boxSizing: "border-box" }}>
                  <option value={1}>Đang bán</option>
                  <option value={0}>Ngừng bán</option>
                </select>
              </div>
            </div>
            <div style={{ display: "flex", gap: 10, marginTop: 22, justifyContent: "flex-end" }}>
              <button onClick={closeModal} style={{ padding: "8px 18px", border: "0.5px solid #d1d5db", borderRadius: 8, background: "#fff", fontSize: 14, cursor: "pointer" }}>Hủy</button>
              <button onClick={handleSave} disabled={saving}
                style={{ padding: "8px 18px", background: saving ? "#93c5fd" : "#2563eb", color: "#fff", border: "none", borderRadius: 8, fontSize: 14, fontWeight: 500, cursor: saving ? "not-allowed" : "pointer" }}>
                {saving ? "Đang lưu..." : "Lưu"}
              </button>
            </div>
          </div>
        </div>
      )}

      {modal === "images" && imgProduct && (
        <div style={{ position: "fixed", inset: 0, background: "rgba(0,0,0,0.4)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000, padding: 16 }}>
          <div style={{ background: "#fff", borderRadius: 14, padding: 28, width: "100%", maxWidth: 520, maxHeight: "90vh", overflowY: "auto" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 4 }}>
              <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>Hình ảnh sản phẩm</h3>
              <button onClick={closeModal} style={{ background: "none", border: "none", fontSize: 20, cursor: "pointer", color: "#6b7280", lineHeight: 1 }}>×</button>
            </div>
            <p style={{ margin: "0 0 18px", fontSize: 13, color: "#6b7280" }}>{imgProduct.tenSanPham}</p>
            {error && <div style={{ background: "#fee2e2", color: "#991b1b", borderRadius: 8, padding: "8px 12px", fontSize: 13, marginBottom: 14 }}>{error}</div>}

            {imgProduct.hinhAnh?.length > 0 ? (
              <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(120px, 1fr))", gap: 12, marginBottom: 18 }}>
                {imgProduct.hinhAnh.map((img) => (
                  <div key={img.id} style={{ border: "0.5px solid #e5e7eb", borderRadius: 10, overflow: "hidden", position: "relative" }}>
                    <img src={`http://localhost:8081${img.url}`} alt="" style={{ width: "100%", height: 100, objectFit: "cover" }}
                       onError={(e) => { e.target.style.display = "none"; }} />
                    <div style={{ display: "none", width: "100%", height: 100, background: "#f3f4f6", alignItems: "center", justifyContent: "center", fontSize: 24 }}>🖼</div>
                    {img.isMain === 1 && (
                      <span style={{ position: "absolute", top: 4, left: 4, background: "#2563eb", color: "#fff", fontSize: 10, padding: "2px 6px", borderRadius: 4 }}>Chính</span>
                    )}
                    <div style={{ padding: "6px 8px", display: "flex", justifyContent: "flex-end" }}>
                      <button onClick={() => handleDeleteImage(img.id)}
                        style={{ background: "none", border: "0.5px solid #fca5a5", borderRadius: 5, padding: "3px 8px", fontSize: 11, cursor: "pointer", color: "#dc2626" }}>Xóa</button>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div style={{ border: "1px dashed #d1d5db", borderRadius: 10, padding: "24px", textAlign: "center", color: "#9ca3af", fontSize: 13, marginBottom: 18 }}>
                Chưa có hình ảnh nào
              </div>
            )}

            <div style={{ borderTop: "0.5px solid #f3f4f6", paddingTop: 16 }}>
              <label style={{ display: "block", fontSize: 13, fontWeight: 500, color: "#374151", marginBottom: 8 }}>Thêm ảnh mới</label>
              <div style={{ display: "flex", gap: 10, alignItems: "center" }}>
                <input ref={fileRef} type="file" accept="image/*" onChange={handleUploadImage}
                  style={{ flex: 1, fontSize: 13, border: "0.5px solid #d1d5db", borderRadius: 8, padding: "6px 10px" }} />
                {uploading && <span style={{ fontSize: 13, color: "#6b7280" }}>Đang upload...</span>}
              </div>
              <p style={{ fontSize: 11, color: "#9ca3af", marginTop: 6 }}>Hỗ trợ JPG, PNG, WEBP</p>
            </div>
          </div>
        </div>
      )}

      {deleteConfirm && (
        <div style={{ position: "fixed", inset: 0, background: "rgba(0,0,0,0.4)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000, padding: 16 }}>
          <div style={{ background: "#fff", borderRadius: 14, padding: 28, width: "100%", maxWidth: 360 }}>
            <h3 style={{ margin: "0 0 8px", fontSize: 16, fontWeight: 600, color: "#111827" }}>Xác nhận xóa</h3>
            <p style={{ margin: "0 0 20px", fontSize: 14, color: "#6b7280", lineHeight: 1.6 }}>
              Bạn có chắc muốn xóa sản phẩm <strong style={{ color: "#111827" }}>{deleteConfirm.tenSanPham}</strong>? Hành động này không thể hoàn tác.
            </p>
            <div style={{ display: "flex", gap: 10, justifyContent: "flex-end" }}>
              <button onClick={() => setDeleteConfirm(null)} style={{ padding: "8px 18px", border: "0.5px solid #d1d5db", borderRadius: 8, background: "#fff", fontSize: 14, cursor: "pointer" }}>Hủy</button>
              <button onClick={() => handleDelete(deleteConfirm.sanPhamId)} style={{ padding: "8px 18px", background: "#dc2626", color: "#fff", border: "none", borderRadius: 8, fontSize: 14, fontWeight: 500, cursor: "pointer" }}>Xóa</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
