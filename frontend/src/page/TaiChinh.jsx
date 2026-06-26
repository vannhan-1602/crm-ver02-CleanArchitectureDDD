import { useEffect, useMemo, useState } from "react";
import { authFetch, canAccessModule, canWriteModule } from "../apiClient";
import "./HopDong.css";
import { ActionIcon } from "../moduleIcons.jsx";

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

const PAYMENT_STATUS_OPTIONS = [
  { value: "all", label: "Tất cả" },
  { value: "ChuaThanhToan", label: PAYMENT_STATUS_LABEL.ChuaThanhToan },
  { value: "ThanhToan1Phan", label: PAYMENT_STATUS_LABEL.ThanhToan1Phan },
  { value: "HoanTat", label: PAYMENT_STATUS_LABEL.HoanTat },
];

const emptyThongKe = {
  tongHoaDon: 0,
  tongDaThu: 0,
  tongConNo: 0,
  tongPhieuThu: 0,
  tongPhieuChi: 0,
  soHoaDon: 0,
  soPhieuThu: 0,
  soPhieuChi: 0,
  soGiaoDich: 0,
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

function formatCustomerName(record) {
  if (record?.tenKhachHang) return record.tenKhachHang;
  if (record?.khachHangId) return `KH ${record.khachHangId}`;
  return "-";
}

function formatHoaDonOption(hoaDon) {
  return `#${hoaDon.id} - ${hoaDon.maHoaDon} - ${formatCustomerName(hoaDon)} - ${formatMoney(hoaDon.tongTien)}`;
}

function formatPhieuThuHoaDonOption(hoaDon) {
  return `${hoaDon.id} - ${hoaDon.maHoaDon}`;
}

function formatHopDongOption(hopDong) {
  return `${hopDong.maHopDong || `HD ${hopDong.id}`} - ${formatCustomerName(hopDong)}`;
}

function toNumberOrNull(value) {
  if (value === "" || value === null || value === undefined) return null;
  return Number(value);
}

async function throwIfRequestFailed(response, fallbackMessage) {
  if (response.ok) return;

  let message;
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

  throw new Error(`${message || fallbackMessage} (${response.status})`);
}

function TaiChinh() {
  const canAccessFinanceTab = (moduleKey) => canAccessModule(moduleKey === "HOA_DON" ? "HOA_DON" : "TAI_CHINH");
  const canWriteFinanceTab = (moduleKey) => canWriteModule(moduleKey === "HOA_DON" ? "HOA_DON" : "TAI_CHINH");

  const [activeTab, setActiveTab] = useState("hoaDon");
  const [hoaDons, setHoaDons] = useState([]);
  const [hopDongs, setHopDongs] = useState([]);
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
  const [paymentStatusFilter, setPaymentStatusFilter] = useState("all");
  const [thongKe, setThongKe] = useState(emptyThongKe);
  const [thongKeFilter, setThongKeFilter] = useState({ from: "", to: "" });
  const [thongKeLoading, setThongKeLoading] = useState(false);
  const [thongKeError, setThongKeError] = useState("");

  const loadAll = async () => {
    setLoading(true);
    setError("");
    try {
      const requests = [];

      if (canAccessFinanceTab("HOA_DON")) {
        requests.push(
          authFetch(`${API_BASE_URL}/api/hoa-don`).then(async (response) => {
            if (!response.ok) throw new Error(`Tải hóa đơn thất bại (${response.status})`);
            return { key: "hoaDons", data: await response.json() };
          }),
        );
      } else {
        setHoaDons([]);
      }

      if (canAccessModule("HOP_DONG")) {
        requests.push(
          authFetch(`${API_BASE_URL}/api/hop-dong`).then(async (response) => {
            if (!response.ok) throw new Error(`Tải hợp đồng thất bại (${response.status})`);
            return { key: "hopDongs", data: await response.json() };
          }),
        );
      } else {
        setHopDongs([]);
      }

      if (canAccessFinanceTab("PHIEU_THU")) {
        requests.push(
          authFetch(`${API_BASE_URL}/api/phieu-thu`).then(async (response) => {
            if (!response.ok) throw new Error(`Tải phiếu thu thất bại (${response.status})`);
            return { key: "phieuThus", data: await response.json() };
          }),
        );
      } else {
        setPhieuThus([]);
      }

      if (canAccessFinanceTab("PHIEU_CHI")) {
        requests.push(
          authFetch(`${API_BASE_URL}/api/phieu-chi`).then(async (response) => {
            if (!response.ok) throw new Error(`Tải phiếu chi thất bại (${response.status})`);
            return { key: "phieuChis", data: await response.json() };
          }),
        );
      } else {
        setPhieuChis([]);
      }

      const results = await Promise.all(requests);
      results.forEach((result) => {
        if (result.key === "hoaDons") setHoaDons(result.data);
        if (result.key === "hopDongs") setHopDongs(result.data);
        if (result.key === "phieuThus") setPhieuThus(result.data);
        if (result.key === "phieuChis") setPhieuChis(result.data);
      });
    } catch (err) {
      setError(err.message || "Tải dữ liệu thất bại");
    } finally {
      setLoading(false);
    }
  };

  const loadThongKe = async () => {
    setThongKeLoading(true);
    setThongKeError("");
    try {
      if (!canAccessModule("TAI_CHINH")) {
        setThongKe(emptyThongKe);
        setThongKeError("Bạn không có quyền xem thống kê tài chính");
        return;
      }
      const params = new URLSearchParams();
      if (thongKeFilter.from) params.set("from", thongKeFilter.from);
      if (thongKeFilter.to) params.set("to", thongKeFilter.to);
      const query = params.toString();
      const response = await authFetch(
        `${API_BASE_URL}/api/tai-chinh/thong-ke${query ? `?${query}` : ""}`,
      );
      await throwIfRequestFailed(response, "Tải thống kê tài chính thất bại");
      setThongKe({ ...emptyThongKe, ...(await response.json()) });
    } catch (err) {
      setThongKeError(err.message || "Không thể tải thống kê tài chính");
    } finally {
      setThongKeLoading(false);
    }
  };

  const reloadTaiChinhData = async () => {
    await Promise.all([loadAll(), loadThongKe()]);
  };

  useEffect(() => {
    const timerId = window.setTimeout(() => {
      reloadTaiChinhData();
    }, 0);
    return () => window.clearTimeout(timerId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const activeItems = useMemo(() => {
    if (activeTab === "phieuThu") return phieuThus;
    if (activeTab === "phieuChi") return phieuChis;
    if (paymentStatusFilter !== "all") {
      return hoaDons.filter((hoaDon) => hoaDon.trangThaiThanhToan === paymentStatusFilter);
    }
    return hoaDons;
  }, [activeTab, hoaDons, paymentStatusFilter, phieuChis, phieuThus]);

  const filteredItems = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    if (!keyword) return activeItems;
    return activeItems.filter((item) =>
      [
        item.maHoaDon,
        item.maPhieuThu,
        item.maPhieuChi,
        item.tenKhachHang,
        item.khachHangId,
        item.hopDongId,
        item.hoaDonId,
        item.trangThaiThanhToan,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(keyword)),
    );
  }, [activeItems, search]);

  const activeTabLabel = TABS.find((tab) => tab.id === activeTab)?.label;
  const canWriteActiveTab =
    (activeTab === "hoaDon" && canWriteFinanceTab("HOA_DON")) ||
    (activeTab === "phieuThu" && canWriteFinanceTab("PHIEU_THU")) ||
    (activeTab === "phieuChi" && canWriteFinanceTab("PHIEU_CHI"));
  const customerNameById = useMemo(() => {
    const names = new Map();
    hopDongs.forEach((hopDong) => {
      const khachHangId = String(hopDong.khachHangId ?? "");
      if (khachHangId && hopDong.tenKhachHang) {
        names.set(khachHangId, hopDong.tenKhachHang);
      }
    });
    hoaDons.forEach((hoaDon) => {
      const khachHangId = String(hoaDon.khachHangId ?? "");
      if (khachHangId && hoaDon.tenKhachHang) {
        names.set(khachHangId, hoaDon.tenKhachHang);
      }
    });
    return names;
  }, [hoaDons, hopDongs]);

  const getCustomerDisplayName = (khachHangId) => {
    if (!khachHangId) return "-";
    return customerNameById.get(String(khachHangId)) || `KH ${khachHangId}`;
  };

  const getHoaDonCustomerDisplayName = (hoaDonId, fallbackKhachHangId) => {
    const hoaDon = findHoaDonById(hoaDonId);
    return hoaDon ? formatCustomerName(hoaDon) : getCustomerDisplayName(fallbackKhachHangId);
  };

  const findHopDongById = (hopDongId) =>
    hopDongs.find((hopDong) => String(hopDong.id) === String(hopDongId));

  const findHoaDonById = (hoaDonId) =>
    hoaDons.find((hoaDon) => String(hoaDon.id) === String(hoaDonId));

  const selectedHoaDonHopDong = findHopDongById(hoaDonForm.hopDongId);
  const selectedPhieuThuHoaDon = findHoaDonById(phieuThuForm.hoaDonId);
  const selectedPhieuChiHoaDon = findHoaDonById(phieuChiForm.hoaDonId);

  const resetForms = () => {
    setHoaDonForm(emptyHoaDonForm);
    setPhieuThuForm(emptyPhieuThuForm);
    setPhieuChiForm(emptyPhieuChiForm);
    setEditing({ type: null, id: null });
  };

  const switchTab = (tab) => {
    if (
      (tab === "hoaDon" && !canAccessFinanceTab("HOA_DON")) ||
      (tab === "phieuThu" && !canAccessFinanceTab("PHIEU_THU")) ||
      (tab === "phieuChi" && !canAccessFinanceTab("PHIEU_CHI"))
    ) {
      setError("Bạn không có quyền truy cập tab này");
      setSuccess("");
      return;
    }
    setActiveTab(tab);
    setError("");
    setSuccess("");
    resetForms();
  };

  const handleThongKeFilterChange = (event) => {
    const { name, value } = event.target;
    setThongKeFilter((current) => ({ ...current, [name]: value }));
  };

  const handleHoaDonChange = (event) => {
    const { name, value } = event.target;
    setHoaDonForm((current) => ({ ...current, [name]: value }));
  };

  const handleHoaDonHopDongChange = (event) => {
    const { value } = event.target;
    const selectedHopDong = findHopDongById(value);
    setHoaDonForm((current) => ({
      ...current,
      hopDongId: value,
      khachHangId: selectedHopDong?.khachHangId ? String(selectedHopDong.khachHangId) : "",
    }));
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
    if (!hoaDonForm.hopDongId.trim()) return "Hợp đồng không được rỗng";
    if (!selectedHoaDonHopDong?.khachHangId) return "Hợp đồng chưa có khách hàng hợp lệ";
    if (!hoaDonForm.tongTien.trim()) return "Tổng tiền không được rỗng";
    if (Number(hoaDonForm.tongTien) <= 0) return "Tổng tiền phải lớn hơn 0";
    return "";
  };

  const validatePhieuThu = () => {
    if (!phieuThuForm.maPhieuThu.trim()) return "Mã phiếu thu không được rỗng";
    if (!phieuThuForm.hoaDonId.trim()) return "Hóa đơn ID không được rỗng";
    if (!selectedPhieuThuHoaDon?.khachHangId) return "Hóa đơn chưa có khách hàng hợp lệ";
    if (!phieuThuForm.soTien.trim()) return "Số tiền không được rỗng";
    if (Number(phieuThuForm.soTien) <= 0) return "Số tiền phải lớn hơn 0";
    return "";
  };

  const validatePhieuChi = () => {
    if (!phieuChiForm.maPhieuChi.trim()) return "Mã phiếu chi không được rỗng";
    if (!phieuChiForm.hoaDonId.trim()) return "Hóa đơn ID không được rỗng";
    if (!selectedPhieuChiHoaDon?.khachHangId) return "Hóa đơn chưa có khách hàng hợp lệ";
    if (!phieuChiForm.soTien.trim()) return "Số tiền không được rỗng";
    if (Number(phieuChiForm.soTien) <= 0) return "Số tiền phải lớn hơn 0";
    return "";
  };

  const submitHoaDon = async () => {
    const selectedHopDong = findHopDongById(hoaDonForm.hopDongId);
    const payload = {
      maHoaDon: hoaDonForm.maHoaDon.trim(),
      hopDongId: Number(hoaDonForm.hopDongId),
      khachHangId: Number(selectedHopDong?.khachHangId),
      tongTien: Number(hoaDonForm.tongTien),
      trangThaiThanhToan: hoaDonForm.trangThaiThanhToan,
    };
    const response = await authFetch(
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
    const selectedHoaDon = findHoaDonById(phieuThuForm.hoaDonId);
    const payload = {
      maPhieuThu: phieuThuForm.maPhieuThu.trim(),
      khachHangId: toNumberOrNull(selectedHoaDon?.khachHangId),
      hoaDonId: Number(phieuThuForm.hoaDonId),
      soTien: Number(phieuThuForm.soTien),
      nguoiLapId: toNumberOrNull(phieuThuForm.nguoiLapId),
    };
    const response = await authFetch(
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
    const selectedHoaDon = findHoaDonById(phieuChiForm.hoaDonId);
    const payload = {
      maPhieuChi: phieuChiForm.maPhieuChi.trim(),
      khachHangId: toNumberOrNull(selectedHoaDon?.khachHangId),
      hoaDonId: Number(phieuChiForm.hoaDonId),
      soTien: Number(phieuChiForm.soTien),
      nguoiLapId: toNumberOrNull(phieuChiForm.nguoiLapId),
    };
    const response = await authFetch(
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
    if (
      (activeTab === "hoaDon" && !canWriteFinanceTab("HOA_DON")) ||
      (activeTab === "phieuThu" && !canWriteFinanceTab("PHIEU_THU")) ||
      (activeTab === "phieuChi" && !canWriteFinanceTab("PHIEU_CHI"))
    ) {
      setError("Bạn không có quyền lưu dữ liệu ở tab này");
      setSuccess("");
      return;
    }
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
      await reloadTaiChinhData();
      resetForms();
      setSuccess(editing.id ? "Cập nhật thành công" : "Tạo mới thành công");
    } catch (err) {
      setError(err.message || "Không thể lưu dữ liệu");
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (item) => {
    if (
      (activeTab === "hoaDon" && !canWriteFinanceTab("HOA_DON")) ||
      (activeTab === "phieuThu" && !canWriteFinanceTab("PHIEU_THU")) ||
      (activeTab === "phieuChi" && !canWriteFinanceTab("PHIEU_CHI"))
    ) {
      setError("Bạn không có quyền sửa dữ liệu ở tab này");
      setSuccess("");
      return;
    }
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
    if (
      (activeTab === "hoaDon" && !canWriteFinanceTab("HOA_DON")) ||
      (activeTab === "phieuThu" && !canWriteFinanceTab("PHIEU_THU")) ||
      (activeTab === "phieuChi" && !canWriteFinanceTab("PHIEU_CHI"))
    ) {
      setError("Bạn không có quyền xóa dữ liệu ở tab này");
      setSuccess("");
      return;
    }
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
      const response = await authFetch(`${API_BASE_URL}/api/${endpoint}/${id}`, {
        method: "DELETE",
      });
      await throwIfRequestFailed(response, "Xóa thất bại");
      await reloadTaiChinhData();
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
              Hợp đồng
              <select
                name="hopDongId"
                value={hoaDonForm.hopDongId}
                onChange={handleHoaDonHopDongChange}
              >
                <option value="">Chọn hợp đồng</option>
                {hopDongs.map((hopDong) => (
                  <option key={hopDong.id} value={hopDong.id}>
                    {formatHopDongOption(hopDong)}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Khách hàng
              <input
                readOnly
                value={
                  selectedHoaDonHopDong
                    ? formatCustomerName(selectedHoaDonHopDong)
                    : getCustomerDisplayName(hoaDonForm.khachHangId)
                }
                placeholder="Chọn hợp đồng"
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
                    {formatPhieuThuHoaDonOption(hoaDon)}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Khách hàng
              <input
                readOnly
                value={
                  selectedPhieuThuHoaDon
                    ? formatCustomerName(selectedPhieuThuHoaDon)
                    : getCustomerDisplayName(phieuThuForm.khachHangId)
                }
                placeholder="Chọn hóa đơn"
              />
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
            <input
              readOnly
              value={
                selectedPhieuChiHoaDon
                  ? formatCustomerName(selectedPhieuChiHoaDon)
                  : getCustomerDisplayName(phieuChiForm.khachHangId)
              }
              placeholder="Chọn hóa đơn"
            />
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
          <td>{formatCustomerName(item)}</td>
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
            {canWriteActiveTab ? (
              <div className="row-actions">
                <button type="button" className="ghost-btn btn-icon" onClick={() => handleEdit(item)}>
                  <ActionIcon name="edit" /> S?a
                </button>
                <button type="button" className="danger-btn btn-icon" onClick={() => handleDelete(item.id)}>
                  <ActionIcon name="delete" /> X?a
                </button>
              </div>
            ) : null}
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
          <td>{getHoaDonCustomerDisplayName(item.hoaDonId, item.khachHangId)}</td>
          <td>{formatMoney(item.soTien)}</td>
          <td>{item.nguoiLapId ?? "-"}</td>
          <td>{formatDateTime(item.ngayTao)}</td>
          <td>{formatDateTime(item.updatedAt)}</td>
          <td>
            {canWriteActiveTab ? (
              <div className="row-actions">
                <button type="button" className="ghost-btn btn-icon" onClick={() => handleEdit(item)}>
                  <ActionIcon name="edit" /> S?a
                </button>
                <button type="button" className="danger-btn btn-icon" onClick={() => handleDelete(item.id)}>
                  <ActionIcon name="delete" /> X?a
                </button>
              </div>
            ) : null}
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
          <button className="secondary-btn btn-icon" type="button" onClick={reloadTaiChinhData}>
            <ActionIcon name="refresh" /> Tải lại
          </button>
        </div>
      </section>

      <section className="stats-controls" aria-label="Lọc thống kê tài chính">
        <label>
          Từ ngày
          <input
            type="date"
            name="from"
            value={thongKeFilter.from}
            onChange={handleThongKeFilterChange}
          />
        </label>
        <label>
          Đến ngày
          <input
            type="date"
            name="to"
            value={thongKeFilter.to}
            onChange={handleThongKeFilterChange}
          />
        </label>
        <button className="secondary-btn btn-icon" type="button" onClick={loadThongKe} disabled={thongKeLoading}>
          <ActionIcon name="refresh" />
          {thongKeLoading ? "Đang tải..." : "Tải thống kê"}
        </button>
        {thongKeError ? <span className="stats-error">{thongKeError}</span> : null}
      </section>

      <section className="stats-row">
        <article className="stat-card">
          <span>Hóa đơn</span>
          <strong>{thongKe.soHoaDon}</strong>
        </article>
        <article className="stat-card">
          <span>Phiếu thu</span>
          <strong>{thongKe.soPhieuThu}</strong>
        </article>
        <article className="stat-card">
          <span>Phiếu chi</span>
          <strong>{thongKe.soPhieuChi}</strong>
        </article>
        <article className="stat-card">
          <span>Còn phải thu</span>
          <strong>{formatMoney(thongKe.tongConNo)}</strong>
        </article>
        <article className="stat-card">
          <span>Tổng hóa đơn</span>
          <strong>{formatMoney(thongKe.tongHoaDon)}</strong>
        </article>
        <article className="stat-card">
          <span>Đã thu</span>
          <strong>{formatMoney(thongKe.tongDaThu)}</strong>
        </article>
        <article className="stat-card">
          <span>Tổng phiếu thu</span>
          <strong>{formatMoney(thongKe.tongPhieuThu)}</strong>
        </article>
        <article className="stat-card">
          <span>Tổng phiếu chi</span>
          <strong>{formatMoney(thongKe.tongPhieuChi)}</strong>
        </article>
        <article className="stat-card">
          <span>Giao dịch</span>
          <strong>{thongKe.soGiaoDich}</strong>
        </article>
      </section>

      <section className="finance-tabs" aria-label="Chọn loại dữ liệu">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            type="button"
            className={`finance-tab${activeTab === tab.id ? " active" : ""}`}
            onClick={() => switchTab(tab.id)}
            disabled={
              (tab.id === "hoaDon" && !canAccessFinanceTab("HOA_DON")) ||
              (tab.id === "phieuThu" && !canAccessFinanceTab("PHIEU_THU")) ||
              (tab.id === "phieuChi" && !canAccessFinanceTab("PHIEU_CHI"))
            }
          >
            {tab.label}
          </button>
        ))}
      </section>

      {activeTab === "hoaDon" ? (
        <section className="finance-filter" aria-label="Lọc trạng thái thanh toán">
          <label>
            Trạng thái thanh toán
            <select
              value={paymentStatusFilter}
              onChange={(event) => setPaymentStatusFilter(event.target.value)}
            >
              {PAYMENT_STATUS_OPTIONS.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </label>
        </section>
      ) : null}

      <section className="content-grid" style={!canWriteActiveTab ? { gridTemplateColumns: "1fr" } : undefined}>
        {canWriteActiveTab ? <form className="panel form-panel" onSubmit={handleSubmit}>
          <div className="panel-head">
            <div>
              <h2>{editing.id ? "Cập nhật" : "Tạo mới"}</h2>
              <p>Biểu mẫu dành cho {activeTabLabel}.</p>
            </div>
            {editing.id ? (
              <button className="ghost-btn btn-icon" type="button" onClick={resetForms}>
                <ActionIcon name="close" /> Hủy sửa
              </button>
            ) : null}
          </div>

          {renderForm()}

          {error ? <div className="message error">{error}</div> : null}
          {success ? <div className="message success">{success}</div> : null}

          <div className="actions">
            <button className="primary-btn btn-icon" type="submit" disabled={submitting}>
                      <ActionIcon name="save" />
              {submitting ? "Đang lưu..." : editing.id ? "Cập nhật" : "Tạo mới"}
            </button>
            <button className="secondary-btn btn-icon" type="button" onClick={resetForms}>
              Làm mới form
            </button>
          </div>
        </form> : null}

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
