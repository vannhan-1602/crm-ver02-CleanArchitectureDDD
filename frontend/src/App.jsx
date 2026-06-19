import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom"

import HopDong from "./page/HopDong"
import TaiChinh from "./page/TaiChinh"
import LeadManager from "./page/LeadManager"
import SanPhamManager from "./page/SanPhamManager.jsx"
import KhachHangManager from "./page/KhachHangManager.jsx"
import TicketManager from "./page/TicketManager.jsx"
import CoHoiManager from "./page/CoHoiManager.jsx"
import HoatDongManager from "./page/HoatDongManager.jsx"
import BaoGia from "./page/BaoGia.jsx"
import BaoCaoThongKe from "./page/BaoCaoThongKe.jsx"

function App() {
  return (
    <Router>
      <div
        style={{
          display: "flex",
          minHeight: "100vh",
          fontFamily: "Arial, sans-serif",
        }}
      >
        <aside
          style={{
            width: "250px",
            backgroundColor: "#212b36",
            color: "#fff",
            padding: "20px",
            display: "flex",
            flexDirection: "column",
          }}
        >
          <h2
            style={{
              textAlign: "center",
              marginBottom: "30px",
              fontSize: "20px",
            }}
          >
            CRM SYSTEM
          </h2>
          <nav style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
            <Link to="/leads" style={linkStyle}>
              Quản lý Lead
            </Link>
            <Link to="/khach-hang" style={linkStyle}>
              Quản lý Khách hàng
            </Link>
            <Link to="/hoat-dong" style={linkStyle}>
              Quản lý Hoạt động
            </Link>
            <Link to="/hop-dong" style={linkStyle}>
              Quản lý Hợp đồng
            </Link>
            <Link to="/tai-chinh" style={linkStyle}>
              Quản lý Hóa đơn
            </Link>
            <Link to="/sanpham" style={linkStyle}>
              Quản lý Sản phẩm
            </Link>
            <Link to="/tickets" style={linkStyle}>
              Quản lý Ticket
            </Link>
            <Link to="/cohoi" style={linkStyle}>
              Quản lý Cơ Hội Bán Hàng
            </Link>
            <Link to="/baogia" style={linkStyle}>
              Quản lý Báo giá
            </Link>
            <Link to="/bao-cao-thong-ke" style={linkStyle}>
              Báo cáo thống kê
            </Link>
          </nav>
        </aside>

        <main
          style={{
            flex: 1,
            backgroundColor: "#f9fafc",
            display: "flex",
            flexDirection: "column",
            height: "100vh",
          }}
        >
          <header
            style={{
              height: "60px",
              backgroundColor: "#fff",
              borderBottom: "1px solid #ddd",
              display: "flex",
              justifyContent: "flex-end",
              alignItems: "center",
              padding: "0 20px",
            }}
          >
            <div style={{ display: "flex", gap: "15px", alignItems: "center" }}>
              <span style={{ cursor: "pointer" }}>Thông báo</span>
              <span style={{ cursor: "pointer" }}>Cài đặt</span>
              <div
                style={{
                  width: "35px",
                  height: "35px",
                  borderRadius: "50%",
                  background: "linear-gradient(135deg, #7c8db5, #d6dde8)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  color: "#fff",
                  fontWeight: 700,
                  overflow: "hidden",
                }}
              >
                A
              </div>
            </div>
          </header>

          <div style={{ padding: "20px", flex: 1, overflowY: "auto" }}>
            <Routes>
              <Route path="/" element={<LeadManager />} />
              <Route path="/leads" element={<LeadManager />} />
              <Route path="/khach-hang" element={<KhachHangManager />} />
              <Route path="/hop-dong" element={<HopDong />} />
              <Route path="/tai-chinh" element={<TaiChinh />} />
              <Route path="/sanpham" element={<SanPhamManager />} />
              <Route path="/tickets" element={<TicketManager />} />
              <Route path="/cohoi" element={<CoHoiManager />} />
              <Route path="/baogia" element={<BaoGia />} />
              <Route path="/bao-cao-thong-ke" element={<BaoCaoThongKe />} />
              <Route path="/hoat-dong" element={<HoatDongManager />} />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  )
}

const linkStyle = {
  color: "#b1b9c3",
  textDecoration: "none",
  padding: "10px",
  borderRadius: "4px",
  transition: "background-color 0.2s",
  display: "block",
}

export default App
