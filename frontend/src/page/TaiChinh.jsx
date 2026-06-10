import { useEffect, useMemo, useState } from "react";
import "./HopDong.css";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

const TABS = [
  { id: "hoaDon", label: "Hóa đơn" },
  { id: "phieuThu", label: "Phiếu thu" },
  { id: "phieuChi", label: "Phiếu chi" },
];

const PAYMENT_STATUS_LABEL = {
  ChuaThanhToan: "Chưa thanh toán",
  ThanhToan1Phan: "Thanh toán 1 phần",
  HoanTat: "Hoàn tất",
};

const emptyHoaDonForm = {
  maHoaDon: "",
  hopDongId: "",
  khachHangId: "",
  tongTien: "",
  trangThaiThanhToan: "ChuaThanhToan",
};

const emptyPhieuThuForm = {
  maPhieuThu: "",
  khachHangId: "",
  hoaDonId: "",
  soTien: "",
  nguoiLapId: "",
};

const emptyPhieuChiForm = {
  maPhieuChi: "",
  khachHangId: "",
  hoaDonId: "",
  soTien: "",
  nguoiLapId: "",
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

function formatMoney(value) {
  const amount = Number(value);
  if (!Number.isFinite(amount)) return "-";
  return new Intl.NumberFormat("vi-VN").format(amount);
}

function formatHoaDonOption(hoaDon) {
  return `#${hoaDon.id} - ${hoaDon.maHoaDon} - KH ${hoaDon.khachHangId} - ${formatMoney(hoaDon.tongTien)}`;
}

function formatKhachHangOption(hoaDon) {
  return `KH ${hoaDon.khachHangId} - từ ${hoaDon.maHoaDon}`;
}

function toNumberOrNull(value) {
  if (value === "" || value === null || value === undefined) return null;
  return Number(value);
}

async function throwIfRequestFailed(response, fallbackMessage) {
  if (response.ok) return;

  let message = fallbackMessage;
  try {
    const data = await response.json();
    message = data.message || data.error || fallbackMessage;
  } catch {
    try {
      const text = await response.text();
      message = text || fallbackMessage;
    } catch {
      message = fallbackMessage;
    }
  }

  throw new Error(`${message} (${response.status})`);
}

function TaiChinh() {
  const [activeTab, setActiveTab] = useState("hoaDon");
  const [hoaDons, setHoaDons] = useState([]);
  const [phieuThus, setPhieuThus] = useState([]);
  const [phieuChis, setPhieuChis] = useState([]);
  const [hoaDonForm, setHoaDonForm] = useState(emptyHoaDonForm);
  const [phieuThuForm, setPhieuThuForm] = useState(emptyPhieuThuForm);
  const [phieuChiForm, setPhieuChiForm] = useState(emptyPhieuChiForm);
  const [editing, setEditing] = useState({ type: null, id: null });
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [search, setSearch] = useState("");

  const loadAll = async () => {
    setLoading(true);
    setError("");
    try {
      const [hoaDonRes, phieuThuRes, phieuChiRes] = await Promise.all([
        fetch(`${API_BASE_URL}/api/hoa-don`),
        fetch(`${API_BASE_URL}/api/phieu-thu`),
        fetch(`${API_BASE_URL}/api/phieu-chi`),
      ]);

      if (!hoaDonRes.ok) throw new Error(`Tải hóa đơn thất bại (${hoaDonRes.status})`);
      if (!phieuThuRes.ok) throw new Error(`Tải phiếu thu thất bại (${phieuThuRes.status})`);
      if (!phieuChiRes.ok) throw new Error(`Tải phiếu chi thất bại (${phieuChiRes.status})`);

      setHoaDons(await hoaDonRes.json());
      setPhieuThus(await phieuThuRes.json());
      setPhieuChis(await phieuChiRes.json());
    } catch (err) {
      setError(err.message || "Tải dữ liệu thất bại");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timerId = window.setTimeout(() => {
      loadAll();
    }, 0);
    return () => window.clearTimeout(timerId);
  }, []);

  const activeItems = useMemo(() => {
    if (activeTab === "phieuThu") return phieuThus;
    if (activeTab === "phieuChi") return phieuChis;
    return hoaDons;
  }, [activeTab, hoaDons, phieuChis, phieuThus]);

  const filteredItems = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    if (!keyword) return activeItems;
    return activeItems.filter((item) =>
      [
        item.maHoaDon,
        item.maPhieuThu,
        item.maPhieuChi,
        item.khachHangId,
        item.hopDongId,
        item.hoaDonId,
        item.trangThaiThanhToan,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(keyword)),
    );
  }, [activeItems, search]);

  const stats = useMemo(() => {
    const tongHoaDon = hoaDons.reduce(
      (sum, item) => sum + Number(item.tongTien || 0),
      0,
    );
    const tongThu = phieuThus.reduce(
      (sum, item) => sum + Number(item.soTien || 0),
      0,
    );
    return {
      hoaDon: hoaDons.length,
      phieuThu: phieuThus.length,
      phieuChi: phieuChis.length,
      conNo: Math.max(tongHoaDon - tongThu, 0),
    };
  }, [hoaDons, phieuChis, phieuThus]);

  const activeTabLabel = TABS.find((tab) => tab.id === activeTab)?.label;
  const khachHangOptions = useMemo(() => {
    const seen = new Set();
    return hoaDons.filter((hoaDon) => {
      const khachHangId = String(hoaDon.khachHangId ?? "");
      if (!khachHangId || seen.has(khachHangId)) return false;
      seen.add(khachHangId);
      return true;
    });
  }, [hoaDons]);

  const resetForms = () => {
    setHoaDonForm(emptyHoaDonForm);
    setPhieuThuForm(emptyPhieuThuForm);
    setPhieuChiForm(emptyPhieuChiForm);
    setEditing({ type: null, id: null });
  };

  const switchTab = (tab) => {
    setActiveTab(tab);
    setError("");
    setSuccess("");
    resetForms();
  };

  const handleHoaDonChange = (event) => {
    const { name, value } = event.target;
    setHoaDonForm((current) => ({ ...current, [name]: value }));
  };

  const handlePhieuThuChange = (event) => {
    const { name, value } = event.target;
    setPhieuThuForm((current) => ({ ...current, [name]: value }));
  };

  const handlePhieuThuHoaDonChange = (event) => {
    const { value } = event.target;
    const selectedHoaDon = hoaDons.find((hoaDon) => String(hoaDon.id) === value);
    setPhieuThuForm((current) => ({
      ...current,
      hoaDonId: value,
      khachHangId: selectedHoaDon?.khachHangId ? String(selectedHoaDon.khachHangId) : "",
    }));
  };

  const handlePhieuChiChange = (event) => {
    const { name, value } = event.target;
    setPhieuChiForm((current) => ({ ...current, [name]: value }));
  };

  const handlePhieuChiHoaDonChange = (event) => {
    const { value } = event.target;
    const selectedHoaDon = hoaDons.find((hoaDon) => String(hoaDon.id) === value);
    setPhieuChiForm((current) => ({
      ...current,
      hoaDonId: value,
      khachHangId: selectedHoaDon?.khachHangId ? String(selectedHoaDon.khachHangId) : "",
    }));
  };

  const validateHoaDon = () => {
    if (!hoaDonForm.maHoaDon.trim()) return "Mã hóa đơn không được rỗng";
    if (!hoaDonForm.khachHangId.trim()) return "Khách hàng ID không được rỗng";
    if (!hoaDonForm.tongTien.trim()) return "Tổng tiền không được rỗng";
    if (Number(hoaDonForm.tongTien) <= 0) return "Tổng tiền phải lớn hơn 0";
    return "";
  };

  const validatePhieuThu = () => {
    if (!phieuThuForm.maPhieuThu.trim()) return "Mã phiếu thu không được rỗng";
    if (!phieuThuForm.hoaDonId.trim()) return "Hóa đơn ID không được rỗng";
    if (!phieuThuForm.soTien.trim()) return "Số tiền không được rỗng";
    if (Number(phieuThuForm.soTien) <= 0) return "Số tiền phải lớn hơn 0";
    return "";
  };

  const validatePhieuChi = () => {
    if (!phieuChiForm.maPhieuChi.trim()) return "Mã phiếu chi không được rỗng";
    if (!phieuChiForm.hoaDonId.trim()) return "Hóa đơn ID không được rỗng";
    if (!phieuChiForm.soTien.trim()) return "Số tiền không được rỗng";
    if (Number(phieuChiForm.soTien) <= 0) return "Số tiền phải lớn hơn 0";
    return "";
  };

  const submitHoaDon = async () => {
    const payload = {
      maHoaDon: hoaDonForm.maHoaDon.trim(),
      hopDongId: toNumberOrNull(hoaDonForm.hopDongId),
      khachHangId: Number(hoaDonForm.khachHangId),
      tongTien: Number(hoaDonForm.tongTien),
      trangThaiThanhToan: hoaDonForm.trangThaiThanhToan,
    };
    const response = await fetch(
      editing.type === "hoaDon"
        ? `${API_BASE_URL}/api/hoa-don/${editing.id}`
        : `${API_BASE_URL}/api/hoa-don`,
      {
        method: editing.type === "hoaDon" ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      },
    );
    await throwIfRequestFailed(response, "Lưu hóa đơn thất bại");
  };

  const submitPhieuThu = async () => {
    const payload = {
      maPhieuThu: phieuThuForm.maPhieuThu.trim(),
      khachHangId: toNumberOrNull(phieuThuForm.khachHangId),
      hoaDonId: Number(phieuThuForm.hoaDonId),
      soTien: Number(phieuThuForm.soTien),
      nguoiLapId: toNumberOrNull(phieuThuForm.nguoiLapId),
    };
    const response = await fetch(
      editing.type === "phieuThu"
        ? `${API_BASE_URL}/api/phieu-thu/${editing.id}`
        : `${API_BASE_URL}/api/phieu-thu`,
      {
        method: editing.type === "phieuThu" ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      },
    );
    await throwIfRequestFailed(response, "Lưu phiếu thu thất bại");
  };

  const submitPhieuChi = async () => {
    const payload = {
      maPhieuChi: phieuChiForm.maPhieuChi.trim(),
      khachHangId: toNumberOrNull(phieuChiForm.khachHangId),
      hoaDonId: Number(phieuChiForm.hoaDonId),
      soTien: Number(phieuChiForm.soTien),
      nguoiLapId: toNumberOrNull(phieuChiForm.nguoiLapId),
    };
    const response = await fetch(
      editing.type === "phieuChi"
        ? `${API_BASE_URL}/api/phieu-chi/${editing.id}`
        : `${API_BASE_URL}/api/phieu-chi`,
      {
        method: editing.type === "phieuChi" ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      },
    );
    await throwIfRequestFailed(response, "Lưu phiếu chi thất bại");
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const validationMessage =
      activeTab === "hoaDon"
        ? validateHoaDon()
        : activeTab === "phieuThu"
          ? validatePhieuThu()
          : validatePhieuChi();

    if (validationMessage) {
      setError(validationMessage);
      setSuccess("");
      return;
    }

    setSubmitting(true);
    setError("");
    setSuccess("");

    try {
      if (activeTab === "hoaDon") await submitHoaDon();
      if (activeTab === "phieuThu") await submitPhieuThu();
      if (activeTab === "phieuChi") await submitPhieuChi();
      await loadAll();
      resetForms();
      setSuccess(editing.id ? "Cập nhật thành công" : "Tạo mới thành công");
    } catch (err) {
      setError(err.message || "Không thể lưu dữ liệu");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (item) => {
    setError("");
    setSuccess("");
    if (activeTab === "hoaDon") {
      setEditing({ type: "hoaDon", id: item.id });
      setHoaDonForm({
        maHoaDon: item.maHoaDon ?? "",
        hopDongId: item.hopDongId ?? "",
        khachHangId: item.khachHangId ?? "",
        tongTien: item.tongTien ?? "",
        trangThaiThanhToan: item.trangThaiThanhToan ?? "ChuaThanhToan",
      });
      return;
    }
    if (activeTab === "phieuThu") {
      setEditing({ type: "phieuThu", id: item.id });
      setPhieuThuForm({
        maPhieuThu: item.maPhieuThu ?? "",
        khachHangId: item.khachHangId ? String(item.khachHangId) : "",
        hoaDonId: item.hoaDonId ? String(item.hoaDonId) : "",
        soTien: item.soTien ?? "",
        nguoiLapId: item.nguoiLapId ?? "",
      });
      return;
    }
    setEditing({ type: "phieuChi", id: item.id });
    setPhieuChiForm({
      maPhieuChi: item.maPhieuChi ?? "",
      khachHangId: item.khachHangId ? String(item.khachHangId) : "",
      hoaDonId: item.hoaDonId ? String(item.hoaDonId) : "",
      soTien: item.soTien ?? "",
      nguoiLapId: item.nguoiLapId ?? "",
    });
  };

  const handleDelete = async (id) => {
    const labels = {
      hoaDon: "hóa đơn",
      phieuThu: "phiếu thu",
      phieuChi: "phiếu chi",
    };
    const confirmed = window.confirm(`Bạn có muốn xóa ${labels[activeTab]} này không?`);
    if (!confirmed) return;

    const endpoint = {
      hoaDon: "hoa-don",
      phieuThu: "phieu-thu",
      phieuChi: "phieu-chi",
    }[activeTab];

    try {
      const response = await fetch(`${API_BASE_URL}/api/${endpoint}/${id}`, {
        method: "DELETE",
      });
      await throwIfRequestFailed(response, "Xóa thất bại");
      await loadAll();
      if (editing.id === id && editing.type === activeTab) resetForms();
      setSuccess("Xóa thành công");
      setError("");
    } catch (err) {
      setError(err.message || "Không thể xóa dữ liệu");
      setSuccess("");
    }
  };

  const renderForm = () => {
    if (activeTab === "hoaDon") {
      return (
        <>
          <label>
            Mã hóa đơn
            <input
              name="maHoaDon"
              value={hoaDonForm.maHoaDon}
              onChange={handleHoaDonChange}
              placeholder="INV-2605-001"
            />
          </label>
          <div className="two-col">
            <label>
              Khách hàng ID
              <input
                name="khachHangId"
                type="number"
                min="1"
                value={hoaDonForm.khachHangId}
                onChange={handleHoaDonChange}
                placeholder="1"
              />
            </label>
            <label>
              Hợp đồng ID
              <input
                name="hopDongId"
                type="number"
                min="1"
                value={hoaDonForm.hopDongId}
                onChange={handleHoaDonChange}
                placeholder="1"
              />
            </label>
          </div>
          <label>
            Tổng tiền
            <input
              name="tongTien"
              type="number"
              min="1"
              value={hoaDonForm.tongTien}
              onChange={handleHoaDonChange}
              placeholder="25000000"
            />
          </label>
          <label>
            Trạng thái thanh toán
            <select
              name="trangThaiThanhToan"
              value={hoaDonForm.trangThaiThanhToan}
              onChange={handleHoaDonChange}
            >
              <option value="ChuaThanhToan">Chưa thanh toán</option>
              <option value="ThanhToan1Phan">Thanh toán 1 phần</option>
              <option value="HoanTat">Hoàn tất</option>
            </select>
          </label>
        </>
      );
    }

    if (activeTab === "phieuThu") {
      return (
        <>
          <label>
            Mã phiếu thu
            <input
              name="maPhieuThu"
              value={phieuThuForm.maPhieuThu}
              onChange={handlePhieuThuChange}
              placeholder="PT-0001"
            />
          </label>
          <div className="two-col">
            <label>
              Hóa đơn
              <select
                name="hoaDonId"
                value={phieuThuForm.hoaDonId}
                onChange={handlePhieuThuHoaDonChange}
              >
                <option value="">Chọn hóa đơn</option>
                {hoaDons.map((hoaDon) => (
                  <option key={hoaDon.id} value={hoaDon.id}>
                    {formatHoaDonOption(hoaDon)}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Khách hàng
              <select
                name="khachHangId"
                value={phieuThuForm.khachHangId}
                onChange={handlePhieuThuChange}
              >
                <option value="">Chọn khách hàng</option>
                {khachHangOptions.map((hoaDon) => (
                  <option key={hoaDon.khachHangId} value={hoaDon.khachHangId}>
                    {formatKhachHangOption(hoaDon)}
                  </option>
                ))}
              </select>
            </label>
          </div>
          <div className="two-col">
            <label>
              Số tiền
              <input
                name="soTien"
                type="number"
                min="1"
                value={phieuThuForm.soTien}
                onChange={handlePhieuThuChange}
                placeholder="10000000"
              />
            </label>
            <label>
              Người lập ID
              <input
                name="nguoiLapId"
                type="number"
                min="1"
                value={phieuThuForm.nguoiLapId}
                onChange={handlePhieuThuChange}
                placeholder="4"
              />
            </label>
          </div>
        </>
      );
    }

    return (
      <>
        <label>
          Mã phiếu chi
          <input
            name="maPhieuChi"
            value={phieuChiForm.maPhieuChi}
            onChange={handlePhieuChiChange}
            placeholder="PC-0001"
          />
        </label>
        <div className="two-col">
          <label>
            Hóa đơn
            <select
              name="hoaDonId"
              value={phieuChiForm.hoaDonId}
              onChange={handlePhieuChiHoaDonChange}
            >
              <option value="">Chọn hóa đơn</option>
              {hoaDons.map((hoaDon) => (
                <option key={hoaDon.id} value={hoaDon.id}>
                  {formatHoaDonOption(hoaDon)}
                </option>
              ))}
            </select>
          </label>
          <label>
            Khách hàng
            <select
              name="khachHangId"
              value={phieuChiForm.khachHangId}
              onChange={handlePhieuChiChange}
            >
              <option value="">Chọn khách hàng</option>
              {khachHangOptions.map((hoaDon) => (
                <option key={hoaDon.khachHangId} value={hoaDon.khachHangId}>
                  {formatKhachHangOption(hoaDon)}
                </option>
              ))}
            </select>
          </label>
        </div>
        <div className="two-col">
          <label>
            Số tiền
            <input
              name="soTien"
              type="number"
              min="1"
              value={phieuChiForm.soTien}
              onChange={handlePhieuChiChange}
              placeholder="1000000"
            />
          </label>
          <label>
            Người lập ID
            <input
              name="nguoiLapId"
              type="number"
              min="1"
              value={phieuChiForm.nguoiLapId}
              onChange={handlePhieuChiChange}
              placeholder="4"
            />
          </label>
        </div>
      </>
    );
  };

  const renderRows = () => {
    if (filteredItems.length === 0) {
      return (
        <tr>
          <td colSpan="9" className="empty-row">
            {loading ? "Đang tải dữ liệu..." : "Không có dữ liệu phù hợp"}
          </td>
        </tr>
      );
    }

    if (activeTab === "hoaDon") {
      return filteredItems.map((item) => (
        <tr key={item.id}>
          <td>{item.id}</td>
          <td>{item.maHoaDon}</td>
          <td>{item.khachHangId}</td>
          <td>{item.hopDongId ?? "-"}</td>
          <td>{formatMoney(item.tongTien)}</td>
          <td>{formatMoney(item.soTienDaThu)}</td>
          <td>
            <span className={`badge badge-${String(item.trangThaiThanhToan || "").toLowerCase()}`}>
              {PAYMENT_STATUS_LABEL[item.trangThaiThanhToan] || item.trangThaiThanhToan || "-"}
            </span>
          </td>
          <td>{formatDateTime(item.updatedAt)}</td>
          <td>
            <div className="row-actions">
              <button type="button" className="ghost-btn" onClick={() => handleEdit(item)}>
                Sửa
              </button>
              <button type="button" className="danger-btn" onClick={() => handleDelete(item.id)}>
                Xóa
              </button>
            </div>
          </td>
        </tr>
      ));
    }

    return filteredItems.map((item) => {
      const maPhieu = activeTab === "phieuThu" ? item.maPhieuThu : item.maPhieuChi;
      return (
        <tr key={item.id}>
          <td>{item.id}</td>
          <td>{maPhieu}</td>
          <td>{item.hoaDonId}</td>
          <td>{item.khachHangId ?? "-"}</td>
          <td>{formatMoney(item.soTien)}</td>
          <td>{item.nguoiLapId ?? "-"}</td>
          <td>{formatDateTime(item.ngayTao)}</td>
          <td>{formatDateTime(item.updatedAt)}</td>
          <td>
            <div className="row-actions">
              <button type="button" className="ghost-btn" onClick={() => handleEdit(item)}>
                Sửa
              </button>
              <button type="button" className="danger-btn" onClick={() => handleDelete(item.id)}>
                Xóa
              </button>
            </div>
          </td>
        </tr>
      );
    });
  };

  return (
    <main className="hopdong-page">
      <section className="hopdong-header">
        <div>
          <p className="eyebrow">CRM / Kế toán</p>
          <h1>Quản lý hóa đơn và thu chi</h1>
          <p className="subtitle">
            Theo dõi hóa đơn bán hàng, số tiền đã thu và các phiếu thu chi trong CRMOnline.
          </p>
        </div>

        <div className="toolbar">
          <input
            className="search"
            type="search"
            placeholder="Tìm theo mã, ID, trạng thái..."
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <button className="secondary-btn" type="button" onClick={loadAll}>
            Tải lại
          </button>
        </div>
      </section>

      <section className="stats-row">
        <article className="stat-card">
          <span>Hóa đơn</span>
          <strong>{stats.hoaDon}</strong>
        </article>
        <article className="stat-card">
          <span>Phiếu thu</span>
          <strong>{stats.phieuThu}</strong>
        </article>
        <article className="stat-card">
          <span>Phiếu chi</span>
          <strong>{stats.phieuChi}</strong>
        </article>
        <article className="stat-card">
          <span>Còn phải thu</span>
          <strong>{formatMoney(stats.conNo)}</strong>
        </article>
      </section>

      <section className="finance-tabs" aria-label="Chọn loại dữ liệu">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            type="button"
            className={`finance-tab${activeTab === tab.id ? " active" : ""}`}
            onClick={() => switchTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </section>

      <section className="content-grid">
        <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-head">
            <div>
              <h2>{editing.id ? "Cập nhật" : "Tạo mới"}</h2>
              <p>Biểu mẫu dành cho {activeTabLabel}.</p>
            </div>
            {editing.id ? (
              <button className="ghost-btn" type="button" onClick={resetForms}>
                Hủy sửa
              </button>
            ) : null}
          </div>

          {renderForm()}

          {error ? <div className="message error">{error}</div> : null}
          {success ? <div className="message success">{success}</div> : null}

          <div className="actions">
            <button className="primary-btn" type="submit" disabled={submitting}>
              {submitting ? "Đang lưu..." : editing.id ? "Cập nhật" : "Tạo mới"}
            </button>
            <button className="secondary-btn" type="button" onClick={resetForms}>
              Làm mới form
            </button>
          </div>
        </form>

        <section className="panel table-panel">
          <div className="panel-head">
            <div>
              <h2>Danh sách {activeTabLabel}</h2>
              <p>
                Hiển thị {filteredItems.length}/{activeItems.length} bản ghi.
              </p>
            </div>
            {loading ? <span className="loading">Đang tải...</span> : null}
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                {activeTab === "hoaDon" ? (
                  <tr>
                    <th>ID</th>
                    <th>Mã hóa đơn</th>
                    <th>Khách hàng</th>
                    <th>Hợp đồng</th>
                    <th>Tổng tiền</th>
                    <th>Đã thu</th>
                    <th>Trạng thái</th>
                    <th>Cập nhật</th>
                    <th>Hành động</th>
                  </tr>
                ) : (
                  <tr>
                    <th>ID</th>
                    <th>Mã phiếu</th>
                    <th>Hóa đơn</th>
                    <th>Khách hàng</th>
                    <th>Số tiền</th>
                    <th>Người lập</th>
                    <th>Ngày tạo</th>
                    <th>Cập nhật</th>
                    <th>Hành động</th>
                  </tr>
                )}
              </thead>
              <tbody>{renderRows()}</tbody>
            </table>
          </div>
        </section>
      </section>
    </main>
  );
}

export default TaiChinh;
