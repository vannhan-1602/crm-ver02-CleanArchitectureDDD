import { useEffect, useMemo, useState } from "react";
import { authFetch } from "../apiClient";
import "./HopDong.css";
import { ActionIcon } from "../moduleIcons.jsx";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

const emptyForm = {
  maHopDong: "",
  khachHangId: "",
  ngayKy: "",
  thoiHan: "",
  trangThai: "DangThucHien",
};

const STATUS_LABELS = {
  DangThucHien: "Đang thực hiện",
  TamDung: "Tạm dừng",
  ThanhLy: "Thanh lý",
};

function formatStatus(value) {
  return STATUS_LABELS[value] ?? value ?? "-";
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

function HopDong() {
  const [items, setItems] = useState([]);
  const [khachHangList, setKhachHangList] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [search, setSearch] = useState("");

  const filteredItems = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    if (!keyword) return items;
    return items.filter((item) => {
      return [
        item.maHopDong,
        item.tenKhachHang,
        item.khachHangId,
        item.trangThai,
        item.ngayKy,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(keyword));
    });
  }, [items, search]);

  const loadHopDong = async () => {
    setLoading(true);
    setError("");
    try {
      const response = await authFetch(`${API_BASE_URL}/api/hop-dong`);
      if (!response.ok) {
        throw new Error(
          `Không thể tải danh sách hợp đồng (${response.status})`,
        );
      }
      const data = await response.json();
      setItems(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Tải danh sách thất bại");
    } finally {
      setLoading(false);
    }
  };

  const loadKhachHangList = async () => {
    try {
      const response = await authFetch(`${API_BASE_URL}/api/khach-hang`);
      if (!response.ok) return;
      const data = await response.json();
      setKhachHangList(Array.isArray(data) ? data : []);
    } catch {
      // Danh sách khách hàng chỉ phục vụ dropdown, không chặn form nếu lỗi.
    }
  };

  useEffect(() => {
    const initialize = async () => {
      await Promise.all([loadHopDong(), loadKhachHangList()]);
    };

    void initialize();
  }, []);

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const validateForm = () => {
    if (!String(form.maHopDong ?? "").trim()) return "Mã hợp đồng không được rỗng";
    if (!String(form.khachHangId ?? "").trim()) return "Khách hàng không được rỗng";
    if (!form.ngayKy) return "Ngày ký không được rỗng";
    if (!String(form.thoiHan ?? "").trim()) return "Thời hạn không được rỗng";
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
      maHopDong: form.maHopDong.trim(),
      khachHangId: Number(form.khachHangId),
      ngayKy: form.ngayKy,
      thoiHan: Number(form.thoiHan),
      trangThai: form.trangThai,
    };

    try {
      const response = await authFetch(
        editingId
          ? `${API_BASE_URL}/api/hop-dong/${editingId}`
          : `${API_BASE_URL}/api/hop-dong`,
        {
          method: editingId ? "PUT" : "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(payload),
        },
      );

      if (!response.ok) {
        throw new Error(
          editingId
            ? `Cập nhật thất bại (${response.status})`
            : `Tạo mới thất bại (${response.status})`,
        );
      }

      await loadHopDong();
      resetForm();
      setSuccess(
        editingId ? "Cập nhật hợp đồng thành công" : "Tạo hợp đồng thành công",
      );
    } catch (err) {
      setError(err.message || "Không thể lưu hợp đồng");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (item) => {
    setEditingId(item.id);
    setForm({
      maHopDong: item.maHopDong ?? "",
      khachHangId: item.khachHangId != null ? String(item.khachHangId) : "",
      ngayKy: item.ngayKy ?? "",
      thoiHan: item.thoiHan != null ? String(item.thoiHan) : "",
      trangThai: item.trangThai ?? "DangThucHien",
    });
    setError("");
    setSuccess("");
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Bạn có muốn xóa hợp đồng này không?");
    if (!confirmed) return;

    setError("");
    setSuccess("");

    try {
      const response = await authFetch(`${API_BASE_URL}/api/hop-dong/${id}`, {
        method: "DELETE",
      });

      if (!response.ok && response.status !== 204) {
        throw new Error(`Xóa thất bại (${response.status})`);
      }

      await loadHopDong();
      if (editingId === id) {
        resetForm();
      }
      setSuccess("Xóa hợp đồng thành công");
    } catch (err) {
      setError(err.message || "Không thể xóa hợp đồng");
    }
  };

  const stats = useMemo(() => {
    return {
      total: items.length,
      active: items.filter((item) => item.trangThai === "DangThucHien").length,
      paused: items.filter((item) => item.trangThai === "TamDung").length,
      closed: items.filter((item) => item.trangThai === "ThanhLy").length,
    };
  }, [items]);

  return (
    <main className="hopdong-page">
      <section className="hopdong-header">
        <div>
          <p className="eyebrow">CRM / Hop dong</p>
          <h1>Quản lý hợp đồng</h1>
          
        </div>

        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="🔎Tìm theo mã, khách hàng, trạng thái..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <button className="secondary-btn btn-icon" type="button" onClick={loadHopDong}>
            <ActionIcon name="refresh" /> Tải lại
          </button>
        </div>
      </section>

      <section className="stats-row">
        <article className="stat-card">
          <span>Tổng số</span>
          <strong>{stats.total}</strong>
        </article>
        <article className="stat-card">
          <span>Đang thực hiện</span>
          <strong>{stats.active}</strong>
        </article>
        <article className="stat-card">
          <span>Tạm dừng</span>
          <strong>{stats.paused}</strong>
        </article>
        <article className="stat-card">
          <span>Thanh lý</span>
          <strong>{stats.closed}</strong>
        </article>
      </section>

      <section className="content-grid">
        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-head">
            <div>
              <h2>{editingId ? "Cập nhật hợp đồng" : "Tạo hợp đồng mới"}</h2>
            </div>
            {editingId ? (
              <button className="ghost-btn btn-icon" type="button" onClick={resetForm}>
                <ActionIcon name="close" /> Hủy sửa
              </button>
            ) : null}
          </div>

          <label>
            Mã hợp đồng
            <input
              name="maHopDong"
              value={form.maHopDong}
              onChange={handleChange}
              placeholder="HD-0001"
            />
          </label>

          <label>
            Tên khách hàng
            <select
              name="khachHangId"
              value={form.khachHangId}
              onChange={handleChange}
            >
              <option value="">-- Chọn khách hàng --</option>
              {khachHangList.map((khachHang) => (
                <option key={khachHang.id} value={khachHang.id}>
                  {khachHang.tenKhachHang ?? `Khách hàng #${khachHang.id}`}
                </option>
              ))}
            </select>
          </label>

          <div className="two-col">
            <label>
              Ngày ký
              <input
                name="ngayKy"
                type="date"
                value={form.ngayKy}
                onChange={handleChange}
              />
            </label>

            <label>
              Thời hạn
              <input
                name="thoiHan"
                type="number"
                min="0"
                value={form.thoiHan}
                onChange={handleChange}
                placeholder="12"
              />
            </label>
          </div>

          <label>
            Trạng thái
            <select
              name="trangThai"
              value={form.trangThai}
              onChange={handleChange}
            >
              {Object.entries(STATUS_LABELS).map(([value, label]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ))}
            </select>
          </label>

          {error ? <div className="message error">{error}</div> : null}
          {success ? <div className="message success">{success}</div> : null}

          <div className="actions">
            <button className="primary-btn btn-icon" type="submit" disabled={submitting}>
                      <ActionIcon name="save" />
              {submitting ? "Đang lưu..." : editingId ? "Cập nhật" : "Tạo mới"}
            </button>
            <button className="secondary-btn btn-icon" type="button" onClick={resetForm}>
              Làm mới form
            </button>
          </div>
        </form>

        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sách hợp đồng</h2>
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
                  <th>ID</th>
                  <th>Mã hợp đồng</th>
                  <th>Khách hàng</th>
                  <th>Ngày ký</th>
                  <th>Thời hạn</th>
                  <th>Trạng thái</th>
                  <th>Cập nhật</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {filteredItems.length === 0 ? (
                  <tr>
                    <td colSpan="8" className="empty-row">
                      {loading
                        ? "Đang tải dữ liệu..."
                        : "Không có dữ liệu phù hợp"}
                    </td>
                  </tr>
                ) : (
                  filteredItems.map((item) => (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td>{item.maHopDong}</td>
                      <td>
                        <div className="stacked-cell">
                          <strong>{item.tenKhachHang || 'Không có tên'}</strong>
                        </div>
                      </td>
                      <td>{item.ngayKy ?? "-"}</td>
                      <td>{item.thoiHan ?? "-"}</td>
                      <td>
                        <span
                          className={`badge badge-${String(item.trangThai || "").toLowerCase()}`}
                        >
                          {formatStatus(item.trangThai)}
                        </span>
                      </td>
                      <td>{formatDateTime(item.updatedAt)}</td>
                      <td>
                        <div className="row-actions">
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

export default HopDong;

