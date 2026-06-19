import { useEffect, useMemo, useState } from "react";

const API_BASE_URL = "http://localhost:8081";
const ROLES = ["admin", "manager", "sale", "accountant"];
const ACTIONS = [
  ["canView", "Xem"],
  ["canCreate", "Tạo"],
  ["canUpdate", "Sửa"],
  ["canDelete", "Xóa"],
];
const FALLBACK_MODULES = [
  { code: "leads", name: "Quản lý Lead" },
  { code: "khach-hang", name: "Quản lý Khách hàng" },
  { code: "hoat-dong", name: "Quản lý Hoạt động" },
  { code: "hop-dong", name: "Quản lý Hợp đồng" },
  { code: "tai-chinh", name: "Quản lý Hóa đơn" },
  { code: "sanpham", name: "Quản lý Sản phẩm" },
  { code: "tickets", name: "Quản lý Ticket" },
  { code: "cohoi", name: "Quản lý Cơ hội bán hàng" },
  { code: "baogia", name: "Quản lý Báo giá" },
  { code: "bao-cao-thong-ke", name: "Báo cáo thống kê" },
];

export default function UserPermissionManager({ token }) {
  const [users, setUsers] = useState([]);
  const [modules, setModules] = useState(FALLBACK_MODULES);
  const [selected, setSelected] = useState(null);
  const [filters, setFilters] = useState({ roleCode: "", chucVu: "", phongBan: "" });
  const [message, setMessage] = useState({ type: "", text: "" });
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  const authHeaders = useMemo(
    () => ({
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    }),
    [token],
  );

  const loadModules = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/api/permissions/modules`, { headers: authHeaders });
      if (res.ok) setModules(await res.json());
    } catch {
      setModules(FALLBACK_MODULES);
    }
  };

  const loadUsers = async () => {
    setLoading(true);
    setMessage({ type: "", text: "" });
    const query = new URLSearchParams();
    Object.entries(filters).forEach(([key, value]) => {
      if (value) query.set(key, value);
    });
    try {
      const res = await fetch(`${API_BASE_URL}/api/users?${query.toString()}`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải user (${res.status})`);
      const data = await res.json();
      setUsers(Array.isArray(data) ? data : []);
      if (selected && !data.some((item) => item.id === selected.id)) setSelected(null);
    } catch (err) {
      setMessage({ type: "error", text: err.message || "Tải user thất bại" });
    } finally {
      setLoading(false);
    }
  };

  const loadUserDetail = async (id) => {
    try {
      const res = await fetch(`${API_BASE_URL}/api/users/${id}`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải chi tiết user (${res.status})`);
      setSelected(await res.json());
    } catch (err) {
      setMessage({ type: "error", text: err.message || "Tải chi tiết thất bại" });
    }
  };

  useEffect(() => {
    loadModules();
  }, []);

  useEffect(() => {
    loadUsers();
  }, [filters.roleCode, filters.chucVu, filters.phongBan]);

  const updateSelected = (patch) => setSelected((prev) => ({ ...prev, ...patch }));

  const togglePermission = (moduleCode, key) => {
    setSelected((prev) => {
      const permissions = ensurePermissions(prev.permissions).map((item) => {
        if (item.moduleCode !== moduleCode) return item;
        const next = { ...item, [key]: !item[key] };
        if (["canCreate", "canUpdate", "canDelete"].includes(key) && next[key]) {
          next.canView = true;
        }
        if (key === "canView" && !next.canView) {
          next.canCreate = false;
          next.canUpdate = false;
          next.canDelete = false;
        }
        return next;
      });
      return { ...prev, permissions };
    });
  };

  const saveProfile = async () => {
    if (!selected) return;
    setSaving(true);
    setMessage({ type: "", text: "" });
    try {
      const res = await fetch(`${API_BASE_URL}/api/users/${selected.id}`, {
        method: "PUT",
        headers: authHeaders,
        body: JSON.stringify({
          username: selected.username || `user${selected.id}`,
          roleCode: selected.roleCode || "sale",
          chucVu: selected.chucVu || "",
          phongBan: selected.phongBan || "",
          active: selected.active,
        }),
      });
      if (!res.ok) throw new Error(`Lưu user thất bại (${res.status})`);
      setSelected(await res.json());
      setMessage({ type: "success", text: "Đã lưu thông tin user" });
      loadUsers();
    } catch (err) {
      setMessage({ type: "error", text: err.message || "Lưu user thất bại" });
    } finally {
      setSaving(false);
    }
  };

  const savePermissions = async () => {
    if (!selected) return;
    setSaving(true);
    setMessage({ type: "", text: "" });
    try {
      const res = await fetch(`${API_BASE_URL}/api/users/${selected.id}/permissions`, {
        method: "PUT",
        headers: authHeaders,
        body: JSON.stringify({ permissions: ensurePermissions(selected.permissions) }),
      });
      if (!res.ok) throw new Error(`Lưu quyền thất bại (${res.status})`);
      setSelected(await res.json());
      setMessage({ type: "success", text: "Đã lưu quyền user" });
    } catch (err) {
      setMessage({ type: "error", text: err.message || "Lưu quyền thất bại" });
    } finally {
      setSaving(false);
    }
  };

  const ensurePermissions = (permissions = []) => {
    const byCode = new Map(permissions.map((item) => [item.moduleCode, item]));
    return modules.map((module) => ({
      moduleCode: module.code,
      canView: false,
      canCreate: false,
      canUpdate: false,
      canDelete: false,
      ...(byCode.get(module.code) || {}),
    }));
  };

  const chucVuList = [...new Set(users.map((u) => u.chucVu).filter(Boolean))];
  const phongBanList = [...new Set(users.map((u) => u.phongBan).filter(Boolean))];

  return (
    <div>
      <div style={styles.headerRow}>
        <div>
          <h2 style={styles.title}>Quản lý User & Phân quyền</h2>
          <p style={styles.subTitle}>Role cố định, quyền riêng từng user theo module.</p>
        </div>
        <button onClick={loadUsers} style={styles.secondaryButton}>Tải lại</button>
      </div>

      <div style={styles.filters}>
        <select value={filters.roleCode} onChange={(e) => setFilters((p) => ({ ...p, roleCode: e.target.value }))} style={styles.input}>
          <option value="">Tất cả role</option>
          {ROLES.map((role) => <option key={role} value={role}>{role}</option>)}
        </select>
        <input
          list="chuc-vu-list"
          value={filters.chucVu}
          onChange={(e) => setFilters((p) => ({ ...p, chucVu: e.target.value }))}
          placeholder="Lọc chức vụ"
          style={styles.input}
        />
        <datalist id="chuc-vu-list">{chucVuList.map((item) => <option key={item} value={item} />)}</datalist>
        <input
          list="phong-ban-list"
          value={filters.phongBan}
          onChange={(e) => setFilters((p) => ({ ...p, phongBan: e.target.value }))}
          placeholder="Lọc phòng ban"
          style={styles.input}
        />
        <datalist id="phong-ban-list">{phongBanList.map((item) => <option key={item} value={item} />)}</datalist>
      </div>

      {message.text && (
        <div style={message.type === "error" ? styles.error : styles.success}>{message.text}</div>
      )}

      <div style={styles.layout}>
        <div style={styles.userList}>
          <div style={styles.listHeader}>Danh sách user {loading ? "(đang tải...)" : `(${users.length})`}</div>
          {users.map((user) => (
            <button
              key={user.id}
              onClick={() => loadUserDetail(user.id)}
              style={{
                ...styles.userRow,
                borderColor: selected?.id === user.id ? "#2563eb" : "#e5e7eb",
                background: selected?.id === user.id ? "#eff6ff" : "#fff",
              }}
            >
              <strong>{user.hoTen || `User #${user.id}`}</strong>
              <span>{user.username || "Chưa có tài khoản"} · {user.roleCode || "sale"}</span>
              <span>{user.chucVu || "Chưa có chức vụ"} · {user.phongBan || "Chưa có phòng ban"}</span>
            </button>
          ))}
        </div>

        <div style={styles.detail}>
          {!selected ? (
            <div style={styles.empty}>Chọn một user để chỉnh thông tin và quyền.</div>
          ) : (
            <>
              <div style={styles.profileGrid}>
                <label style={styles.fieldLabel}>Tên nhân viên<input value={selected.hoTen || ""} disabled style={styles.input} /></label>
                <label style={styles.fieldLabel}>Username<input value={selected.username || ""} onChange={(e) => updateSelected({ username: e.target.value })} style={styles.input} /></label>
                <label style={styles.fieldLabel}>Role
                  <select value={selected.roleCode || "sale"} onChange={(e) => updateSelected({ roleCode: e.target.value })} style={styles.input}>
                    {ROLES.map((role) => <option key={role} value={role}>{role}</option>)}
                  </select>
                </label>
                <label style={styles.fieldLabel}>Chức vụ<input value={selected.chucVu || ""} onChange={(e) => updateSelected({ chucVu: e.target.value })} style={styles.input} /></label>
                <label style={styles.fieldLabel}>Phòng ban<input value={selected.phongBan || ""} onChange={(e) => updateSelected({ phongBan: e.target.value })} style={styles.input} /></label>
                <label style={styles.checkLine}>
                  <input type="checkbox" checked={Boolean(selected.active)} onChange={(e) => updateSelected({ active: e.target.checked })} />
                  Cho phép đăng nhập
                </label>
              </div>
              <div style={styles.actions}>
                <button onClick={saveProfile} disabled={saving} style={styles.primaryButton}>Lưu thông tin</button>
                <button onClick={savePermissions} disabled={saving || selected.roleCode === "admin"} style={styles.primaryButton}>Lưu quyền</button>
              </div>
              {selected.roleCode === "admin" && <div style={styles.note}>Admin luôn có toàn quyền.</div>}
              <table style={styles.table}>
                <thead>
                  <tr>
                    <th style={styles.th}>Module</th>
                    {ACTIONS.map(([, label]) => <th key={label} style={styles.th}>{label}</th>)}
                  </tr>
                </thead>
                <tbody>
                  {ensurePermissions(selected.permissions).map((permission) => {
                    const module = modules.find((item) => item.code === permission.moduleCode);
                    return (
                      <tr key={permission.moduleCode}>
                        <td style={styles.td}>{module?.name || permission.moduleCode}</td>
                        {ACTIONS.map(([key]) => (
                          <td key={key} style={styles.tdCenter}>
                            <input
                              type="checkbox"
                              checked={Boolean(permission[key])}
                              disabled={selected.roleCode === "admin"}
                              onChange={() => togglePermission(permission.moduleCode, key)}
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
  filters: { display: "flex", gap: "10px", marginBottom: "14px", flexWrap: "wrap" },
  input: { padding: "9px 10px", border: "1px solid #d1d5db", borderRadius: "6px", minWidth: "160px" },
  layout: { display: "grid", gridTemplateColumns: "330px minmax(0, 1fr)", gap: "16px", alignItems: "start" },
  userList: { display: "flex", flexDirection: "column", gap: "8px" },
  listHeader: { fontWeight: 700, color: "#374151", marginBottom: "4px" },
  userRow: { textAlign: "left", border: "1px solid #e5e7eb", borderRadius: "8px", padding: "12px", display: "grid", gap: "4px", cursor: "pointer" },
  detail: { background: "#fff", border: "1px solid #e5e7eb", borderRadius: "8px", padding: "16px" },
  empty: { color: "#6b7280", padding: "40px", textAlign: "center" },
  profileGrid: { display: "grid", gridTemplateColumns: "repeat(2, minmax(180px, 1fr))", gap: "12px", marginBottom: "12px" },
  fieldLabel: { display: "grid", gap: "6px", color: "#374151", fontSize: "14px" },
  checkLine: { display: "flex", alignItems: "center", gap: "8px", color: "#374151" },
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
