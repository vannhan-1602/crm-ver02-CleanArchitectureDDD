import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

import { clearSession, setSession } from "./apiClient";
import HopDong from "./page/HopDong";
import TaiChinh from "./page/TaiChinh";
import LeadManager from "./page/LeadManager";
import SanPhamManager from "./page/SanPhamManager.jsx";
import KhachHangManager from "./page/KhachHangManager.jsx";
import TicketManager from "./page/TicketManager.jsx";
import CoHoiManager from "./page/CoHoiManager.jsx";
import HoatDongManager from "./page/HoatDongManager.jsx";
import BaoGia from "./page/BaoGia.jsx";
import BaoCaoThongKe from "./page/BaoCaoThongKe.jsx";
import LoginPage from "./page/LoginPage.jsx";
import UserPermissionManager from "./page/UserPermissionManager.jsx";

function App() {
  const [auth, setAuth] = useState(() => {
    const raw = localStorage.getItem("crm-auth");
    return raw ? JSON.parse(raw) : null;
  });

  const handleLogin = (data) => {
    localStorage.setItem("crm-auth", JSON.stringify(data));
    setSession(data);
    setAuth(data);
  };

  const handleLogout = () => {
    localStorage.removeItem("crm-auth");
    clearSession();
    setAuth(null);
  };

  return (
    <Router>
      <AppShell auth={auth} onLogin={handleLogin} onLogout={handleLogout} />
    </Router>
  );
}

function AppShell({ auth, onLogin, onLogout }) {
  const navigate = useNavigate();
  const currentUser = auth?.user;
  const canManageUsers = Boolean(
    currentUser?.admin || currentUser?.roleId === 1 || currentUser?.roleName?.toLowerCase() === "admin",
  );

  useEffect(() => {
    if (auth && window.location.pathname === "/login") {
      navigate("/leads");
    }
  }, [auth, navigate]);

  return (
    <div style={styles.shell}>
      <aside style={styles.sidebar}>
        <h2 style={styles.logo}>CRM SYSTEM</h2>
        <nav style={styles.nav}>
          {auth && (
            <>
              <Link to="/leads" style={styles.navLink}>Quản lý Lead</Link>
              <Link to="/khach-hang" style={styles.navLink}>Quản lý Khách hàng</Link>
              <Link to="/hoat-dong" style={styles.navLink}>Quản lý Hoạt động</Link>
              <Link to="/hop-dong" style={styles.navLink}>Quản lý Hợp đồng</Link>
              <Link to="/tai-chinh" style={styles.navLink}>Quản lý Hóa đơn</Link>
              <Link to="/sanpham" style={styles.navLink}>Quản lý Sản phẩm</Link>
              <Link to="/tickets" style={styles.navLink}>Quản lý Ticket</Link>
              <Link to="/cohoi" style={styles.navLink}>Quản lý Cơ hội bán hàng</Link>
              <Link to="/baogia" style={styles.navLink}>Quản lý Báo giá</Link>
              <Link to="/bao-cao-thong-ke" style={styles.navLink}>Báo cáo thống kê</Link>
              {canManageUsers && <Link to="/users" style={styles.navLink}>Quản lý người dùng</Link>}
            </>
          )}
        </nav>
      </aside>

      <main style={styles.main}>
        <header style={styles.header}>
          <div style={styles.headerActions}>
            <span style={{ cursor: "pointer" }}>Thông báo</span>
            <span style={{ cursor: "pointer" }}>Cài đặt</span>
            {auth ? (
              <>
                <span>{auth.user?.hoTen || auth.user?.username} - {auth.user?.roleName || auth.user?.roleCode}</span>
                <button onClick={onLogout} style={styles.logoutButton}>Đăng xuất</button>
              </>
            ) : (
              <Link to="/login" style={styles.loginLink}>Đăng nhập</Link>
            )}
            <div style={styles.avatar}>
              {(auth?.user?.hoTen || auth?.user?.username || "A").charAt(0).toUpperCase()}
            </div>
          </div>
        </header>

        <div style={styles.content}>
          {!auth ? (
            <Routes>
              <Route path="*" element={<LoginPage onLogin={onLogin} />} />
            </Routes>
          ) : (
            <Routes>
              <Route path="/" element={<LeadManager />} />
              <Route path="/login" element={<LoginPage onLogin={onLogin} />} />
              <Route
                path="/users"
                element={canManageUsers ? <UserPermissionManager token={auth.token} /> : <LeadManager />}
              />
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
          )}
        </div>
      </main>
    </div>
  );
}

const styles = {
  shell: {
    display: "flex",
    minHeight: "100vh",
    fontFamily: "Arial, sans-serif",
  },
  sidebar: {
    width: "250px",
    backgroundColor: "#212b36",
    color: "#fff",
    padding: "20px",
    display: "flex",
    flexDirection: "column",
  },
  logo: {
    textAlign: "center",
    marginBottom: "30px",
    fontSize: "20px",
  },
  nav: {
    display: "flex",
    flexDirection: "column",
    gap: "15px",
  },
  navLink: {
    color: "#fff",
    textDecoration: "none",
    padding: "8px 10px",
    borderRadius: "6px",
  },
  main: {
    flex: 1,
    backgroundColor: "#f9fafc",
    display: "flex",
    flexDirection: "column",
    height: "100vh",
  },
  header: {
    height: "60px",
    backgroundColor: "#fff",
    borderBottom: "1px solid #ddd",
    display: "flex",
    justifyContent: "flex-end",
    alignItems: "center",
    padding: "0 20px",
  },
  headerActions: {
    display: "flex",
    gap: "15px",
    alignItems: "center",
  },
  loginLink: {
    color: "#2563eb",
    textDecoration: "none",
    fontWeight: 700,
  },
  logoutButton: {
    border: "1px solid #d1d5db",
    borderRadius: "6px",
    background: "#fff",
    color: "#374151",
    padding: "7px 10px",
    cursor: "pointer",
  },
  avatar: {
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
  },
  content: {
    padding: "20px",
    flex: 1,
    overflowY: "auto",
  },
};

export default App;
