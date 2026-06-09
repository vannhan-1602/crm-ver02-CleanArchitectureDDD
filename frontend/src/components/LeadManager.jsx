import React, { useState, useEffect } from "react";

const API_URL = "http://localhost:8081/api/leads";

export default function LeadManager() {
  const [leads, setLeads] = useState([]);
  const [formData, setFormData] = useState({
    tenLead: "",
    tenCongTy: "",
    soDienThoai: "",
    email: "",
    nhanVienPhuTrachId: "",
  });
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchLeads();
  }, []);

  const fetchLeads = async () => {
    setLoading(true);
    try {
      const response = await fetch(API_URL);
      const data = await response.json();
      setLeads(data);
    } catch (error) {
      setMessage("Lỗi khi tải danh sách Lead: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const payload = {
      ...formData,
      nhanVienPhuTrachId: formData.nhanVienPhuTrachId
        ? parseInt(formData.nhanVienPhuTrachId)
        : null,
    };

    try {
      if (editingId) {
        await fetch(`${API_URL}/${editingId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        setMessage("Cập nhật Lead thành công!");
      } else {
        await fetch(API_URL, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        setMessage("Thêm mới Lead thành công!");
      }
      resetForm();
      fetchLeads();
    } catch (error) {
      setMessage("Lỗi: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (lead) => {
    setFormData({
      tenLead: lead.tenLead || "",
      tenCongTy: lead.tenCongTy || "",
      soDienThoai: lead.soDienThoai || "",
      email: lead.email || "",
      nhanVienPhuTrachId: lead.nhanVienPhuTrachId || "",
    });
    setEditingId(lead.id);
    setMessage("");
  };

  const resetForm = () => {
    setEditingId(null);
    setFormData({
      tenLead: "",
      tenCongTy: "",
      soDienThoai: "",
      email: "",
      nhanVienPhuTrachId: "",
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa (Soft Delete) Lead này?"))
      return;
    setLoading(true);
    try {
      await fetch(`${API_URL}/${id}`, { method: "DELETE" });
      setMessage("Xóa thành công!");
      fetchLeads();
    } catch (error) {
      setMessage("Lỗi khi xóa: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleChangeStatus = async (id, newStatus) => {
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/${id}/status`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ tinhTrangMoi: newStatus }),
      });
      if (response.ok) {
        setMessage("Cập nhật trạng thái thành công!");
        fetchLeads();
      } else {
        setMessage("Lỗi không hợp lệ với trạng thái mới.");
      }
    } catch (error) {
      setMessage("Lỗi chuyển trạng thái: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleConvert = async (id) => {
    if (
      !window.confirm("Bạn muốn convert Lead này thành Khách hàng chính thức?")
    )
      return;
    setLoading(true);
    try {
      const response = await fetch(`${API_URL}/${id}/convert`, {
        method: "POST",
      });
      if (response.ok) {
        const newKhachHang = await response.json();
        setMessage(
          `Convert thành công! Mã Khách Hàng mới: ${newKhachHang.maKhachHang}`
        );
        fetchLeads();
      } else {
        setMessage(
          'Lỗi convert. Hãy chắc chắn Lead đang ở trạng thái "DangChamSoc"'
        );
      }
    } catch (error) {
      setMessage("Lỗi hệ thống khi convert: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: "1200px", margin: "auto" }}>
      <h2>Quản lý Lead (Tiềm năng)</h2>

      {message && (
        <div style={{ padding: "10px", marginBottom: "15px", backgroundColor: "#e2f3e5", color: "#2d6a4f", borderRadius: "4px" }}>
          {message}
        </div>
      )}

      <form onSubmit={handleSubmit} style={{ border: "1px solid #ddd", padding: "20px", borderRadius: "8px", marginBottom: "30px", backgroundColor: "#fff" }}>
        <h3>{editingId ? "Sửa thông tin Lead" : "Thêm mới Lead"}</h3>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "15px" }}>
          <div>
            <label style={{ display: "block", marginBottom: "5px" }}>Tên Lead (*): </label>
            <input style={inputStyle} name="tenLead" value={formData.tenLead} onChange={handleInputChange} required />
          </div>
          <div>
            <label style={{ display: "block", marginBottom: "5px" }}>Tên Công ty: </label>
            <input style={inputStyle} name="tenCongTy" value={formData.tenCongTy} onChange={handleInputChange} />
          </div>
          <div>
            <label style={{ display: "block", marginBottom: "5px" }}>Số điện thoại: </label>
            <input style={inputStyle} name="soDienThoai" value={formData.soDienThoai} onChange={handleInputChange} />
          </div>
          <div>
            <label style={{ display: "block", marginBottom: "5px" }}>Email: </label>
            <input style={inputStyle} type="email" name="email" value={formData.email} onChange={handleInputChange} />
          </div>
          <div>
            <label style={{ display: "block", marginBottom: "5px" }}>Nhân viên phụ trách (ID): </label>
            <input style={inputStyle} type="number" name="nhanVienPhuTrachId" value={formData.nhanVienPhuTrachId} onChange={handleInputChange} />
          </div>
        </div>

        <div style={{ marginTop: "20px" }}>
          <button type="submit" disabled={loading} style={btnPrimary}>
            {editingId ? "Cập nhật" : "Thêm mới"}
          </button>
          {editingId && (
            <button type="button" onClick={resetForm} style={btnSecondary}>Hủy</button>
          )}
        </div>
      </form>

      {loading ? (
        <p>Đang tải dữ liệu...</p>
      ) : (
        <div style={{ backgroundColor: "#fff", borderRadius: "8px", overflow: "hidden", border: "1px solid #ddd" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", textAlign: "left" }}>
            <thead>
              <tr style={{ backgroundColor: "#f1f1f1", borderBottom: "2px solid #ddd" }}>
                <th style={thTdStyle}>ID</th>
                <th style={thTdStyle}>Tên Lead</th>
                <th style={thTdStyle}>Công ty</th>
                <th style={thTdStyle}>SĐT & Email</th>
                <th style={thTdStyle}>NV Phụ trách</th>
                <th style={thTdStyle}>Trạng Thái</th>
                <th style={thTdStyle}>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {leads.map((lead) => (
                <tr key={lead.id} style={{ borderBottom: "1px solid #ddd" }}>
                  <td style={thTdStyle}>{lead.id}</td>
                  <td style={thTdStyle}><strong>{lead.tenLead}</strong></td>
                  <td style={thTdStyle}>{lead.tenCongTy || "-"}</td>
                  <td style={thTdStyle}>
                    {lead.soDienThoai}
                    <br />
                    <small style={{ color: "gray" }}>{lead.email}</small>
                  </td>
                  <td style={thTdStyle}>{lead.nhanVienPhuTrachId || "-"}</td>
                  <td style={thTdStyle}>
                    <select
                      value={lead.tinhTrang}
                      onChange={(e) => handleChangeStatus(lead.id, e.target.value)}
                      disabled={lead.tinhTrang === "DaChuyenDoi"}
                      style={{ padding: "5px", borderRadius: "4px" }}
                    >
                      <option value="Moi">Mới</option>
                      <option value="DangChamSoc">Đang chăm sóc</option>
                      <option value="DaChuyenDoi" disabled>Đã chuyển đổi</option>
                      <option value="NgungChamSoc">Ngừng chăm sóc</option>
                    </select>
                  </td>
                  <td style={thTdStyle}>
                    <button onClick={() => handleEdit(lead)} style={btnSmall}>Sửa</button>
                    <button
                      onClick={() => handleDelete(lead.id)}
                      style={{ ...btnSmall, backgroundColor: "#dc3545", color: "white" }}
                    >
                      Xóa
                    </button>
                    {lead.tinhTrang === "DangChamSoc" && (
                      <button
                        onClick={() => handleConvert(lead.id)}
                        style={{ ...btnSmall, backgroundColor: "#198754", color: "white", fontWeight: "bold" }}
                      >
                        Chuyển đổi
                      </button>
                    )}
                  </td>
                </tr>
              ))}
              {leads.length === 0 && (
                <tr>
                  <td colSpan="7" style={{ textAlign: "center", padding: "20px" }}>Chưa có dữ liệu</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

const inputStyle = { width: "100%", padding: "8px", boxSizing: "border-box", borderRadius: "4px", border: "1px solid #ccc" };
const thTdStyle = { padding: "12px 8px" };
const btnPrimary = { padding: "10px 20px", backgroundColor: "#0d6efd", color: "#fff", border: "none", borderRadius: "4px", cursor: "pointer", marginRight: "10px" };
const btnSecondary = { padding: "10px 20px", backgroundColor: "#6c757d", color: "#fff", border: "none", borderRadius: "4px", cursor: "pointer" };
const btnSmall = { padding: "5px 10px", marginRight: "5px", border: "1px solid #ccc", borderRadius: "4px", cursor: "pointer", backgroundColor: "#f8f9fa" };