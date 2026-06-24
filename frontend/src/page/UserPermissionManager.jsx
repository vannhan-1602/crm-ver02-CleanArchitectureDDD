import { useEffect, useMemo, useState, useCallback } from "react";

const API_BASE_URL = "http://localhost:8081";

const ACTIONS = [
  ["canView", "Xem"],
  ["canRead", "Đọc"],
  ["canWrite", "Ghi"],
];

const FALLBACK_MODULES = [
  { moduleKey: "LEAD", name: "Quản lý Lead", path: "/api/leads" },
  { moduleKey: "KHACH_HANG", name: "Khách hàng", path: "/api/khach-hang" },
  { moduleKey: "CO_HOI", name: "Cơ hội bán hàng", path: "/api/cohoi" },
  { moduleKey: "HOAT_DONG", name: "Hoạt động", path: "/api/hoat-dong" },
  { moduleKey: "BAO_GIA", name: "Báo giá", path: "/api/bao-gia" },
  { moduleKey: "HOP_DONG", name: "Hợp đồng", path: "/api/hop-dong" },
  { moduleKey: "HOA_DON", name: "Hóa đơn", path: "/api/hoa-don" },
  { moduleKey: "TAI_CHINH", name: "Tài chính", path: "/api/tai-chinh" },
  { moduleKey: "BAO_CAO", name: "Báo cáo thống kê", path: "/api/bao-cao-thong-ke" },
  { moduleKey: "SAN_PHAM", name: "Sản phẩm", path: "/api/sanpham" },
  { moduleKey: "TICKET", name: "Ticket", path: "/api/tickets" },
  { moduleKey: "NHAN_VIEN", name: "Nhân viên", path: "/api/nhan-vien" },
];

const AV_COLORS = [
  { bg: "#E6F1FB", color: "#185FA5" },
  { bg: "#E1F5EE", color: "#0F6E56" },
  { bg: "#EEEDFE", color: "#534AB7" },
  { bg: "#FAEEDA", color: "#854F0B" },
  { bg: "#FAECE7", color: "#993C1D" },
];

function getInitials(name) {
  if (!name) return "?";
  return name.split(" ").slice(-2).map((w) => w[0]).join("").toUpperCase().slice(0, 2);
}

function getAvatarColor(id) {
  return AV_COLORS[(id - 1) % AV_COLORS.length];
}

function Avatar({ id, name, size = 36 }) {
  const { bg, color } = getAvatarColor(id);
  return (
      <div
          style={{
            width: size,
            height: size,
            borderRadius: "50%",
            background: bg,
            color,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            fontSize: size * 0.35,
            fontWeight: 500,
            flexShrink: 0,
          }}
      >
        {getInitials(name)}
      </div>
  );
}

function Toggle({ checked, disabled, onChange }) {
  return (
      <label style={{ position: "relative", width: 34, height: 18, display: "inline-block", cursor: disabled ? "not-allowed" : "pointer" }}>
        <input
            type="checkbox"
            checked={checked}
            disabled={disabled}
            onChange={onChange}
            style={{ opacity: 0, width: 0, height: 0, position: "absolute" }}
        />
        <span
            style={{
              position: "absolute",
              inset: 0,
              borderRadius: 9,
              background: checked ? "#185FA5" : "#d1d5db",
              transition: "background .15s",
              opacity: disabled ? 0.5 : 1,
            }}
        />
        <span
            style={{
              position: "absolute",
              width: 12,
              height: 12,
              borderRadius: "50%",
              background: "#fff",
              top: 3,
              left: checked ? 19 : 3,
              transition: "left .15s",
            }}
        />
      </label>
  );
}

function Toast({ type, text }) {
  if (!text) return null;
  const isOk = type === "success";
  return (
      <div
          style={{
            display: "flex",
            alignItems: "center",
            gap: 6,
            padding: "6px 12px",
            borderRadius: 8,
            fontSize: 12,
            background: isOk ? "#E1F5EE" : "#FCEBEB",
            color: isOk ? "#0F6E56" : "#A32D2D",
          }}
      >
        {isOk ? "✓" : "✕"} {text}
      </div>
  );
}

function StatusDot({ active }) {
  return (
      <span
          title={active ? "Hoạt động" : "Không hoạt động"}
          style={{
            width: 7,
            height: 7,
            borderRadius: "50%",
            background: active ? "#1D9E75" : "#E24B4A",
            flexShrink: 0,
            display: "inline-block",
          }}
      />
  );
}

export default function UserPermissionManager({ token }) {
  const [users, setUsers] = useState([]);
  const [modules, setModules] = useState(FALLBACK_MODULES);
  const [selected, setSelected] = useState(null);
  const [message, setMessage] = useState({ type: "", text: "" });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const authHeaders = useMemo(
      () => ({ "Content-Type": "application/json", Authorization: `Bearer ${token}` }),
      [token]
  );

  const showMessage = (type, text) => {
    setMessage({ type, text });
    setTimeout(() => setMessage({ type: "", text: "" }), 3000);
  };

  const loadUsers = useCallback(async () => {
    setLoading(true);
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải user (${res.status})`);
      const data = await res.json();
      setUsers(Array.isArray(data) ? data : []);
    } catch (err) {
      showMessage("error", err.message || "Tải người dùng thất bại");
    } finally {
      setLoading(false);
    }
  }, [authHeaders]);

  const selectUser = async (user) => {
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users/${user.id}/permissions`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải quyền (${res.status})`);
      const permissions = await res.json();
      setSelected({ ...user, permissions: Array.isArray(permissions) ? permissions : [] });
    } catch (err) {
      showMessage("error", err.message || "Tải quyền thất bại");
    }
  };

  useEffect(() => {
    let ignore = false;
    const init = async () => {
      try {
        const modRes = await fetch(`${API_BASE_URL}/api/admin/modules`, { headers: authHeaders });
        if (modRes.ok && !ignore) {
          const data = await modRes.json();
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
        if (!ignore) showMessage("error", err.message || "Tải người dùng thất bại");
      } finally {
        if (!ignore) setLoading(false);
      }
    };
    void init();
    return () => { ignore = true; };
  }, [authHeaders]);

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
        if ((key === "canRead" || key === "canWrite") && next[key]) next.canView = true;
        if (key === "canWrite" && next.canWrite) next.canRead = true;
        if (key === "canRead" && !next.canRead) next.canWrite = false;
        if (key === "canView" && !next.canView) { next.canRead = false; next.canWrite = false; }
        return next;
      });
      return { ...prev, permissions };
    });
  };

  const savePermissions = async () => {
    if (!selected) return;
    const normalizedPermissions = ensurePermissions(selected.permissions);
    setSaving(true);
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users/${selected.id}/permissions`, {
        method: "PUT",
        headers: authHeaders,
        body: JSON.stringify({ permissions: normalizedPermissions }),
      });
      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        throw new Error(errData?.message || `Lưu quyền thất bại (${res.status})`);
      }
      const permissions = await res.json();
      setSelected((prev) => ({ ...prev, permissions }));
      showMessage("success", "Đã lưu quyền cho " + selected.hoTen);
    } catch (err) {
      showMessage("error", err.message || "Lưu quyền thất bại");
    } finally {
      setSaving(false);
    }
  };

  const isAdmin = selected?.roleId === 1;

  return (
      <div style={{ fontFamily: "system-ui, sans-serif", fontSize: 14, color: "#111827" }}>
        {/* Header */}
        <div style={{ display: "flex", alignItems: "baseline", justifyContent: "space-between", marginBottom: 4 }}>
          <div>
            <h2 style={{ fontSize: 16, fontWeight: 500, margin: 0 }}>Phân quyền người dùng</h2>
            <p style={{ fontSize: 12, color: "#6b7280", marginTop: 2 }}>Nguồn: HT_UserModulePermission</p>
          </div>
          <button onClick={loadUsers} style={s.reloadBtn}>
            ↺ Tải lại
          </button>
        </div>

        {/* Main grid */}
        <div style={s.grid}>
          {/* User list */}
          <div style={s.panel}>
            <div style={s.panelHeader}>
              <span style={s.panelTitle}>Người dùng</span>
              <span style={s.badge}>{loading ? "đang tải…" : `${users.length} người`}</span>
            </div>
            <div style={{ overflowY: "auto", maxHeight: 480 }}>
              {loading ? (
                  <div style={s.loadingRow}>Đang tải...</div>
              ) : users.length === 0 ? (
                  <div style={s.loadingRow}>Không có người dùng nào</div>
              ) : (
                  users.map((user) => (
                      <button
                          key={user.id}
                          onClick={() => selectUser(user)}
                          style={{
                            ...s.userItem,
                            background: selected?.id === user.id ? "#E6F1FB" : "transparent",
                          }}
                      >
                        <Avatar id={user.id} name={user.hoTen} size={34} />
                        <div style={{ minWidth: 0, flex: 1 }}>
                          <div style={s.userName}>{user.hoTen || `User #${user.id}`}</div>
                          <div style={s.userMeta}>{user.username} · {user.roleName || `Vai trò #${user.roleId}`}</div>
                        </div>
                        <StatusDot active={user.trangThai === "Hoạt động"} />
                      </button>
                  ))
              )}
            </div>
          </div>

          {/* Detail panel */}
          <div style={s.panel}>
            {!selected ? (
                <div style={s.emptyState}>
                  <span style={{ fontSize: 36, opacity: 0.25 }}>👥</span>
                  <span style={{ fontSize: 13, color: "#6b7280" }}>Chọn người dùng để xem và chỉnh quyền</span>
                </div>
            ) : (
                <div style={{ display: "flex", flexDirection: "column", height: "100%" }}>
                  {/* Profile topbar */}
                  <div style={s.topBar}>
                    <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                      <Avatar id={selected.id} name={selected.hoTen} size={38} />
                      <div>
                        <div style={{ fontSize: 14, fontWeight: 500 }}>{selected.hoTen}</div>
                        <div style={{ fontSize: 11, color: "#6b7280", marginTop: 1 }}>
                          {selected.username} · {selected.trangThai}
                        </div>
                      </div>
                    </div>
                    <div style={s.roleChip}>🛡 {selected.roleName}</div>
                  </div>

                  {/* Admin note */}
                  {isAdmin && (
                      <div style={{ margin: "10px 16px 0" }}>
                        <div style={s.adminNote}>🔒 Admin luôn có toàn quyền trên tất cả module</div>
                      </div>
                  )}

                  {/* Permissions table */}
                  <div style={{ overflowY: "auto", flex: 1 }}>
                    <table style={s.table}>
                      <thead>
                      <tr>
                        <th style={{ ...s.th, width: "46%" }}>Module</th>
                        {ACTIONS.map(([, label]) => (
                            <th key={label} style={{ ...s.th, textAlign: "center" }}>{label}</th>
                        ))}
                      </tr>
                      </thead>
                      <tbody>
                      {ensurePermissions(selected.permissions).map((perm) => {
                        const mod = modules.find((m) => m.moduleKey === perm.moduleKey);
                        return (
                            <tr key={perm.moduleKey} style={{ cursor: "default" }}>
                              <td style={s.td}>
                                <div style={{ fontWeight: 500, fontSize: 13 }}>{mod?.name || perm.moduleKey}</div>
                                <div style={{ fontSize: 11, color: "#6b7280", marginTop: 1 }}>{mod?.path}</div>
                              </td>
                              {ACTIONS.map(([key]) => (
                                  <td key={key} style={{ ...s.td, textAlign: "center" }}>
                                    <Toggle
                                        checked={Boolean(perm[key])}
                                        disabled={isAdmin}
                                        onChange={() => togglePermission(perm.moduleKey, key)}
                                    />
                                  </td>
                              ))}
                            </tr>
                        );
                      })}
                      </tbody>
                    </table>
                  </div>

                  {/* Footer */}
                  <div style={s.footer}>
                    <Toast type={message.type} text={message.text} />
                    <button
                        onClick={savePermissions}
                        disabled={saving || isAdmin}
                        style={{ ...s.saveBtn, opacity: saving || isAdmin ? 0.5 : 1, cursor: saving || isAdmin ? "not-allowed" : "pointer" }}
                    >
                      {saving ? "Đang lưu…" : "💾 Lưu quyền"}
                    </button>
                  </div>
                </div>
            )}
          </div>
        </div>
      </div>
  );
}

const s = {
  grid: {
    display: "grid",
    gridTemplateColumns: "280px minmax(0, 1fr)",
    gap: 16,
    minHeight: 560,
    alignItems: "start",
  },
  panel: {
    background: "#fff",
    border: "0.5px solid #e5e7eb",
    borderRadius: 12,
    overflow: "hidden",
  },
  panelHeader: {
    padding: "12px 16px",
    borderBottom: "0.5px solid #e5e7eb",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
  },
  panelTitle: {
    fontSize: 11,
    fontWeight: 500,
    color: "#6b7280",
    textTransform: "uppercase",
    letterSpacing: "0.05em",
  },
  badge: {
    fontSize: 11,
    background: "#f3f4f6",
    color: "#6b7280",
    borderRadius: 20,
    padding: "2px 8px",
  },
  userItem: {
    width: "100%",
    display: "flex",
    alignItems: "center",
    gap: 10,
    padding: "10px 14px",
    cursor: "pointer",
    borderBottom: "0.5px solid #f3f4f6",
    border: "none",
    textAlign: "left",
    transition: "background .12s",
  },
  userName: { fontSize: 13, fontWeight: 500, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" },
  userMeta: { fontSize: 11, color: "#6b7280", marginTop: 1, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" },
  loadingRow: { padding: 20, textAlign: "center", color: "#6b7280", fontSize: 13 },
  emptyState: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    gap: 10,
    padding: 60,
    minHeight: 300,
  },
  topBar: {
    padding: "12px 16px",
    borderBottom: "0.5px solid #e5e7eb",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
  },
  roleChip: {
    display: "inline-flex",
    alignItems: "center",
    gap: 5,
    fontSize: 12,
    padding: "3px 10px",
    borderRadius: 20,
    background: "#EEEDFE",
    color: "#534AB7",
    fontWeight: 500,
    whiteSpace: "nowrap",
  },
  adminNote: {
    display: "flex",
    alignItems: "center",
    gap: 6,
    fontSize: 12,
    color: "#185FA5",
    background: "#E6F1FB",
    borderRadius: 8,
    padding: "6px 12px",
  },
  table: { width: "100%", borderCollapse: "collapse" },
  th: {
    fontSize: 11,
    fontWeight: 500,
    color: "#6b7280",
    textTransform: "uppercase",
    letterSpacing: "0.05em",
    padding: "10px 14px",
    textAlign: "left",
    background: "#f9fafb",
    borderBottom: "0.5px solid #e5e7eb",
  },
  td: { padding: "9px 14px", borderBottom: "0.5px solid #f3f4f6" },
  footer: {
    padding: "12px 16px",
    borderTop: "0.5px solid #e5e7eb",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    background: "#f9fafb",
  },
  saveBtn: {
    display: "inline-flex",
    alignItems: "center",
    gap: 6,
    padding: "7px 16px",
    background: "#185FA5",
    color: "#fff",
    border: "none",
    borderRadius: 8,
    fontSize: 13,
    fontWeight: 500,
    transition: "background .12s",
  },
  reloadBtn: {
    display: "inline-flex",
    alignItems: "center",
    gap: 5,
    padding: "6px 12px",
    border: "0.5px solid #d1d5db",
    borderRadius: 8,
    background: "transparent",
    color: "#6b7280",
    fontSize: 12,
    cursor: "pointer",
  },
};