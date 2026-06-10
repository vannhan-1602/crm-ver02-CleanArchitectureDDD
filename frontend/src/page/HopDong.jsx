import { useEffect, useMemo, useState } from "react";
import "./HopDong.css";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

const emptyForm = {
  maHopDong: "",
  khachHangId: "",
  ngayKy: "",
  thoiHan: "",
  trangThai: "DangThucHien",
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

function HopDong() {
  const [items, setItems] = useState([]);
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
      return [item.maHopDong, item.khachHangId, item.trangThai, item.ngayKy]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(keyword));
    });
  }, [items, search]);

  const loadHopDong = async () => {
    setLoading(true);
    setError("");
    try {
      const response = await fetch(`${API_BASE_URL}/api/hop-dong`);
      if (!response.ok) {
        throw new Error(
          `Khong the tai danh sach hop dong (${response.status})`,
        );
      }
      const data = await response.json();
      setItems(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Tai danh sach that bai");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHopDong();
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
    if (!form.maHopDong.trim()) return "Ma hop dong khong duoc rong";
    if (!form.khachHangId.trim()) return "Khach hang ID khong duoc rong";
    if (!form.ngayKy) return "Ngay ky khong duoc rong";
    if (!form.thoiHan.trim()) return "Thoi han khong duoc rong";
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
      const response = await fetch(
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
            ? `Cap nhat that bai (${response.status})`
            : `Tao moi that bai (${response.status})`,
        );
      }

      await loadHopDong();
      resetForm();
      setSuccess(
        editingId ? "Cap nhat hop dong thanh cong" : "Tao hop dong thanh cong",
      );
    } catch (err) {
      setError(err.message || "Khong the luu hop dong");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (item) => {
    setEditingId(item.id);
    setForm({
      maHopDong: item.maHopDong ?? "",
      khachHangId: item.khachHangId ?? "",
      ngayKy: item.ngayKy ?? "",
      thoiHan: item.thoiHan ?? "",
      trangThai: item.trangThai ?? "DangThucHien",
    });
    setError("");
    setSuccess("");
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm("Ban co muon xoa hop dong nay khong?");
    if (!confirmed) return;

    setError("");
    setSuccess("");

    try {
      const response = await fetch(`${API_BASE_URL}/api/hop-dong/${id}`, {
        method: "DELETE",
      });

      if (!response.ok && response.status !== 204) {
        throw new Error(`Xoa that bai (${response.status})`);
      }

      await loadHopDong();
      if (editingId === id) {
        resetForm();
      }
      setSuccess("Xoa hop dong thanh cong");
    } catch (err) {
      setError(err.message || "Khong the xoa hop dong");
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
          <h1>Quan ly hop dong</h1>
          <p className="subtitle">
            Tao, cap nhat, tim va xoa hop dong truc tiep tu API backend.
          </p>
        </div>

        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="Tim theo ma, khach hang, trang thai..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <button className="secondary-btn" type="button" onClick={loadHopDong}>
            Tai lai
          </button>
        </div>
      </section>

      <section className="stats-row">
        <article className="stat-card">
          <span>Tong so</span>
          <strong>{stats.total}</strong>
        </article>
        <article className="stat-card">
          <span>Dang thuc hien</span>
          <strong>{stats.active}</strong>
        </article>
        <article className="stat-card">
          <span>Tam dung</span>
          <strong>{stats.paused}</strong>
        </article>
        <article className="stat-card">
          <span>Thanh ly</span>
          <strong>{stats.closed}</strong>
        </article>
      </section>

      <section className="content-grid">
        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-head">
            <div>
              <h2>{editingId ? "Cap nhat hop dong" : "Tao hop dong moi"}</h2>
              <p>Du lieu se goi vao API `api/hop-dong`.</p>
            </div>
            {editingId ? (
              <button className="ghost-btn" type="button" onClick={resetForm}>
                Huy sua
              </button>
            ) : null}
          </div>

          <label>
            Ma hop dong
            <input
              name="maHopDong"
              value={form.maHopDong}
              onChange={handleChange}
              placeholder="HD-0001"
            />
          </label>

          <label>
            Khach hang ID
            <input
              name="khachHangId"
              type="number"
              min="1"
              value={form.khachHangId}
              onChange={handleChange}
              placeholder="1"
            />
          </label>

          <div className="two-col">
            <label>
              Ngay ky
              <input
                name="ngayKy"
                type="date"
                value={form.ngayKy}
                onChange={handleChange}
              />
            </label>

            <label>
              Thoi han
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
            Trang thai
            <select
              name="trangThai"
              value={form.trangThai}
              onChange={handleChange}
            >
              <option value="DangThucHien">Dang thuc hien</option>
              <option value="TamDung">Tam dung</option>
              <option value="ThanhLy">Thanh ly</option>
            </select>
          </label>

          {error ? <div className="message error">{error}</div> : null}
          {success ? <div className="message success">{success}</div> : null}

          <div className="actions">
            <button className="primary-btn" type="submit" disabled={submitting}>
              {submitting ? "Dang luu..." : editingId ? "Cap nhat" : "Tao moi"}
            </button>
            <button className="secondary-btn" type="button" onClick={resetForm}>
              Lam moi form
            </button>
          </div>
        </form>

        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sach hop dong</h2>
              <p>
                Hien thi {filteredItems.length}/{items.length} ban ghi.
              </p>
            </div>
            {loading ? <span className="loading">Dang tai...</span> : null}
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Ma hop dong</th>
                  <th>Khach hang</th>
                  <th>Ngay ky</th>
                  <th>Thoi han</th>
                  <th>Trang thai</th>
                  <th>Cap nhat</th>
                  <th>Hanh dong</th>
                </tr>
              </thead>
              <tbody>
                {filteredItems.length === 0 ? (
                  <tr>
                    <td colSpan="8" className="empty-row">
                      {loading
                        ? "Dang tai du lieu..."
                        : "Khong co du lieu phu hop"}
                    </td>
                  </tr>
                ) : (
                  filteredItems.map((item) => (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td>{item.maHopDong}</td>
                      <td>{item.khachHangId}</td>
                      <td>{item.ngayKy ?? "-"}</td>
                      <td>{item.thoiHan ?? "-"}</td>
                      <td>
                        <span
                          className={`badge badge-${String(item.trangThai || "").toLowerCase()}`}
                        >
                          {item.trangThai || "-"}
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
                            Sua
                          </button>
                          <button
                            type="button"
                            className="danger-btn"
                            onClick={() => handleDelete(item.id)}
                          >
                            Xoa
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
