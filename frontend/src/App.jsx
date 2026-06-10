import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

// Import các component/page
import HopDong from "./page/HopDong"; 
import LeadManager from "./components/LeadManager";
import SanPhamManager from "./page/SanPhamManager.jsx";

function App() {
  return (
    <Router>
      <div style={{ display: "flex", minHeight: "100vh", fontFamily: "Arial, sans-serif" }}>
        
        {/* THANH SIDEBAR (Bên trái) */}
        <aside style={{ width: "250px", backgroundColor: "#212b36", color: "#fff", padding: "20px", display: "flex", flexDirection: "column" }}>
          <h2 style={{ textAlign: "center", marginBottom: "30px", fontSize: "20px" }}>CRM SYSTEM</h2>
          <nav style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
            <Link to="/khach-hang" style={linkStyle}>Quản lý Khách hàng</Link>
            <Link to="/nhan-vien" style={linkStyle}>Quản lý Nhân viên</Link>
            <Link to="/leads" style={linkStyle}>Quản lý Lead</Link>
            <Link to="/hop-dong" style={linkStyle}>Quản lý Hợp đồng</Link>
              <Link to="/sanpham" style={linkStyle}>Quản lý Hợp đồng</Link>
          </nav>
        </aside>

        {/* KHU VỰC NỘI DUNG CHÍNH (Bên phải) */}
        <main style={{ flex: 1, backgroundColor: "#f9fafc", display: "flex", flexDirection: "column", height: "100vh" }}>
          
          {/* Header */}
          <header style={{ height: "60px", backgroundColor: "#fff", borderBottom: "1px solid #ddd", display: "flex", justifyContent: "flex-end", alignItems: "center", padding: "0 20px" }}>
            <div style={{ display: "flex", gap: "15px", alignItems: "center" }}>
               <span style={{ cursor: "pointer" }}>🔔</span>
               <span style={{ cursor: "pointer" }}>⚙️</span>
               <div style={{ width: "35px", height: "35px", borderRadius: "50%", backgroundColor: "#ccc", overflow: "hidden" }}>
                 <img src="https://via.placeholder.com/35" alt="avatar" style={{ width: "100%", height: "100%" }} />
               </div>
            </div>
          </header>

          {/* Nội dung sẽ thay đổi khi bạn bấm vào menu ở Sidebar */}
          <div style={{ padding: "20px", flex: 1, overflowY: "auto" }}>
            <Routes>
              <Route path="/leads" element={<LeadManager />} />
              <Route path="/hop-dong" element={<HopDong />} />
                <Route path="/sanpham" element={<SanPhamManager />} />
              {/* Trang mặc định khi vừa mở web lên */}
              <Route path="/" element={<LeadManager />} /> 
            </Routes>
          </div>

        </main>
      </div>
    </Router>
  );
}

// CSS cho các link ở Sidebar
const linkStyle = {
  color: "#b1b9c3",
  textDecoration: "none",
  padding: "10px",
  borderRadius: "4px",
  transition: "background-color 0.2s",
  display: "block"
};

export default App;