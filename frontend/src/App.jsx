import { useEffect, useMemo, useState } from "react"
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from "react-router-dom"

import { api, clearSession, getCurrentUser, getPermissions, setSession } from "./apiClient"
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

const NAV_ITEMS = [
  { to: "/leads", label: "Quản lý Lead", moduleKey: "LEAD", element: <LeadManager /> },
  { to: "/khach-hang", label: "Quản lý Khách hàng", moduleKey: "KHACH_HANG", element: <KhachHangManager /> },
  { to: "/hoat-dong", label: "Quản lý Hoạt động", moduleKey: "HOAT_DONG", element: <HoatDongManager /> },
  { to: "/hop-dong", label: "Quản lý Hợp đồng", moduleKey: "HOP_DONG", element: <HopDong /> },
  { to: "/tai-chinh", label: "Quản lý Hóa đơn", moduleKey: "TAI_CHINH", element: <TaiChinh /> },
  { to: "/sanpham", label: "Quản lý Sản phẩm", moduleKey: "SAN_PHAM", element: <SanPhamManager /> },
  { to: "/tickets", label: "Quản lý Ticket", moduleKey: "TICKET", element: <TicketManager /> },
  { to: "/cohoi", label: "Quản lý Cơ hội", moduleKey: "CO_HOI", element: <CoHoiManager /> },
  { to: "/baogia", label: "Quản lý Báo giá", moduleKey: "BAO_GIA", element: <BaoGia /> },
  { to: "/bao-cao-thong-ke", label: "Báo cáo thống kê", moduleKey: "BAO_CAO", element: <BaoCaoThongKe /> },
]

function canOpen(user, permissions, moduleKey) {
  if (user?.admin || user?.roleId === 1 || user?.roleName === "Admin") return true
  const permission = permissions.find((p) => p.moduleKey === moduleKey)
  return Boolean(permission?.canView || permission?.canRead || permission?.canWrite)
}

function App() {
  const [user, setUser] = useState(() => getCurrentUser())
  const [permissions, setPermissions] = useState(() => getPermissions())

  const visibleItems = useMemo(
    () => NAV_ITEMS.filter((item) => canOpen(user, permissions, item.moduleKey)),
    [user, permissions],
  )

  const handleLogin = (loginResult) => {
    setSession(loginResult)
    setUser(loginResult.user)
    setPermissions(loginResult.permissions ?? [])
  }

  const handleLogout = async () => {
    try {
      await api.post("/api/auth/logout")
    } catch {
      // Client-side logout is enough for the stateless token flow.
    }
    clearSession()
    setUser(null)
    setPermissions([])
  }

  if (!user) {
    return <LoginScreen onLogin={handleLogin} />
  }

  return (
    <Router>
      <div style={{ display: "flex", minHeight: "100vh", fontFamily: "Arial, sans-serif" }}>
        <aside style={sidebarStyle}>
          <h2 style={{ textAlign: "center", marginBottom: 30, fontSize: 20 }}>CRM SYSTEM</h2>
          <nav style={{ display: "flex", flexDirection: "column", gap: 12 }}>
            {visibleItems.map((item) => (
              <Link key={item.to} to={item.to} style={linkStyle}>
                {item.label}
              </Link>
            ))}
            {(user.admin || user.roleId === 1 || user.roleName === "Admin") && (
              <Link to="/admin/users" style={linkStyle}>
                Quản trị tài khoản
              </Link>
            )}
          </nav>
        </aside>

        <main style={{ flex: 1, backgroundColor: "#f9fafc", display: "flex", flexDirection: "column", height: "100vh" }}>
          <header style={headerStyle}>
            <div style={{ display: "flex", gap: 15, alignItems: "center" }}>
              <span>{user.username}</span>
              <span style={roleBadgeStyle}>{user.roleName || `Role ${user.roleId}`}</span>
              <button style={logoutButtonStyle} type="button" onClick={handleLogout}>
                Đăng xuất
              </button>
            </div>
          </header>

          <div style={{ padding: 20, flex: 1, overflowY: "auto" }}>
            <Routes>
              <Route path="/" element={<Navigate to={visibleItems[0]?.to || "/admin/users"} replace />} />
              {NAV_ITEMS.map((item) => (
                <Route
                  key={item.to}
                  path={item.to}
                  element={canOpen(user, permissions, item.moduleKey) ? item.element : <Forbidden />}
                />
              ))}
              <Route
                path="/admin/users"
                element={(user.admin || user.roleId === 1 || user.roleName === "Admin") ? <AdminUsers /> : <Forbidden />}
              />
            </Routes>
          </div>
        </main>
      </div>
    </Router>
  )
}

function LoginScreen({ onLogin }) {
  const [form, setForm] = useState({ username: "admin", password: "123456" })
  const [error, setError] = useState("")
  const [loading, setLoading] = useState(false)

  const submit = async (event) => {
    event.preventDefault()
    setLoading(true)
    setError("")
    try {
      const response = await api.post("/api/auth/login", form)
      onLogin(response.data)
    } catch (err) {
      setError(err.response?.data?.message || "Đăng nhập thất bại")
    } finally {
      setLoading(false)
    }
  }

  return (
    <main style={loginPageStyle}>
      <form onSubmit={submit} style={loginCardStyle}>
        <h1 style={{ margin: 0, fontSize: 28 }}>CRM SYSTEM</h1>
        <p style={{ marginTop: 8, color: "#5d6b82" }}>Đăng nhập để tiếp tục</p>
        <label style={fieldStyle}>
          Tên đăng nhập
          <input style={inputStyle} value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
        </label>
        <label style={fieldStyle}>
          Mật khẩu
          <input style={inputStyle} type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        </label>
        {error && <div style={errorStyle}>{error}</div>}
        <button style={primaryButtonStyle} disabled={loading}>
          {loading ? "Đang đăng nhập..." : "Đăng nhập"}
        </button>
      </form>
    </main>
  )
}

function AdminUsers() {
  const readSelectedUserId = () => {
    const raw = localStorage.getItem("crm_admin_selected_user_id")
    return raw ? Number(raw) : null
  }

  const readPermissionDrafts = () => {
    try {
      const raw = localStorage.getItem("crm_admin_permissions_drafts")
      if (!raw) return {}
      const drafts = JSON.parse(raw)
      return drafts && typeof drafts === "object" ? drafts : {}
    } catch {
      return {}
    }
  }

  const writePermissionDrafts = (drafts) => {
    localStorage.setItem("crm_admin_permissions_drafts", JSON.stringify(drafts))
  }

  const [users, setUsers] = useState([])
  const [modules, setModules] = useState({})
  const [selectedUserId, setSelectedUserId] = useState(() => readSelectedUserId())
  const [permissions, setPermissions] = useState([])
  const [permissionDrafts, setPermissionDrafts] = useState(() => readPermissionDrafts())
  const [savingPermissions, setSavingPermissions] = useState(false)
  const [form, setForm] = useState({ username: "", password: "", roleId: 3, hoTen: "", email: "", soDienThoai: "" })
  const [message, setMessage] = useState("")

  useEffect(() => {
    if (!selectedUserId) return

    const draft = permissionDrafts[selectedUserId]
    if (Array.isArray(draft)) {
      setPermissions(draft)
      return
    }

    const loadSelectedPermissions = async () => {
      try {
        const response = await api.get(`/api/admin/users/${selectedUserId}/permissions`)
        setPermissions(response.data)
      } catch {
        // Leave the current view alone; load() below still populates users/modules.
      }
    }

    loadSelectedPermissions()
  }, [selectedUserId, permissionDrafts])

  const load = async () => {
    const [usersRes, modulesRes] = await Promise.all([api.get("/api/admin/users"), api.get("/api/admin/modules")])
    setUsers(usersRes.data)
    setModules(modulesRes.data)
  }

  useEffect(() => {
    load()
  }, [])

  const selectUser = async (id) => {
    setSelectedUserId(id)
    localStorage.setItem("crm_admin_selected_user_id", String(id))
    setMessage("")

    const draft = permissionDrafts[id]
    if (Array.isArray(draft)) {
      setPermissions(draft)
      return
    }

    try {
      const response = await api.get(`/api/admin/users/${id}/permissions`)
      setPermissions(response.data)
    } catch (err) {
      setMessage(err.response?.data?.message || `Không thể tải phân quyền (${err.response?.status ?? err.message})`)
    }
  }

  const createUser = async (event) => {
    event.preventDefault()
    try {
      await api.post("/api/admin/users", { ...form, roleId: Number(form.roleId) })
      setForm({ username: "", password: "", roleId: 3, hoTen: "", email: "", soDienThoai: "" })
      setMessage("Đã tạo tài khoản")
      await load()
    } catch (err) {
      setMessage(err.response?.data?.message || `Không thể tạo tài khoản (${err.response?.status ?? err.message})`)
    }
  }

  const permissionFor = (moduleKey) =>
    permissions.find((permission) => permission.moduleKey === moduleKey) ?? { moduleKey, canView: false, canRead: false, canWrite: false }

  const togglePermission = (moduleKey, field) => {
    const current = permissionFor(moduleKey)
    const next = { ...current, [field]: !current[field] }
    setPermissions((items) => {
      const updated = [...items.filter((item) => item.moduleKey !== moduleKey), next]
      if (selectedUserId) {
        setPermissionDrafts((prev) => {
          const nextDrafts = { ...prev, [selectedUserId]: updated }
          writePermissionDrafts(nextDrafts)
          return nextDrafts
        })
      }
      return updated
    })
  }

  const savePermissions = async () => {
    if (!selectedUserId) {
      setMessage("Vui lòng chọn một tài khoản trước")
      return
    }

    setSavingPermissions(true)
    setMessage("")
    try {
      await api.put(`/api/admin/users/${selectedUserId}/permissions`, { permissions })
      const response = await api.get(`/api/admin/users/${selectedUserId}/permissions`)
      setPermissions(response.data)
      setPermissionDrafts((prev) => {
        const nextDrafts = { ...prev }
        delete nextDrafts[selectedUserId]
        writePermissionDrafts(nextDrafts)
        return nextDrafts
      })
      setMessage("Đã cập nhật phân quyền")
    } catch (err) {
      setMessage(err.response?.data?.message || `Không thể lưu phân quyền (${err.response?.status ?? err.message})`)
    } finally {
      setSavingPermissions(false)
    }
  }

  return (
    <main style={{ display: "grid", gridTemplateColumns: "360px 1fr", gap: 20 }}>
      <section style={panelStyle}>
        <h2>Tạo tài khoản</h2>
        <form onSubmit={createUser} style={{ display: "grid", gap: 12 }}>
          <input style={inputStyle} placeholder="Họ tên" value={form.hoTen} onChange={(e) => setForm({ ...form, hoTen: e.target.value })} />
          <input style={inputStyle} placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          <input style={inputStyle} placeholder="Số điện thoại" value={form.soDienThoai} onChange={(e) => setForm({ ...form, soDienThoai: e.target.value })} />
          <input style={inputStyle} placeholder="Username" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
          <input style={inputStyle} placeholder="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          <select style={inputStyle} value={form.roleId} onChange={(e) => setForm({ ...form, roleId: e.target.value })}>
            <option value={1}>Admin</option>
            <option value={2}>Manager</option>
            <option value={3}>Sale</option>
            <option value={4}>Accountant</option>
          </select>
          <button style={primaryButtonStyle}>Tạo tài khoản</button>
        </form>
      </section>

      <section style={panelStyle}>
        <h2>Phân quyền theo user</h2>
        {message && <p style={{ color: "#166534" }}>{message}</p>}
        <div style={{ display: "flex", gap: 10, flexWrap: "wrap", marginBottom: 16 }}>
          {users.map((item) => (
            <button
              key={item.id}
              type="button"
              style={item.id === selectedUserId ? selectedUserButtonStyle : userButtonStyle}
              onClick={() => selectUser(item.id)}
            >
              {item.username} - {item.roleName || item.roleId}
            </button>
          ))}
        </div>
        {selectedUserId && (
          <>
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  <th style={thStyle}>Module</th>
                  <th style={thStyle}>Xem</th>
                  <th style={thStyle}>Đọc</th>
                  <th style={thStyle}>Viết</th>
                </tr>
              </thead>
              <tbody>
                {[...new Set(Object.values(modules))].map((moduleKey) => {
                  const permission = permissionFor(moduleKey)
                  return (
                    <tr key={moduleKey}>
                      <td style={tdStyle}>{moduleKey}</td>
                      {["canView", "canRead", "canWrite"].map((field) => (
                        <td key={field} style={tdStyle}>
                          <input type="checkbox" checked={Boolean(permission[field])} onChange={() => togglePermission(moduleKey, field)} />
                        </td>
                      ))}
                    </tr>
                  )
                })}
              </tbody>
            </table>
            <button
              style={{ ...primaryButtonStyle, marginTop: 16 }}
              type="button"
              onClick={savePermissions}
              disabled={savingPermissions}
            >
              {savingPermissions ? "Đang lưu..." : "Lưu phân quyền"}
            </button>
          </>
        )}
      </section>
    </main>
  )
}

function Forbidden() {
  return <div style={panelStyle}>Bạn không có quyền truy cập module này.</div>
}

const sidebarStyle = { width: 250, backgroundColor: "#212b36", color: "#fff", padding: 20, display: "flex", flexDirection: "column" }
const linkStyle = { color: "#d6dde8", textDecoration: "none", padding: 10, borderRadius: 4, display: "block" }
const headerStyle = { height: 60, backgroundColor: "#fff", borderBottom: "1px solid #ddd", display: "flex", justifyContent: "flex-end", alignItems: "center", padding: "0 20px" }
const roleBadgeStyle = { padding: "4px 8px", borderRadius: 4, background: "#eef2ff", color: "#3730a3", fontSize: 12, fontWeight: 700 }
const logoutButtonStyle = { border: "1px solid #d7dde8", background: "#fff", borderRadius: 4, padding: "8px 10px" }
const loginPageStyle = { minHeight: "100vh", display: "grid", placeItems: "center", background: "#eef3fb" }
const loginCardStyle = { width: 380, background: "#fff", padding: 28, borderRadius: 8, boxShadow: "0 18px 45px rgba(31, 41, 55, 0.12)", display: "grid", gap: 14 }
const fieldStyle = { display: "grid", gap: 6, color: "#344055", fontWeight: 700 }
const inputStyle = { width: "100%", border: "1px solid #d7dde8", borderRadius: 6, padding: "10px 12px", background: "#fff" }
const errorStyle = { padding: 10, background: "#fee2e2", color: "#991b1b", borderRadius: 6 }
const primaryButtonStyle = { border: 0, borderRadius: 6, padding: "10px 14px", background: "#2563eb", color: "#fff", fontWeight: 700 }
const panelStyle = { background: "#fff", border: "1px solid #e4e8f0", borderRadius: 8, padding: 20 }
const userButtonStyle = { border: "1px solid #d7dde8", background: "#fff", borderRadius: 6, padding: "8px 10px" }
const selectedUserButtonStyle = { ...userButtonStyle, borderColor: "#2563eb", color: "#2563eb", fontWeight: 700 }
const thStyle = { textAlign: "left", borderBottom: "1px solid #e4e8f0", padding: 10 }
const tdStyle = { borderBottom: "1px solid #eef1f6", padding: 10 }

export default App
