import { useEffect, useMemo, useState } from "react";
import { api as ax, canWriteModule } from "../apiClient";
import "./HopDong.css";
import "./TicketManager.css";
import "./ManagerForm.css";
import { ActionIcon } from "../moduleIcons.jsx";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

const PRIORITIES = [
  ["Thap", "Thấp"],
  ["TrungBinh", "Trung bình"],
  ["Cao", "Cao"],
  ["KhanCap", "Khẩn cấp"],
];

const SOURCES = [
  ["Email", "Email"],
  ["Phone", "Điện thoại"],
  ["Web", "Web"],
  ["Zalo", "Zalo"],
  ["TrucTiep", "Trực tiếp"],
];

const STATUSES = [
  ["Moi", "Mới"],
  ["DangXuLy", "Đang xử lý"],
  ["ChoPhanHoi", "Chờ phản hồi"],
  ["Dong", "Đóng"],
];

const FEEDBACK_TYPES = [
  ["NoiBoXuLy", "Nội bộ xử lý"],
  ["PhanHoiKhachHang", "Phản hồi khách hàng"],
  ["YeuCauBoSung", "Yêu cầu bổ sung"],
  ["DongTicket", "Đóng ticket"],
];

const EMPTY_TICKET = {
  maTicket: "",
  tieuDe: "",
  moTa: "",
  fileDinhKem: "",
  loaiTicketId: "",
  khachHangId: "",
  hopDongId: "",
  sanPhamId: "",
  mucDoUuTien: "TrungBinh",
  nguonTiepNhan: "Phone",
  trangThai: "Moi",
  nhanVienTiepNhanId: "",
  nhanVienXuLyId: "",
  ngayHenXuLy: "",
  ngayDong: "",
  lyDoDong: "",
};

const EMPTY_FEEDBACK = {
  nguoiPhanHoiId: "",
  loaiPhanHoi: "NoiBoXuLy",
  noiDung: "",
  fileDinhKem: "",
  trangThaiSau: "",
};

const toNumberOrNull = (value) => (value === "" || value == null ? null : Number(value));
const labelOf = (options, value) => options.find(([key]) => key === value)?.[1] ?? value ?? "-";

function toDateTimeInput(value) {
  if (!value) return "";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "";
  return new Date(date.getTime() - date.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
}

function formatDateTime(value) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat("vi-VN", { dateStyle: "short", timeStyle: "short" }).format(date);
}

export default function TicketManager() {
  const canWriteTickets = canWriteModule("TICKET");
  const [tickets, setTickets] = useState([]);
  const [loaiTickets, setLoaiTickets] = useState([]);
  const [khachHangList, setKhachHangList] = useState([]);
  const [hopDongList, setHopDongList] = useState([]);
  const [nhanVienList, setNhanVienList] = useState([]);
  const [sanPhamList, setSanPhamList] = useState([]);
  const [feedbackList, setFeedbackList] = useState([]);
  const [form, setForm] = useState(EMPTY_TICKET);
  const [feedbackForm, setFeedbackForm] = useState(EMPTY_FEEDBACK);
  const [editingId, setEditingId] = useState(null);
  const [selectedTicket, setSelectedTicket] = useState(null);
  const [search, setSearch] = useState("");
  const [filterStatus, setFilterStatus] = useState("");
  const [filterPriority, setFilterPriority] = useState("");
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [uploadingAttachment, setUploadingAttachment] = useState({ ticket: false, feedback: false });
  const [message, setMessage] = useState({ type: "", text: "" });

  const khachHangMap = useMemo(
    () => new Map(khachHangList.map((item) => [String(item.id), item.tenKhachHang ?? `KH #${item.id}`])),
    [khachHangList],
  );
  const nhanVienMap = useMemo(
    () => new Map(nhanVienList.map((item) => [String(item.id), item.hoTen ?? item.tenNhanVien ?? `NV #${item.id}`])),
    [nhanVienList],
  );
  const loaiTicketMap = useMemo(
    () => new Map(loaiTickets.map((item) => [String(item.id), item.tenLoai])),
    [loaiTickets],
  );
  const hopDongMap = useMemo(
    () => new Map(hopDongList.map((item) => [String(item.id), item])),
    [hopDongList],
  );

  const formatHopDongLabel = (hopDong) => {
    if (!hopDong) return "";
    const maHopDong = hopDong.maHopDong || `HD #${hopDong.id}`;
    const tenKhachHang = hopDong.tenKhachHang || khachHangMap.get(String(hopDong.khachHangId));
    return tenKhachHang ? `${maHopDong} - ${tenKhachHang}` : maHopDong;
  };
  const availableHopDongs = useMemo(
    () => hopDongList.filter((item) => !form.khachHangId || String(item.khachHangId) === String(form.khachHangId)),
    [form.khachHangId, hopDongList],
  );

  const loadTickets = async () => {
    setLoading(true);
    try {
      const res = await ax.get("/api/tickets");
      setTickets(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      setMessage({ type: "error", text: `Không thể tải ticket (${err.response?.status ?? err.message})` });
    } finally {
      setLoading(false);
    }
  };

  const loadLookups = async () => {
    const [loai, khachHang, hopDong, nhanVien, sanPham] = await Promise.allSettled([
      ax.get("/api/loai-ticket"),
      ax.get("/api/khach-hang"),
      ax.get("/api/hop-dong"),
      ax.get("/api/nhan-vien"),
      ax.get("/api/sanpham"),
    ]);
    if (loai.status === "fulfilled") setLoaiTickets(Array.isArray(loai.value.data) ? loai.value.data : []);
    if (khachHang.status === "fulfilled") setKhachHangList(Array.isArray(khachHang.value.data) ? khachHang.value.data : []);
    if (hopDong.status === "fulfilled") setHopDongList(Array.isArray(hopDong.value.data) ? hopDong.value.data : []);
    if (nhanVien.status === "fulfilled") setNhanVienList(Array.isArray(nhanVien.value.data) ? nhanVien.value.data : []);
    if (sanPham.status === "fulfilled") setSanPhamList(Array.isArray(sanPham.value.data) ? sanPham.value.data : []);
  };

  const loadFeedback = async (ticketId) => {
    if (!ticketId) {
      setFeedbackList([]);
      return;
    }
    try {
      const res = await ax.get("/api/ticket-phan-hoi", { params: { ticketId } });
      setFeedbackList(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      setMessage({ type: "error", text: `Không thể tải phản hồi (${err.response?.status ?? err.message})` });
    }
  };

  useEffect(() => {
    loadTickets();
    loadLookups();
  }, []);

  const filteredTickets = useMemo(() => {
    const keyword = search.trim().toLowerCase();
    return tickets.filter((ticket) => {
      const matchKeyword =
        !keyword ||
        [ticket.maTicket, ticket.tieuDe, ticket.moTa]
          .filter(Boolean)
          .some((value) => String(value).toLowerCase().includes(keyword));
      return (
        matchKeyword &&
        (!filterStatus || ticket.trangThai === filterStatus) &&
        (!filterPriority || ticket.mucDoUuTien === filterPriority)
      );
    });
  }, [tickets, search, filterStatus, filterPriority]);

  const stats = useMemo(
    () => ({
      total: tickets.length,
      moi: tickets.filter((item) => item.trangThai === "Moi").length,
      dangXuLy: tickets.filter((item) => item.trangThai === "DangXuLy").length,
      khanCap: tickets.filter((item) => item.mucDoUuTien === "KhanCap").length,
    }),
    [tickets],
  );

  const selectedTicketFresh = selectedTicket
    ? tickets.find((item) => item.id === selectedTicket.id) ?? selectedTicket
    : null;

  const resetForm = () => {
    setForm(EMPTY_TICKET);
    setEditingId(null);
    setMessage({ type: "", text: "" });
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleHopDongChange = (event) => {
    const { value } = event.target;
    const hopDong = hopDongMap.get(String(value));
    setForm((prev) => ({
      ...prev,
      hopDongId: value,
      khachHangId: hopDong?.khachHangId ? String(hopDong.khachHangId) : prev.khachHangId,
    }));
  };

  const handleKhachHangChange = (event) => {
    const { value } = event.target;
    setForm((prev) => {
      const currentHopDong = hopDongMap.get(String(prev.hopDongId));
      return {
        ...prev,
        khachHangId: value,
        hopDongId: currentHopDong && String(currentHopDong.khachHangId) !== String(value) ? "" : prev.hopDongId,
      };
    });
  };

  const handleFeedbackChange = (event) => {
    const { name, value } = event.target;
    setFeedbackForm((prev) => ({ ...prev, [name]: value }));
  };

  const uploadAttachment = async (file, target) => {
    if (!file) return;

    const data = new FormData();
    data.append("file", file);
    setUploadingAttachment((prev) => ({ ...prev, [target]: true }));
    try {
      const res = await ax.post("/api/tickets/attachments", data, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      const url = res.data?.url || "";
      if (!url) throw new Error("Upload khong tra ve duong dan file");
      if (target === "ticket") {
        setForm((prev) => ({ ...prev, fileDinhKem: url }));
      } else {
        setFeedbackForm((prev) => ({ ...prev, fileDinhKem: url }));
      }
      setMessage({ type: "success", text: "Đã tải file đính kèm lên." });
    } catch (err) {
      setMessage({ type: "error", text: `Không thể tải file đính kèm (${err.response?.status ?? err.message})` });
    } finally {
      setUploadingAttachment((prev) => ({ ...prev, [target]: false }));
    }
  };

  const clearAttachment = (target) => {
    if (target === "ticket") {
      setForm((prev) => ({ ...prev, fileDinhKem: "" }));
    } else {
      setFeedbackForm((prev) => ({ ...prev, fileDinhKem: "" }));
    }
  };

  const validate = () => {
    if (!form.maTicket.trim()) return "Mã ticket không được rỗng";
    if (!form.tieuDe.trim()) return "Tiêu đề không được rỗng";
    if (!form.khachHangId) return "Vui lòng chọn khách hàng";
    if (form.trangThai === "Dong" && !form.lyDoDong.trim()) return "Vui lòng nhập lý do đóng ticket";
    return "";
  };

  const ticketPayload = () => ({
    maTicket: form.maTicket.trim(),
    tieuDe: form.tieuDe.trim(),
    moTa: form.moTa.trim() || null,
    fileDinhKem: form.fileDinhKem.trim() || null,
    loaiTicketId: toNumberOrNull(form.loaiTicketId),
    khachHangId: toNumberOrNull(form.khachHangId),
    hopDongId: toNumberOrNull(form.hopDongId),
    sanPhamId: toNumberOrNull(form.sanPhamId),
    mucDoUuTien: form.mucDoUuTien,
    nguonTiepNhan: form.nguonTiepNhan,
    trangThai: form.trangThai,
    nhanVienTiepNhanId: toNumberOrNull(form.nhanVienTiepNhanId),
    nhanVienXuLyId: toNumberOrNull(form.nhanVienXuLyId),
    ngayHenXuLy: form.ngayHenXuLy || null,
    ngayDong: form.ngayDong || null,
    lyDoDong: form.lyDoDong.trim() || null,
  });

  const handleSubmit = async (event) => {
    event.preventDefault();
    const err = validate();
    if (err) {
      setMessage({ type: "error", text: err });
      return;
    }
    setSubmitting(true);
    try {
      if (editingId) {
        await ax.put(`/api/tickets/${editingId}`, ticketPayload());
      } else {
        await ax.post("/api/tickets", ticketPayload());
      }
      await loadTickets();
      resetForm();
      setMessage({ type: "success", text: editingId ? "Cập nhật ticket thành công" : "Thêm ticket thành công" });
    } catch (err2) {
      setMessage({ type: "error", text: `Không thể lưu ticket (${err2.response?.status ?? err2.message})` });
    } finally {
      setSubmitting(false);
    }
  };

  const handleEdit = (ticket) => {
    setEditingId(ticket.id);
    setForm({
      maTicket: ticket.maTicket ?? "",
      tieuDe: ticket.tieuDe ?? "",
      moTa: ticket.moTa ?? "",
      fileDinhKem: ticket.fileDinhKem ?? "",
      loaiTicketId: ticket.loaiTicketId ?? "",
      khachHangId: ticket.khachHangId ?? "",
      hopDongId: ticket.hopDongId ?? "",
      sanPhamId: ticket.sanPhamId ?? "",
      mucDoUuTien: ticket.mucDoUuTien ?? "TrungBinh",
      nguonTiepNhan: ticket.nguonTiepNhan ?? "Phone",
      trangThai: ticket.trangThai ?? "Moi",
      nhanVienTiepNhanId: ticket.nhanVienTiepNhanId ?? "",
      nhanVienXuLyId: ticket.nhanVienXuLyId ?? "",
      ngayHenXuLy: toDateTimeInput(ticket.ngayHenXuLy),
      ngayDong: toDateTimeInput(ticket.ngayDong),
      lyDoDong: ticket.lyDoDong ?? "",
    });
    setMessage({ type: "", text: "" });
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (ticket) => {
    if (!window.confirm(`Bạn có chắc muốn xóa ticket "${ticket.maTicket}" không?`)) return;
    try {
      await ax.delete(`/api/tickets/${ticket.id}`);
      await loadTickets();
      if (selectedTicket?.id === ticket.id) {
        setSelectedTicket(null);
        setFeedbackList([]);
      }
      setMessage({ type: "success", text: "Xóa ticket thành công" });
    } catch (err) {
      setMessage({ type: "error", text: `Không thể xóa ticket (${err.response?.status ?? err.message})` });
    }
  };

  const selectTicket = async (ticket) => {
    setSelectedTicket(ticket);
    setFeedbackForm(EMPTY_FEEDBACK);
    await loadFeedback(ticket.id);
  };

  const handleFeedbackSubmit = async (event) => {
    event.preventDefault();
    if (!selectedTicketFresh) return;
    if (!feedbackForm.noiDung.trim()) {
      setMessage({ type: "error", text: "Nội dung phản hồi không được rỗng" });
      return;
    }
    try {
      await ax.post("/api/ticket-phan-hoi", {
        ticketId: selectedTicketFresh.id,
        nguoiPhanHoiId: toNumberOrNull(feedbackForm.nguoiPhanHoiId),
        loaiPhanHoi: feedbackForm.loaiPhanHoi,
        noiDung: feedbackForm.noiDung.trim(),
        fileDinhKem: feedbackForm.fileDinhKem.trim() || null,
        trangThaiSau: feedbackForm.trangThaiSau || null,
      });
      setFeedbackForm(EMPTY_FEEDBACK);
      await Promise.all([loadTickets(), loadFeedback(selectedTicketFresh.id)]);
      setMessage({ type: "success", text: "Thêm phản hồi thành công" });
    } catch (err) {
      setMessage({ type: "error", text: `Không thể thêm phản hồi (${err.response?.status ?? err.message})` });
    }
  };

  return (
    <main className="hopdong-page ticket-page">
      <section className="hopdong-header">
        <div>
          <p className="eyebrow">CRM / Ticket</p>
          <h1>Quản lý Ticket</h1>
          <p className="subtitle">Tiếp nhận, phân công xử lý và ghi nhận phản hồi theo từng ticket.</p>
        </div>
        <div className="toolbar">
          <input className="search" type="search" placeholder="Tìm mã, tiêu đề, mô tả..." value={search} onChange={(e) => setSearch(e.target.value)} />
          <select className="search ticket-filter" value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
            <option value="">Tất cả trạng thái</option>
            {STATUSES.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
          </select>
          <select className="search ticket-filter" value={filterPriority} onChange={(e) => setFilterPriority(e.target.value)}>
            <option value="">Tất cả ưu tiên</option>
            {PRIORITIES.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
          </select>
          <button className="secondary-btn btn-icon" type="button" onClick={loadTickets}><ActionIcon name="refresh" /> Tải lại</button>
        </div>
      </section>

      <section className="stats-row">
        <article className="stat-card"><span>Tổng ticket</span><strong>{stats.total}</strong></article>
        <article className="stat-card"><span>Mới</span><strong>{stats.moi}</strong></article>
        <article className="stat-card"><span>Đang xử lý</span><strong>{stats.dangXuLy}</strong></article>
        <article className="stat-card"><span>Khẩn cấp</span><strong>{stats.khanCap}</strong></article>
      </section>

      <section className="content-grid ticket-grid" style={!canWriteTickets ? { gridTemplateColumns: "1fr" } : undefined}>
        {canWriteTickets ? <form className="panel form-panel ticket-form" onSubmit={handleSubmit}>
          <div className={`panel-head form-panel-head ${editingId ? "is-edit" : ""}`}>
            <div className="form-title-wrap">
              <div className="form-title-icon" aria-hidden="true">{editingId ? "✎" : "+"}</div>
              <div>
                <span className="form-mode-badge">{editingId ? "Đang chỉnh sửa" : "Tạo mới"}</span>
              <h2>{editingId ? "Cập nhật ticket" : "Thêm ticket mới"}</h2>
                <p>Nhập thông tin tiếp nhận, phân công và xử lý.</p>
              </div>
            </div>
            {editingId ? <button className="ghost-btn form-cancel-btn btn-icon" type="button" onClick={resetForm}><ActionIcon name="close" /> Hủy sửa</button> : null}
          </div>

          <div className="manager-form-body">
            <div className="form-section">
              <div className="section-title">Thông tin ticket</div>
              <div className="two-col">
                <label className="field">Mã ticket <span className="ticket-req">*</span>
                  <input name="maTicket" value={form.maTicket} onChange={handleChange} disabled={Boolean(editingId)} placeholder="TK-0001" />
                </label>
                <label className="field">Loại ticket
                  <select name="loaiTicketId" value={form.loaiTicketId} onChange={handleChange}>
                    <option value="">-- Chọn loại --</option>
                    {loaiTickets.map((item) => <option key={item.id} value={item.id}>{item.tenLoai}</option>)}
                  </select>
                </label>
              </div>

              <label className="field">Tiêu đề <span className="ticket-req">*</span>
                <input name="tieuDe" value={form.tieuDe} onChange={handleChange} placeholder="Tóm tắt vấn đề" />
              </label>

              <label className="field">Mô tả
                <textarea name="moTa" value={form.moTa} onChange={handleChange} placeholder="Mô tả chi tiết nội dung cần xử lý" />
              </label>
            </div>

            <div className="form-section">
              <div className="section-title">Liên kết</div>
              <div className="two-col">
                <label className="field">Khách hàng <span className="ticket-req">*</span>
                  <select name="khachHangId" value={form.khachHangId} onChange={handleKhachHangChange}>
                    <option value="">-- Chọn khách hàng --</option>
                    {khachHangList.map((item) => <option key={item.id} value={item.id}>{item.tenKhachHang ?? `KH #${item.id}`}</option>)}
                  </select>
                </label>
                <label className="field">Sản phẩm
                  <select name="sanPhamId" value={form.sanPhamId} onChange={handleChange}>
                    <option value="">-- Chọn sản phẩm --</option>
                    {sanPhamList.map((item) => {
                      const id = item.sanPhamId ?? item.id;
                      return <option key={id} value={id}>{item.tenSanPham ?? item.tenSP ?? `SP #${id}`}</option>;
                    })}
                  </select>
                </label>
              </div>

              <div className="two-col">
                <label className="field">Hợp đồng
                  <select name="hopDongId" value={form.hopDongId} onChange={handleHopDongChange}>
                    <option value="">-- Chọn hợp đồng --</option>
                    {availableHopDongs.map((item) => (
                      <option key={item.id} value={item.id}>
                        {formatHopDongLabel(item)}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="field">File đính kèm
                  <div className="ticket-attachment-control">
                    <input
                      type="file"
                      onChange={(event) => uploadAttachment(event.target.files?.[0], "ticket")}
                    />
                    {uploadingAttachment.ticket ? (
                      <span className="ticket-attachment-status">Đang tải file...</span>
                    ) : form.fileDinhKem ? (
                      <div className="ticket-attachment-preview">
                        <a href={`${API_BASE_URL}${form.fileDinhKem}`} target="_blank" rel="noreferrer">
                          {form.fileDinhKem.split("/").pop()}
                        </a>
                        <button type="button" className="ghost-btn btn-icon" onClick={() => clearAttachment("ticket")}>
                          <ActionIcon name="close" /> Bỏ file
                        </button>
                      </div>
                    ) : (
                      <span className="ticket-attachment-status">Chưa chọn file</span>
                    )}
                  </div>
                </label>
              </div>
            </div>

            <div className="form-section">
              <div className="section-title">Phân loại và xử lý</div>
              <div className="two-col">
                <label className="field">Mức độ ưu tiên
                  <select name="mucDoUuTien" value={form.mucDoUuTien} onChange={handleChange}>
                    {PRIORITIES.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                  </select>
                </label>
                <label className="field">Nguồn tiếp nhận
                  <select name="nguonTiepNhan" value={form.nguonTiepNhan} onChange={handleChange}>
                    {SOURCES.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                  </select>
                </label>
              </div>

              <div className="two-col">
                <label className="field">Trạng thái
                  <select name="trangThai" value={form.trangThai} onChange={handleChange}>
                    {STATUSES.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                  </select>
                </label>
                <label className="field">Ngày hẹn xử lý
                  <input name="ngayHenXuLy" type="datetime-local" value={form.ngayHenXuLy} onChange={handleChange} />
                </label>
              </div>
            </div>

            <div className="form-section">
              <div className="section-title">Nhân sự</div>
              <div className="two-col">
                <label className="field">NV tiếp nhận
                  <select name="nhanVienTiepNhanId" value={form.nhanVienTiepNhanId} onChange={handleChange}>
                    <option value="">-- Chọn nhân viên --</option>
                    {nhanVienList.map((item) => <option key={item.id} value={item.id}>{item.hoTen ?? item.tenNhanVien ?? `NV #${item.id}`}</option>)}
                  </select>
                </label>
                <label className="field">NV xử lý
                  <select name="nhanVienXuLyId" value={form.nhanVienXuLyId} onChange={handleChange}>
                    <option value="">-- Chọn nhân viên --</option>
                    {nhanVienList.map((item) => <option key={item.id} value={item.id}>{item.hoTen ?? item.tenNhanVien ?? `NV #${item.id}`}</option>)}
                  </select>
                </label>
              </div>
            </div>

            <div className="form-section">
              <div className="section-title">Đóng ticket</div>
              <div className="two-col">
                <label className="field">Ngày đóng
                  <input name="ngayDong" type="datetime-local" value={form.ngayDong} onChange={handleChange} />
                </label>
                <label className="field">Lý do đóng
                  <input name="lyDoDong" value={form.lyDoDong} onChange={handleChange} placeholder="Nhập khi đóng ticket" />
                </label>
              </div>
            </div>

            {message.text ? <div className={`message ${message.type}`}>{message.text}</div> : null}

            <div className="actions">
              <button className="secondary-btn btn-icon" type="button" onClick={resetForm}><ActionIcon name="refresh" /> Làm mới</button>
              <button className="primary-btn btn-icon" type="submit" disabled={submitting}>
                      <ActionIcon name="save" />{submitting ? "Đang lưu..." : editingId ? "Cập nhật ticket" : "Thêm ticket"}</button>
            </div>
          </div>
        </form> : null}

        <section className="ticket-main-column">
          <section className="panel table-panel">
            <div className="panel-head">
              <div>
                <h2>Danh sách ticket</h2>
                <p>Hiển thị {filteredTickets.length}/{tickets.length} bản ghi.</p>
              </div>
              {loading ? <span className="loading">Đang tải...</span> : null}
            </div>

            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Mã</th>
                    <th>Tiêu đề</th>
                    <th>Khách hàng</th>
                    <th>Ưu tiên</th>
                    <th>Trạng thái</th>
                    <th>NV xử lý</th>
                    <th>Hẹn xử lý</th>
                    <th>Hành động</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredTickets.length === 0 ? (
                    <tr><td colSpan="8" className="empty-row">{loading ? "Đang tải dữ liệu..." : "Không có dữ liệu phù hợp"}</td></tr>
                  ) : filteredTickets.map((ticket) => (
                    <tr key={ticket.id} className={selectedTicketFresh?.id === ticket.id ? "ticket-selected-row" : ""}>
                      <td><span className="ticket-code">{ticket.maTicket}</span></td>
                      <td>
                        <div className="ticket-title-cell">
                          <strong>{ticket.tieuDe}</strong>
                          <span>{loaiTicketMap.get(String(ticket.loaiTicketId)) ?? "Chưa phân loại"}</span>
                        </div>
                      </td>
                      <td>{khachHangMap.get(String(ticket.khachHangId)) ?? `KH #${ticket.khachHangId}`}</td>
                      <td><span className={`badge ticket-priority-${ticket.mucDoUuTien}`}>{labelOf(PRIORITIES, ticket.mucDoUuTien)}</span></td>
                      <td><span className={`badge ticket-status-${ticket.trangThai}`}>{labelOf(STATUSES, ticket.trangThai)}</span></td>
                      <td>{ticket.nhanVienXuLyId ? nhanVienMap.get(String(ticket.nhanVienXuLyId)) ?? `NV #${ticket.nhanVienXuLyId}` : "-"}</td>
                      <td>{formatDateTime(ticket.ngayHenXuLy)}</td>
                      <td>
                        <div className="row-actions">
                          <button type="button" className="ghost-btn btn-icon" onClick={() => selectTicket(ticket)}><ActionIcon name="reply" /> Phản hồi</button>
                          {canWriteTickets ? (
                            <>
                              <button type="button" className="ghost-btn btn-icon" onClick={() => handleEdit(ticket)}><ActionIcon name="edit" /> Sửa</button>
                              <button type="button" className="danger-btn btn-icon" onClick={() => handleDelete(ticket)}><ActionIcon name="delete" /> Xóa</button>
                            </>
                          ) : null}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>

          <section className="panel ticket-feedback-panel">
            <div className="panel-head">
              <div>
                <h2>Phản hồi ticket</h2>
                <p>{selectedTicketFresh ? `${selectedTicketFresh.maTicket} - ${selectedTicketFresh.tieuDe}` : "Chọn một ticket để xem và thêm phản hồi."}</p>
              </div>
            </div>

            {selectedTicketFresh ? (
              <>
                {canWriteTickets ? <form className="ticket-feedback-form" onSubmit={handleFeedbackSubmit}>
                  <div className="two-col">
                    <label>Người phản hồi
                      <select name="nguoiPhanHoiId" value={feedbackForm.nguoiPhanHoiId} onChange={handleFeedbackChange}>
                        <option value="">-- Chọn nhân viên --</option>
                        {nhanVienList.map((item) => <option key={item.id} value={item.id}>{item.hoTen ?? item.tenNhanVien ?? `NV #${item.id}`}</option>)}
                      </select>
                    </label>
                    <label>Loại phản hồi
                      <select name="loaiPhanHoi" value={feedbackForm.loaiPhanHoi} onChange={handleFeedbackChange}>
                        {FEEDBACK_TYPES.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </label>
                  </div>
                  <div className="two-col">
                    <label>Chuyển trạng thái
                      <select name="trangThaiSau" value={feedbackForm.trangThaiSau} onChange={handleFeedbackChange}>
                        <option value="">-- Giữ nguyên --</option>
                        {STATUSES.map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </label>
                    <label>File đính kèm
                      <div className="ticket-attachment-control">
                        <input
                          type="file"
                          onChange={(event) => uploadAttachment(event.target.files?.[0], "feedback")}
                        />
                        {uploadingAttachment.feedback ? (
                          <span className="ticket-attachment-status">?ang t?i file...</span>
                        ) : feedbackForm.fileDinhKem ? (
                          <div className="ticket-attachment-preview">
                            <a href={`${API_BASE_URL}${feedbackForm.fileDinhKem}`} target="_blank" rel="noreferrer">
                              {feedbackForm.fileDinhKem.split("/").pop()}
                            </a>
                            <button type="button" className="ghost-btn btn-icon" onClick={() => clearAttachment("feedback")}>
                              <ActionIcon name="close" /> B? file
                            </button>
                          </div>
                        ) : (
                          <span className="ticket-attachment-status">Ch?a ch?n file</span>
                        )}
                      </div>
                    </label>
                  </div>
                  <label>Nội dung phản hồi <span className="ticket-req">*</span>
                    <textarea name="noiDung" value={feedbackForm.noiDung} onChange={handleFeedbackChange} placeholder="Nhập nội dung xử lý hoặc phản hồi khách hàng" />
                  </label>
                  <button className="primary-btn btn-icon" type="submit"><ActionIcon name="add" /> Thêm phản hồi</button>
                </form> : null}

                <div className="ticket-timeline">
                  {feedbackList.length === 0 ? (
                    <div className="empty-row">Ticket chưa có phản hồi.</div>
                  ) : feedbackList.map((item) => (
                    <article className="ticket-timeline-item" key={item.id}>
                      <div>
                        <strong>{labelOf(FEEDBACK_TYPES, item.loaiPhanHoi)}</strong>
                        <span>{formatDateTime(item.createdAt)}</span>
                      </div>
                      <p>{item.noiDung}</p>
                      <small>
                        {item.nguoiPhanHoiId ? nhanVienMap.get(String(item.nguoiPhanHoiId)) ?? `NV #${item.nguoiPhanHoiId}` : "Không rõ người phản hồi"}
                        {item.trangThaiSau ? ` · ${labelOf(STATUSES, item.trangThaiTruoc)} → ${labelOf(STATUSES, item.trangThaiSau)}` : ""}
                      </small>
                    </article>
                  ))}
                </div>
              </>
            ) : (
              <div className="ticket-empty-feedback">Chọn nút “Phản hồi” ở một ticket trong bảng để thao tác.</div>
            )}
          </section>
        </section>
      </section>
    </main>
  );
}
