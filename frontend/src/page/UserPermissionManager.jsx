import { useEffect, useMemo, useState } from "react";

const API_BASE_URL = "http://localhost:8081";
const ACTIONS = [
  ["canView", "Xem"],
  ["canRead", "Đọc"],
  ["canWrite", "Ghi"],
];
const FALLBACK_MODULES = [
  { moduleKey: "LEAD", name: "Quản lý Lead", path: "/api/leads" },
  { moduleKey: "KHACH_HANG", name: "Quản lý Khách hàng", path: "/api/khach-hang" },
  { moduleKey: "CO_HOI", name: "Quản lý Cơ hội bán hàng", path: "/api/cohoi" },
  { moduleKey: "HOAT_DONG", name: "Quản lý Hoạt động", path: "/api/hoat-dong" },
  { moduleKey: "BAO_GIA", name: "Quản lý Báo giá", path: "/api/bao-gia" },
  { moduleKey: "HOP_DONG", name: "Quản lý Hợp đồng", path: "/api/hop-dong" },
  { moduleKey: "HOA_DON", name: "Quản lý Hóa đơn", path: "/api/hoa-don" },
  { moduleKey: "TAI_CHINH", name: "Quản lý Tài chính", path: "/api/tai-chinh" },
  { moduleKey: "BAO_CAO", name: "Báo cáo thống kê", path: "/api/bao-cao-thong-ke" },
  { moduleKey: "SAN_PHAM", name: "Quản lý Sản phẩm", path: "/api/sanpham" },
  { moduleKey: "TICKET", name: "Quản lý Ticket", path: "/api/tickets" },
  { moduleKey: "NHAN_VIEN", name: "Quản lý Nhân viên", path: "/api/nhan-vien" },
];

export default function UserPermissionManager({ token }) {
  const [users, setUsers] = useState([]);
  const [modules, setModules] = useState(FALLBACK_MODULES);
  const [selected, setSelected] = useState(null);
  const [message, setMessage] = useState({ type: "", text: "" });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const authHeaders = useMemo(
    () => ({
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    }),
    [token],
  );

  const loadUsers = async () => {
    setLoading(true);
    setMessage({ type: "", text: "" });
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải user (${res.status})`);
      const data = await res.json();
      setUsers(Array.isArray(data) ? data : []);
      if (selected && !data.some((item) => item.id === selected.id)) setSelected(null);
    } catch (err) {
      setMessage({ type: "error", text: err.message || "Tải người dùng thất bại" });
    } finally {
      setLoading(false);
    }
  };

  const selectUser = async (user) => {
    setMessage({ type: "", text: "" });
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users/${user.id}/permissions`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải quyền (${res.status})`);
      const permissions = await res.json();
      setSelected({ ...user, permissions: Array.isArray(permissions) ? permissions : [] });
    } catch (err) {
      setMessage({ type: "error", text: err.message || "Tải quyền thất bại" });
    }
  };

  useEffect(() => {
    let ignore = false;

    const loadInitialData = async () => {
      try {
        const moduleRes = await fetch(`${API_BASE_URL}/api/admin/modules`, { headers: authHeaders });
        if (moduleRes.ok && !ignore) {
          const data = await moduleRes.json();
          setModules(Array.isArray(data) ? data : FALLBACK_MODULES);
        }
      } catch {
        if (!ignore) setModules(FALLBACK_MODULES);
      }

      try {
        const userRes = await fetch(`${API_BASE_URL}/api/admin/users`, { headers: authHeaders });
        if (!userRes.ok) throw new Error(`Không thể tải user (${userRes.status})`);
        const data = await userRes.json();
        if (!ignore) setUsers(Array.isArray(data) ? data : []);
      } catch (err) {
        if (!ignore) setMessage({ type: "error", text: err.message || "Tải người dùng thất bại" });
      } finally {
        if (!ignore) setLoading(false);
      }
    };

    void loadInitialData();
    return () => {
      ignore = true;
    };
  }, [authHeaders]);

  const updateSelected = (patch) => setSelected((prev) => ({ ...prev, ...patch }));

  const ensurePermissions = (permissions = []) => {
    const byKey = new Map(permissions.map((item) => [item.moduleKey, item]));
    return modules.map((module) => ({
      moduleKey: module.moduleKey,
      canView: false,
      canRead: false,
      canWrite: false,
      ...(byKey.get(module.moduleKey) || {}),
    }));
  };

  const togglePermission = (moduleKey, key) => {
    setSelected((prev) => {
      const permissions = ensurePermissions(prev.permissions).map((item) => {
        if (item.moduleKey !== moduleKey) return item;
        const next = { ...item, [key]: !item[key] };
        if ((key === "canRead" || key === "canWrite") && next[key]) {
          next.canView = true;
        }
        if (key === "canWrite" && next.canWrite) {
          next.canRead = true;
        }
        if (key === "canRead" && !next.canRead) {
          next.canWrite = false;
        }
        if (key === "canView" && !next.canView) {
          next.canRead = false;
          next.canWrite = false;
        }
        return next;
      });
      return { ...prev, permissions };
    });
  };

  const savePermissions = async () => {
    if (!selected) return;
    setSaving(true);
    setMessage({ type: "", text: "" });
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users/${selected.id}/permissions`, {
        method: "PUT",
        headers: authHeaders,
        body: JSON.stringify({ permissions: ensurePermissions(selected.permissions) }),
      });
      if (!res.ok) throw new Error(`Lưu quyền thất bại (${res.status})`);
      const permissions = await res.json();
      updateSelected({ permissions });
      setMessage({ type: "success", text: "Đã lưu quyền người dùng" });
    } catch (err) {
      setMessage({ type: "error", text: err.message || "Lưu quyền thất bại" });
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <div style={styles.headerRow}>
        <div>
          <h2 style={styles.title}>Quản lý người dùng & Phân quyền</h2>
          <p style={styles.subTitle}>Nguồn quyền: HT_UserModulePermission.</p>
        </div>
        <button onClick={loadUsers} style={styles.secondaryButton}>Tải lại</button>
      </div>

      {message.text && (
        <div style={message.type === "error" ? styles.error : styles.success}>{message.text}</div>
      )}

      <div style={styles.layout}>
        <div style={styles.userList}>
          <div style={styles.listHeader}>Danh sách người dùng {loading ? "(đang tải...)" : `(${users.length})`}</div>
          {users.map((user) => (
            <button
              key={user.id}
              onClick={() => selectUser(user)}
              style={{
                ...styles.userRow,
                borderColor: selected?.id === user.id ? "#2563eb" : "#e5e7eb",
                background: selected?.id === user.id ? "#eff6ff" : "#fff",
              }}
            >
              <strong>{user.hoTen || `User #${user.id}`}</strong>
              <span>{user.username} - {user.roleName || `Vai trò #${user.roleId || "-"}`}</span>
              <span>Trạng thái: {user.trangThai || "-"}</span>
            </button>
          ))}
        </div>

        <div style={styles.detail}>
          {!selected ? (
            <div style={styles.empty}>Chọn người dùng để chỉnh quyền.</div>
          ) : (
            <>
              <div style={styles.profileGrid}>
                <label style={styles.fieldLabel}>Nhân viên<input value={selected.hoTen || ""} disabled style={styles.input} /></label>
                <label style={styles.fieldLabel}>Tên đăng nhập<input value={selected.username || ""} disabled style={styles.input} /></label>
                <label style={styles.fieldLabel}>Vai trò<input value={selected.roleName || ""} disabled style={styles.input} /></label>
                <label style={styles.fieldLabel}>Trạng thái<input value={selected.trangThai || ""} disabled style={styles.input} /></label>
              </div>
              <div style={styles.actions}>
                <button onClick={savePermissions} disabled={saving || selected.roleId === 1} style={styles.primaryButton}>
                  Lưu quyền
                </button>
              </div>
              {selected.roleId === 1 && <div style={styles.note}>Admin luôn có toàn quyền.</div>}
              <table style={styles.table}>
                <thead>
                  <tr>
                    <th style={styles.th}>Module</th>
                    {ACTIONS.map(([, label]) => <th key={label} style={styles.th}>{label}</th>)}
                  </tr>
                </thead>
                <tbody>
                  {ensurePermissions(selected.permissions).map((permission) => {
                    const module = modules.find((item) => item.moduleKey === permission.moduleKey);
                    return (
                      <tr key={permission.moduleKey}>
                        <td style={styles.td}>{module?.name || permission.moduleKey}</td>
                        {ACTIONS.map(([key]) => (
                          <td key={key} style={styles.tdCenter}>
                            <input
                              type="checkbox"
                              checked={Boolean(permission[key])}
                              disabled={selected.roleId === 1}
                              onChange={() => togglePermission(permission.moduleKey, key)}
                            />
                          </td>
                        ))}
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

const styles = {
  headerRow: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" },
  title: { margin: 0, fontSize: "24px", color: "#111827" },
  subTitle: { margin: "6px 0 0", color: "#6b7280" },
  input: { padding: "9px 10px", border: "1px solid #d1d5db", borderRadius: "6px", minWidth: "160px" },
  layout: { display: "grid", gridTemplateColumns: "330px minmax(0, 1fr)", gap: "16px", alignItems: "start" },
  userList: { display: "flex", flexDirection: "column", gap: "8px" },
  listHeader: { fontWeight: 700, color: "#374151", marginBottom: "4px" },
  userRow: { textAlign: "left", border: "1px solid #e5e7eb", borderRadius: "8px", padding: "12px", display: "grid", gap: "4px", cursor: "pointer" },
  detail: { background: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px", padding: "16px" },
  empty: { color: "#6b7280", padding: "40px", textAlign: "center" },
  profileGrid: { display: "grid", gridTemplateColumns: "repeat(2, minmax(180px, 1fr))", gap: "12px", marginBottom: "12px" },
  fieldLabel: { display: "grid", gap: "6px", color: "#374151", fontSize: "14px" },
  actions: { display: "flex", gap: "10px", marginBottom: "12px" },
  primaryButton: { border: "none", borderRadius: "6px", background: "#2563eb", color: "#fff", padding: "9px 14px", cursor: "pointer", fontWeight: 700 },
  secondaryButton: { border: "1px solid #d1d5db", borderRadius: "6px", background: "#fff", color: "#374151", padding: "9px 14px", cursor: "pointer" },
  note: { background: "#ecfdf5", color: "#047857", padding: "10px 12px", borderRadius: "6px", marginBottom: "12px" },
  table: { width: "100%", borderCollapse: "collapse" },
  th: { textAlign: "left", background: "#f3f4f6", padding: "10px", border: "1px solid #e5e7eb" },
  td: { padding: "10px", border: "1px solid #e5e7eb" },
  tdCenter: { padding: "10px", border: "1px solid #e5e7eb", textAlign: "center" },
  error: { background: "#fef2f2", color: "#b91c1c", padding: "10px 12px", borderRadius: "6px", marginBottom: "12px" },
  success: { background: "#ecfdf5", color: "#047857", padding: "10px 12px", borderRadius: "6px", marginBottom: "12px" },
};
