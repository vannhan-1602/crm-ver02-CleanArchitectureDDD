import { BrowserRouter as Router, Routes, Route, Link, Navigate, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

import { clearSession, getPermissions, setSession } from "./apiClient";
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

const APP_MODULES = [
  { path: "/leads", moduleKeys: ["LEAD"], label: "Quản lý Lead", element: <LeadManager /> },
  { path: "/khach-hang", moduleKeys: ["KHACH_HANG"], label: "Quản lý Khách hàng", element: <KhachHangManager /> },
  { path: "/hoat-dong", moduleKeys: ["HOAT_DONG"], label: "Quản lý Hoạt động", element: <HoatDongManager /> },
  { path: "/hop-dong", moduleKeys: ["HOP_DONG"], label: "Quản lý Hợp đồng", element: <HopDong /> },
  { path: "/tai-chinh", moduleKeys: ["HOA_DON", "TAI_CHINH"], label: "Quản lý Hóa đơn", element: <TaiChinh /> },
  { path: "/sanpham", moduleKeys: ["SAN_PHAM"], label: "Quản lý Sản phẩm", element: <SanPhamManager /> },
  { path: "/tickets", moduleKeys: ["TICKET"], label: "Quản lý Ticket", element: <TicketManager /> },
  { path: "/cohoi", moduleKeys: ["CO_HOI"], label: "Quản lý Cơ hội bán hàng", element: <CoHoiManager /> },
  { path: "/baogia", moduleKeys: ["BAO_GIA"], label: "Quản lý Báo giá", element: <BaoGia /> },
  { path: "/bao-cao-thong-ke", moduleKeys: ["BAO_CAO"], label: "Báo cáo thống kê", element: <BaoCaoThongKe /> },
];

function NoPermissionPage() {
  return (
    <div style={styles.noPermission}>
      <h2 style={styles.noPermissionTitle}>Bạn chưa được cấp quyền sử dụng module nào</h2>
      <p style={styles.noPermissionText}>Vui lòng liên hệ quản trị viên để được phân quyền.</p>
    </div>
  );
}

function hasAdminAccess(user) {
  return Boolean(user?.admin || user?.roleId === 1 || user?.roleName?.toLowerCase() === "admin");
}

function canUsePermission(permission) {
  return Boolean(permission?.canView && permission?.canRead && permission?.canWrite);
}

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
  const isAdmin = hasAdminAccess(currentUser);
  const userPermissions = Array.isArray(auth?.permissions) ? auth.permissions : getPermissions();

  const canUseModule = (moduleKeys = []) => {
    if (isAdmin) return true;
    return moduleKeys.some((moduleKey) => {
      const permission = userPermissions.find((item) => item.moduleKey === moduleKey);
      return canUsePermission(permission);
    });
  };

  const visibleModules = APP_MODULES.filter((module) => canUseModule(module.moduleKeys));
  const canManageUsers = Boolean(
    (isAdmin || currentUser?.roleName?.toLowerCase() === "manager") && canUseModule(["NHAN_VIEN"]),
  );
  const firstAllowedPath = visibleModules[0]?.path || (canManageUsers ? "/users" : "/no-permission");

  useEffect(() => {
    if (auth && window.location.pathname === "/login") {
      navigate(firstAllowedPath);
    }
  }, [auth, firstAllowedPath, navigate]);

  return (
    <div style={styles.shell}>
      <aside style={styles.sidebar}>
        <h2 style={styles.logo}>CRM SYSTEM</h2>
        <nav style={styles.nav}>
          {auth && (
            <>
              {visibleModules.map((module) => (
                <Link key={module.path} to={module.path} style={styles.navLink}>
                  {module.label}
                </Link>
              ))}
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
              <Route path="/" element={<Navigate to={firstAllowedPath} replace />} />
              <Route path="/login" element={<LoginPage onLogin={onLogin} />} />
              <Route path="/no-permission" element={<NoPermissionPage />} />
              <Route
                path="/users"
                element={canManageUsers ? <UserPermissionManager token={auth.token} /> : <Navigate to={firstAllowedPath} replace />}
              />
              {APP_MODULES.map((module) => (
                <Route
                  key={module.path}
                  path={module.path}
                  element={canUseModule(module.moduleKeys) ? module.element : <Navigate to={firstAllowedPath} replace />}
                />
              ))}
              <Route path="*" element={<Navigate to={firstAllowedPath} replace />} />
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
  noPermission: {
    minHeight: "calc(100vh - 140px)",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    textAlign: "center",
    color: "#374151",
  },
  noPermissionTitle: {
    margin: "0 0 8px",
    fontSize: "22px",
    color: "#111827",
  },
  noPermissionText: {
    margin: 0,
    color: "#6b7280",
  },
};

export default App;
