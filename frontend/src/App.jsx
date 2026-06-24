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
  { path: "/leads", moduleKeys: ["LEAD"], label: "Quản lý Lead", icon: "👤", element: <LeadManager /> },
  { path: "/khach-hang", moduleKeys: ["KHACH_HANG"], label: "Quản lý Khách hàng", icon: "🏢", element: <KhachHangManager /> },
  { path: "/hoat-dong", moduleKeys: ["HOAT_DONG"], label: "Quản lý Hoạt động", icon: "📋", element: <HoatDongManager /> },
  { path: "/hop-dong", moduleKeys: ["HOP_DONG"], label: "Quản lý Hợp đồng", icon: "📄", element: <HopDong /> },
  { path: "/tai-chinh", moduleKeys: ["HOA_DON", "TAI_CHINH"], label: "Quản lý Hóa đơn", icon: "💰", element: <TaiChinh /> },
  { path: "/sanpham", moduleKeys: ["SAN_PHAM"], label: "Quản lý Sản phẩm", icon: "📦", element: <SanPhamManager /> },
  { path: "/tickets", moduleKeys: ["TICKET"], label: "Quản lý Ticket", icon: "🎫", element: <TicketManager /> },
  { path: "/cohoi", moduleKeys: ["CO_HOI"], label: "Cơ hội bán hàng", icon: "🎯", element: <CoHoiManager /> },
  { path: "/baogia", moduleKeys: ["BAO_GIA"], label: "Quản lý Báo giá", icon: "📊", element: <BaoGia /> },
  { path: "/bao-cao-thong-ke", moduleKeys: ["BAO_CAO"], label: "Báo cáo thống kê", icon: "📈", element: <BaoCaoThongKe /> },
];

function NoPermissionPage() {
  return (
      <div style={styles.noPermission}>
        <div style={styles.noPermissionIcon}>🔒</div>
        <h2 style={styles.noPermissionTitle}>Chưa được cấp quyền truy cập</h2>
        <p style={styles.noPermissionText}>Vui lòng liên hệ quản trị viên để được phân quyền.</p>
      </div>
  );
}

function hasAdminAccess(user) {
  return Boolean(user?.admin || user?.roleId === 1 || user?.roleName?.toLowerCase() === "admin");
}

function canUsePermission(permission) {
  return Boolean(permission?.canView || permission?.canRead || permission?.canWrite);
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
  const [currentPath, setCurrentPath] = useState(window.location.pathname);

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

  useEffect(() => {
    const handler = () => setCurrentPath(window.location.pathname);
    window.addEventListener("popstate", handler);
    return () => window.removeEventListener("popstate", handler);
  }, []);

  const handleNav = (path) => {
    navigate(path);
    setCurrentPath(path);
  };

  const displayName = auth?.user?.hoTen || auth?.user?.username || "Người dùng";
  const roleName = auth?.user?.roleName || auth?.user?.roleCode || "";

  return (
      <div style={styles.shell}>
        {/* ── Sidebar ── */}
        <aside style={styles.sidebar}>
          {/* Logo */}
          <div style={styles.logoWrap}>
            <div style={styles.logoIcon}>C</div>
            <div>
              <div style={styles.logoText}>CRM System</div>
              <div style={styles.logoSub}>Quản lý khách hàng</div>
            </div>
          </div>

          {/* Divider */}
          <div style={styles.divider} />

          {/* Nav */}
          <nav style={styles.nav}>
            {auth && (
                <>
                  <div style={styles.navSection}>MENU CHÍNH</div>
                  {visibleModules.map((module) => {
                    const isActive = currentPath === module.path;
                    return (
                        <button
                            key={module.path}
                            onClick={() => handleNav(module.path)}
                            style={{
                              ...styles.navItem,
                              ...(isActive ? styles.navItemActive : {}),
                            }}
                        >
                          <span style={styles.navIcon}>{module.icon}</span>
                          <span>{module.label}</span>
                          {isActive && <span style={styles.navActiveBar} />}
                        </button>
                    );
                  })}

                  {canManageUsers && (
                      <>
                        <div style={{ ...styles.navSection, marginTop: 16 }}>QUẢN TRỊ</div>
                        <button
                            onClick={() => handleNav("/users")}
                            style={{
                              ...styles.navItem,
                              ...(currentPath === "/users" ? styles.navItemActive : {}),
                            }}
                        >
                          <span style={styles.navIcon}>⚙️</span>
                          <span>Quản lý người dùng</span>
                        </button>
                      </>
                  )}
                </>
            )}
          </nav>

          {/* User card at bottom */}
          {auth && (
              <div style={styles.userCard}>
                <div style={styles.userAvatar}>
                  {displayName.charAt(0).toUpperCase()}
                </div>
                <div style={styles.userInfo}>
                  <div style={styles.userName}>{displayName}</div>
                  <div style={styles.userRole}>{roleName}</div>
                </div>
                <button onClick={onLogout} style={styles.logoutIconBtn} title="Đăng xuất">
                  ↩
                </button>
              </div>
          )}
        </aside>

        {/* ── Main ── */}
        <main style={styles.main}>
          {/* Header */}
          <header style={styles.header}>
            <div style={styles.headerLeft}>
              <div style={styles.breadcrumb}>
                {visibleModules.find((m) => m.path === currentPath)?.label || "Trang chủ"}
              </div>
            </div>
            <div style={styles.headerActions}>
              <button style={styles.headerBtn}>🔔</button>
              <button style={styles.headerBtn}>⚙</button>
              {!auth && (
                  <Link to="/login" style={styles.loginLink}>Đăng nhập</Link>
              )}
            </div>
          </header>

          {/* Content */}
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
    fontFamily: "'Inter', 'Segoe UI', Arial, sans-serif",
    background: "#f0f2f5",
  },

  /* ── Sidebar ── */
  sidebar: {
    width: "260px",
    minHeight: "100vh",
    backgroundColor: "#ffffff",
    borderRight: "1px solid #e8eaed",
    display: "flex",
    flexDirection: "column",
    boxShadow: "2px 0 8px rgba(0,0,0,0.04)",
    flexShrink: 0,
  },

  logoWrap: {
    display: "flex",
    alignItems: "center",
    gap: "12px",
    padding: "22px 20px 18px",
  },
  logoIcon: {
    width: "38px",
    height: "38px",
    borderRadius: "10px",
    background: "linear-gradient(135deg, #2563eb, #1d4ed8)",
    color: "#fff",
    fontWeight: 800,
    fontSize: "18px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexShrink: 0,
    boxShadow: "0 4px 12px rgba(37,99,235,0.3)",
  },
  logoText: {
    fontSize: "15px",
    fontWeight: 700,
    color: "#111827",
    lineHeight: 1.2,
  },
  logoSub: {
    fontSize: "11px",
    color: "#9ca3af",
    marginTop: "1px",
  },

  divider: {
    height: "1px",
    background: "#f0f2f5",
    margin: "0 16px 12px",
  },

  nav: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
    gap: "2px",
    padding: "0 10px",
    overflowY: "auto",
  },

  navSection: {
    fontSize: "10px",
    fontWeight: 700,
    color: "#9ca3af",
    letterSpacing: "0.1em",
    padding: "8px 10px 4px",
    textTransform: "uppercase",
  },

  navItem: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "9px 12px",
    borderRadius: "8px",
    border: "none",
    background: "transparent",
    color: "#4b5563",
    fontSize: "13px",
    fontWeight: 500,
    cursor: "pointer",
    textAlign: "left",
    width: "100%",
    position: "relative",
    transition: "background 0.15s, color 0.15s",
  },

  navItemActive: {
    background: "#eff6ff",
    color: "#2563eb",
    fontWeight: 600,
  },

  navIcon: {
    fontSize: "15px",
    width: "20px",
    textAlign: "center",
    flexShrink: 0,
  },

  navActiveBar: {
    position: "absolute",
    right: "0",
    top: "50%",
    transform: "translateY(-50%)",
    width: "3px",
    height: "20px",
    background: "#2563eb",
    borderRadius: "2px 0 0 2px",
  },

  /* User card */
  userCard: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "14px 16px",
    borderTop: "1px solid #f0f2f5",
    margin: "8px 0 0",
  },
  userAvatar: {
    width: "34px",
    height: "34px",
    borderRadius: "50%",
    background: "linear-gradient(135deg, #dbeafe, #bfdbfe)",
    color: "#1d4ed8",
    fontWeight: 700,
    fontSize: "14px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexShrink: 0,
    border: "2px solid #e0e7ff",
  },
  userInfo: {
    flex: 1,
    minWidth: 0,
  },
  userName: {
    fontSize: "13px",
    fontWeight: 600,
    color: "#111827",
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis",
  },
  userRole: {
    fontSize: "11px",
    color: "#6b7280",
    marginTop: "1px",
  },
  logoutIconBtn: {
    background: "none",
    border: "1px solid #e5e7eb",
    borderRadius: "6px",
    width: "28px",
    height: "28px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    cursor: "pointer",
    fontSize: "14px",
    color: "#6b7280",
    flexShrink: 0,
    transition: "background 0.15s",
  },

  /* ── Main ── */
  main: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
    minWidth: 0,
    minHeight: "100vh",
  },

  header: {
    height: "58px",
    backgroundColor: "#ffffff",
    borderBottom: "1px solid #e8eaed",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    padding: "0 24px",
    flexShrink: 0,
    boxShadow: "0 1px 4px rgba(0,0,0,0.04)",
  },

  headerLeft: {
    display: "flex",
    alignItems: "center",
    gap: "12px",
  },

  breadcrumb: {
    fontSize: "14px",
    fontWeight: 600,
    color: "#111827",
  },

  headerActions: {
    display: "flex",
    gap: "8px",
    alignItems: "center",
  },

  headerBtn: {
    background: "#f9fafb",
    border: "1px solid #e5e7eb",
    borderRadius: "8px",
    width: "36px",
    height: "36px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    cursor: "pointer",
    fontSize: "15px",
    color: "#6b7280",
    transition: "background 0.15s",
  },

  loginLink: {
    color: "#2563eb",
    textDecoration: "none",
    fontWeight: 700,
    fontSize: "14px",
  },

  content: {
    flex: 1,
    overflowY: "auto",
    background: "#f0f2f5",
  },

  /* No permission */
  noPermission: {
    minHeight: "calc(100vh - 58px)",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    textAlign: "center",
    color: "#374151",
    gap: "12px",
  },
  noPermissionIcon: {
    fontSize: "48px",
    marginBottom: "4px",
  },
  noPermissionTitle: {
    margin: 0,
    fontSize: "20px",
    fontWeight: 700,
    color: "#111827",
  },
  noPermissionText: {
    margin: 0,
    color: "#6b7280",
    fontSize: "14px",
  },
};

export default App;