import { useCallback, useEffect, useMemo, useState } from "react";
import { API_BASE_URL } from "../apiClient";
import { ActionIcon, ModuleIcon } from "../moduleIcons.jsx";

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

const EMPTY_FORM = {
  id: null,
  hoTen: "",
  email: "",
  soDienThoai: "",
  username: "",
  password: "",
  roleId: "",
  trangThai: "Active",
};

const AV_COLORS = [
  { bg: "#E6F1FB", color: "#185FA5" },
  { bg: "#E1F5EE", color: "#0F6E56" },
  { bg: "#EEEDFE", color: "#534AB7" },
  { bg: "#FAEEDA", color: "#854F0B" },
  { bg: "#FAECE7", color: "#993C1D" },
];

function getInitials(name) {
  if (!name) return "?";
  return name
    .split(" ")
    .filter(Boolean)
    .slice(-2)
    .map((word) => word[0])
    .join("")
    .toUpperCase()
    .slice(0, 2);
}

function isActiveStatus(status) {
  return String(status || "").toLowerCase() === "active";
}

function getStatusLabel(status) {
  return isActiveStatus(status) ? "Hoạt động" : "Đã khóa";
}

function getAvatarColor(id = 1) {
  return AV_COLORS[Math.abs(id - 1) % AV_COLORS.length];
}

function Avatar({ id, name, size = 36 }) {
  const { bg, color } = getAvatarColor(id);
  return (
    <div style={{ ...s.avatar, width: size, height: size, background: bg, color, fontSize: size * 0.35 }}>
      {getInitials(name)}
    </div>
  );
}

function Toggle({ checked, disabled, onChange }) {
  return (
    <label style={{ ...s.toggle, cursor: disabled ? "not-allowed" : "pointer" }}>
      <input
        type="checkbox"
        checked={checked}
        disabled={disabled}
        onChange={onChange}
        style={s.toggleInput}
      />
      <span style={{ ...s.toggleTrack, background: checked ? "#185FA5" : "#d1d5db", opacity: disabled ? 0.5 : 1 }} />
      <span style={{ ...s.toggleThumb, left: checked ? 19 : 3 }} />
    </label>
  );
}

function Toast({ type, text }) {
  if (!text) return null;
  const isOk = type === "success";
  return (
    <div style={{ ...s.toast, background: isOk ? "#E1F5EE" : "#FCEBEB", color: isOk ? "#0F6E56" : "#A32D2D" }}>
      {isOk ? "OK" : "Lỗi"}: {text}
    </div>
  );
}

function Field({ label, children }) {
  return (
    <label style={s.field}>
      <span style={s.label}>{label}</span>
      {children}
    </label>
  );
}

export default function UserPermissionManager({ token }) {
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [modules, setModules] = useState(FALLBACK_MODULES);
  const [selected, setSelected] = useState(null);
  const [form, setForm] = useState(EMPTY_FORM);
  const [query, setQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");
  const [message, setMessage] = useState({ type: "", text: "" });
  const [loading, setLoading] = useState(true);
  const [savingUser, setSavingUser] = useState(false);
  const [savingPermissions, setSavingPermissions] = useState(false);

  const authHeaders = useMemo(
    () => ({ "Content-Type": "application/json", Authorization: `Bearer ${token}` }),
    [token],
  );

  const showMessage = (type, text) => {
    setMessage({ type, text });
    window.setTimeout(() => setMessage({ type: "", text: "" }), 3000);
  };

  const normalizeUserForm = (user = {}) => ({
    id: user.id ?? null,
    hoTen: user.hoTen ?? "",
    email: user.email ?? "",
    soDienThoai: user.soDienThoai ?? "",
    username: user.username ?? "",
    password: "",
    roleId: user.roleId ?? "",
    trangThai: user.trangThai ?? "Active",
  });

  const refreshUsers = useCallback(async () => {
    setLoading(true);
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải người dùng (${res.status})`);
      const data = await res.json();
      const nextUsers = Array.isArray(data) ? data : [];
      setUsers(nextUsers);
      setSelected((prev) => {
        if (!prev) return prev;
        const fresh = nextUsers.find((item) => item.id === prev.id);
        return fresh ? { ...fresh, permissions: prev.permissions ?? [] } : null;
      });
    } catch (err) {
      showMessage("error", err.message || "Tải người dùng thất bại");
    } finally {
      setLoading(false);
    }
  }, [authHeaders]);

  useEffect(() => {
    let ignore = false;
    const init = async () => {
      try {
        const [modRes, roleRes, userRes] = await Promise.all([
          fetch(`${API_BASE_URL}/api/admin/modules`, { headers: authHeaders }),
          fetch(`${API_BASE_URL}/api/admin/roles`, { headers: authHeaders }),
          fetch(`${API_BASE_URL}/api/admin/users`, { headers: authHeaders }),
        ]);
        if (ignore) return;
        if (modRes.ok) {
          const data = await modRes.json();
          setModules(Array.isArray(data) ? data : FALLBACK_MODULES);
        }
        if (roleRes.ok) {
          const data = await roleRes.json();
          setRoles(Array.isArray(data) ? data : []);
        }
        if (!userRes.ok) throw new Error(`Không thể tải người dùng (${userRes.status})`);
        const usersData = await userRes.json();
        setUsers(Array.isArray(usersData) ? usersData : []);
      } catch (err) {
        if (!ignore) showMessage("error", err.message || "Tải dữ liệu quản trị thất bại");
      } finally {
        if (!ignore) setLoading(false);
      }
    };
    void init();
    return () => {
      ignore = true;
    };
  }, [authHeaders]);

  const filteredUsers = useMemo(() => {
    const lower = query.trim().toLowerCase();
    return users.filter((user) => {
      const matchesSearch = !lower
        || String(user.hoTen || "").toLowerCase().includes(lower)
        || String(user.username || "").toLowerCase().includes(lower)
        || String(user.roleName || "").toLowerCase().includes(lower);
      const matchesStatus = statusFilter === "all"
        || (statusFilter === "active" && isActiveStatus(user.trangThai))
        || (statusFilter === "inactive" && !isActiveStatus(user.trangThai));
      return matchesSearch && matchesStatus;
    });
  }, [query, statusFilter, users]);

  const selectUser = async (user) => {
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users/${user.id}/permissions`, { headers: authHeaders });
      if (!res.ok) throw new Error(`Không thể tải quyền (${res.status})`);
      const permissions = await res.json();
      setSelected({ ...user, permissions: Array.isArray(permissions) ? permissions : [] });
      setForm(normalizeUserForm(user));
    } catch (err) {
      showMessage("error", err.message || "Tải quyền thất bại");
    }
  };

  const startCreate = () => {
    setSelected(null);
    setForm({ ...EMPTY_FORM, roleId: roles[0]?.id ?? "" });
  };

  const updateForm = (key, value) => {
    setForm((prev) => ({ ...prev, [key]: value }));
  };

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
      if (!prev) return prev;
      const permissions = ensurePermissions(prev.permissions).map((item) => {
        if (item.moduleKey !== moduleKey) return item;
        const next = { ...item, [key]: !item[key] };
        if ((key === "canRead" || key === "canWrite") && next[key]) next.canView = true;
        if (key === "canWrite" && next.canWrite) next.canRead = true;
        if (key === "canRead" && !next.canRead) next.canWrite = false;
        if (key === "canView" && !next.canView) {
          next.canRead = false;
          next.canWrite = false;
        }
        return next;
      });
      return { ...prev, permissions };
    });
  };

  const saveUser = async () => {
    if (!form.username.trim()) {
      showMessage("error", "Username không được để trống");
      return;
    }
    if (!form.id && !form.password.trim()) {
      showMessage("error", "Mật khẩu bắt buộc khi tạo user mới");
      return;
    }
    if (!form.id && !form.hoTen.trim()) {
      showMessage("error", "Họ tên không được để trống khi tạo user mới");
      return;
    }

    const payload = {
      hoTen: form.hoTen.trim(),
      email: form.email.trim(),
      soDienThoai: form.soDienThoai.trim(),
      username: form.username.trim(),
      password: form.password.trim() || null,
      roleId: form.roleId ? Number(form.roleId) : null,
      trangThai: form.trangThai,
    };

    setSavingUser(true);
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users${form.id ? `/${form.id}` : ""}`, {
        method: form.id ? "PUT" : "POST",
        headers: authHeaders,
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        throw new Error(errData?.message || `Lưu user thất bại (${res.status})`);
      }
      const saved = await res.json();
      await refreshUsers();
      showMessage("success", form.id ? "Đã cập nhật user" : "Đã tạo user mới");
      if (!form.id) {
        const createdUser = {
          id: saved.id,
          username: saved.username,
          nhanSuId: saved.nhanSuId,
          hoTen: form.hoTen,
          roleId: saved.roleId,
          roleName: roles.find((role) => role.id === saved.roleId)?.tenRole,
          trangThai: saved.trangThai,
          permissions: [],
        };
        setSelected(createdUser);
        setForm(normalizeUserForm(createdUser));
      } else {
        setForm((prev) => ({ ...prev, password: "" }));
      }
    } catch (err) {
      showMessage("error", err.message || "Lưu user thất bại");
    } finally {
      setSavingUser(false);
    }
  };

  const deactivateUser = async () => {
    if (!form.id) return;
    setSavingUser(true);
    try {
      const res = await fetch(`${API_BASE_URL}/api/admin/users/${form.id}`, {
        method: "DELETE",
        headers: authHeaders,
      });
      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        throw new Error(errData?.message || `Khóa user thất bại (${res.status})`);
      }
      await refreshUsers();
      setForm((prev) => ({ ...prev, trangThai: "Inactive" }));
      showMessage("success", "Đã khóa user");
    } catch (err) {
      showMessage("error", err.message || "Khóa user thất bại");
    } finally {
      setSavingUser(false);
    }
  };

  const savePermissions = async () => {
    if (!selected) return;
    const normalizedPermissions = ensurePermissions(selected.permissions);
    setSavingPermissions(true);
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
      showMessage("success", `Đã lưu quyền cho ${selected.hoTen || selected.username}`);
    } catch (err) {
      showMessage("error", err.message || "Lưu quyền thất bại");
    } finally {
      setSavingPermissions(false);
    }
  };

  const selectedRoleName = roles.find((role) => role.id === Number(form.roleId))?.tenRole || selected?.roleName || "";
  const isAdmin = Number(form.roleId) === 1 || String(selectedRoleName).toLowerCase() === "admin";

  return (
    <div style={s.page}>
      <div style={s.header}>
        <div>
          <h2 style={s.title}>Quản lý người dùng</h2>
          <p style={s.subtitle}>Tạo tài khoản, cập nhật vai trò, khóa user và phân quyền module.</p>
        </div>
        <div style={s.headerActions}>
          <Toast type={message.type} text={message.text} />
          <button onClick={refreshUsers} style={{ ...s.secondaryBtn, ...s.btnIcon }}>
            <ActionIcon name="refresh" /> Tải lại
          </button>
          <button onClick={startCreate} style={{ ...s.primaryBtn, ...s.btnIcon }}>
            <ActionIcon name="add" /> Thêm user
          </button>
        </div>
      </div>

      <div style={s.grid}>
        <div style={s.panel}>
          <div style={s.panelHeader}>
            <span style={s.panelTitle}>Người dùng</span>
            <span style={s.badge}>{loading ? "đang tải..." : `${filteredUsers.length}/${users.length}`}</span>
          </div>
          <div style={s.filters}>
            <div style={s.searchWrap}>
              <ActionIcon name="search" size={15} />
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="Tìm theo tên, username, vai trò"
                style={s.searchInput}
              />
            </div>
            <select value={statusFilter} onChange={(event) => setStatusFilter(event.target.value)} style={s.input}>
              <option value="all">Tất cả trạng thái</option>
              <option value="active">Đang hoạt động</option>
              <option value="inactive">Đã khóa</option>
            </select>
          </div>
          <div style={s.userList}>
            {loading ? (
              <div style={s.loadingRow}>Đang tải...</div>
            ) : filteredUsers.length === 0 ? (
              <div style={s.loadingRow}>Không có người dùng nào</div>
            ) : (
              filteredUsers.map((user) => (
                <button
                  key={user.id}
                  onClick={() => selectUser(user)}
                  style={{
                    ...s.userItem,
                    background: selected?.id === user.id ? "#E6F1FB" : "transparent",
                  }}
                >
                  <Avatar id={user.id} name={user.hoTen || user.username} size={34} />
                  <div style={s.userText}>
                    <div style={s.userName}>{user.hoTen || `User #${user.id}`}</div>
                    <div style={s.userMeta}>{user.username} - {user.roleName || `Vai trò #${user.roleId || "-"}`}</div>
                  </div>
                  <span style={{ ...s.statusDot, background: isActiveStatus(user.trangThai) ? "#1D9E75" : "#E24B4A" }} />
                </button>
              ))
            )}
          </div>
        </div>

        <div style={s.panel}>
          <div style={s.panelHeader}>
            <span style={s.panelTitle}>{form.id ? "Thông tin tài khoản" : "Thêm tài khoản"}</span>
            <span style={s.badge}>{form.id ? `ID ${form.id}` : "Mới"}</span>
          </div>
          <div style={s.formGrid}>
            <Field label="Họ tên">
              <input value={form.hoTen} onChange={(event) => updateForm("hoTen", event.target.value)} style={s.input} />
            </Field>
            <Field label="Username">
              <input value={form.username} onChange={(event) => updateForm("username", event.target.value)} style={s.input} />
            </Field>
            <Field label={form.id ? "Mật khẩu mới" : "Mật khẩu"}>
              <input
                type="password"
                value={form.password}
                onChange={(event) => updateForm("password", event.target.value)}
                placeholder={form.id ? "Để trống nếu không đổi" : ""}
                style={s.input}
              />
            </Field>
            <Field label="Vai trò">
              <select value={form.roleId} onChange={(event) => updateForm("roleId", event.target.value)} style={s.input}>
                <option value="">Chưa chọn</option>
                {roles.map((role) => (
                  <option key={role.id} value={role.id}>{role.tenRole}</option>
                ))}
              </select>
            </Field>
            <Field label="Email">
              <input value={form.email} onChange={(event) => updateForm("email", event.target.value)} style={s.input} />
            </Field>
            <Field label="Số điện thoại">
              <input value={form.soDienThoai} onChange={(event) => updateForm("soDienThoai", event.target.value)} style={s.input} />
            </Field>
            <Field label="Trạng thái">
              <select value={form.trangThai} onChange={(event) => updateForm("trangThai", event.target.value)} style={s.input}>
                <option value="Active">Hoạt động</option>
                <option value="Inactive">Đã khóa</option>
              </select>
            </Field>
          </div>
          <div style={s.formActions}>
            {form.id && isActiveStatus(form.trangThai) && (
              <button onClick={deactivateUser} disabled={savingUser} style={{ ...s.dangerBtn, ...s.btnIcon }}>
                <ActionIcon name="delete" /> Khóa user
              </button>
            )}
            <button onClick={saveUser} disabled={savingUser} style={{ ...s.primaryBtn, ...s.btnIcon, opacity: savingUser ? 0.6 : 1 }}>
              <ActionIcon name="save" /> {savingUser ? "Đang lưu..." : "Lưu tài khoản"}
            </button>
          </div>
        </div>

        <div style={{ ...s.panel, gridColumn: "2 / 3" }}>
          {!selected ? (
            <div style={s.emptyState}>
              <ModuleIcon moduleKey="NHAN_VIEN" size={34} />
              <span>Chọn user để phân quyền module.</span>
            </div>
          ) : (
            <>
              <div style={s.permissionTopBar}>
                <div style={s.profile}>
                  <Avatar id={selected.id} name={selected.hoTen || selected.username} size={38} />
                  <div>
                    <div style={s.profileName}>{selected.hoTen || selected.username}</div>
                    <div style={s.profileMeta}>{selected.username} - {getStatusLabel(selected.trangThai)}</div>
                  </div>
                </div>
                <div style={s.roleChip}>{selected.roleName || selectedRoleName || "Chưa có vai trò"}</div>
              </div>

              {isAdmin && (
                <div style={s.adminNote}>Admin luôn có toàn quyền trên tất cả module.</div>
              )}

              <div style={s.tableWrap}>
                <table style={s.table}>
                  <thead>
                    <tr>
                      <th style={{ ...s.th, width: "48%" }}>Module</th>
                      {ACTIONS.map(([, label]) => (
                        <th key={label} style={{ ...s.th, textAlign: "center" }}>{label}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {ensurePermissions(selected.permissions).map((perm) => {
                      const mod = modules.find((item) => item.moduleKey === perm.moduleKey);
                      return (
                        <tr key={perm.moduleKey}>
                          <td style={s.td}>
                            <div style={s.moduleCell}>
                              <span style={s.moduleIcon}>
                                <ModuleIcon moduleKey={perm.moduleKey} size={16} />
                              </span>
                              <span style={s.moduleText}>
                                <div style={s.moduleName}>{mod?.name || perm.moduleKey}</div>
                              </span>
                            </div>
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

              <div style={s.footer}>
                <span style={s.helperText}>{isAdmin ? "Không cần lưu quyền riêng cho admin." : "Quyền Ghi tự động bao gồm Đọc và Xem."}</span>
                <button
                  onClick={savePermissions}
                  disabled={savingPermissions || isAdmin}
                  style={{ ...s.primaryBtn, ...s.btnIcon, opacity: savingPermissions || isAdmin ? 0.5 : 1 }}
                >
                  <ActionIcon name="save" /> {savingPermissions ? "Đang lưu..." : "Lưu quyền"}
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

const s = {
  page: {
    fontFamily: "system-ui, sans-serif",
    fontSize: 14,
    color: "#111827",
    padding: 24,
  },
  header: {
    display: "flex",
    alignItems: "flex-start",
    justifyContent: "space-between",
    gap: 16,
    marginBottom: 16,
  },
  title: { fontSize: 18, fontWeight: 700, margin: 0 },
  subtitle: { fontSize: 12, color: "#6b7280", margin: "4px 0 0" },
  headerActions: { display: "flex", alignItems: "center", gap: 8, flexWrap: "wrap", justifyContent: "flex-end" },
  btnIcon: { display: "inline-flex", alignItems: "center", justifyContent: "center", gap: 6 },
  primaryBtn: {
    padding: "8px 14px",
    background: "#185FA5",
    color: "#fff",
    border: "none",
    borderRadius: 8,
    fontSize: 13,
    fontWeight: 600,
    cursor: "pointer",
    whiteSpace: "nowrap",
  },
  secondaryBtn: {
    padding: "8px 12px",
    border: "0.5px solid #d1d5db",
    borderRadius: 8,
    background: "#fff",
    color: "#4b5563",
    fontSize: 13,
    cursor: "pointer",
    whiteSpace: "nowrap",
  },
  dangerBtn: {
    padding: "8px 12px",
    border: "0.5px solid #F0B7B7",
    borderRadius: 8,
    background: "#FCEBEB",
    color: "#A32D2D",
    fontSize: 13,
    fontWeight: 600,
    cursor: "pointer",
    whiteSpace: "nowrap",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "300px minmax(0, 1fr)",
    gap: 16,
    alignItems: "start",
  },
  panel: {
    background: "#fff",
    border: "0.5px solid #e5e7eb",
    borderRadius: 8,
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
    fontWeight: 700,
    color: "#6b7280",
    textTransform: "uppercase",
    letterSpacing: "0.05em",
  },
  badge: { fontSize: 11, background: "#f3f4f6", color: "#6b7280", borderRadius: 20, padding: "2px 8px" },
  filters: { padding: 12, borderBottom: "0.5px solid #f3f4f6", display: "grid", gap: 8 },
  searchWrap: {
    display: "flex",
    alignItems: "center",
    gap: 8,
    border: "0.5px solid #d1d5db",
    borderRadius: 8,
    padding: "0 10px",
    background: "#fff",
  },
  searchInput: {
    border: "none",
    outline: "none",
    height: 34,
    flex: 1,
    minWidth: 0,
    fontSize: 13,
  },
  input: {
    width: "100%",
    height: 36,
    border: "0.5px solid #d1d5db",
    borderRadius: 8,
    padding: "0 10px",
    fontSize: 13,
    background: "#fff",
    boxSizing: "border-box",
  },
  userList: { overflowY: "auto", maxHeight: 560 },
  userItem: {
    width: "100%",
    display: "flex",
    alignItems: "center",
    gap: 10,
    padding: "10px 14px",
    cursor: "pointer",
    border: "none",
    borderBottom: "0.5px solid #f3f4f6",
    textAlign: "left",
  },
  userText: { minWidth: 0, flex: 1 },
  userName: { fontSize: 13, fontWeight: 600, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" },
  userMeta: { fontSize: 11, color: "#6b7280", marginTop: 1, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" },
  statusDot: { width: 8, height: 8, borderRadius: "50%", flexShrink: 0, display: "inline-block" },
  loadingRow: { padding: 20, textAlign: "center", color: "#6b7280", fontSize: 13 },
  formGrid: { padding: 16, display: "grid", gridTemplateColumns: "repeat(2, minmax(0, 1fr))", gap: 12 },
  field: { display: "grid", gap: 5, minWidth: 0 },
  label: { fontSize: 11, fontWeight: 600, color: "#6b7280", textTransform: "uppercase" },
  formActions: {
    padding: "12px 16px",
    borderTop: "0.5px solid #e5e7eb",
    display: "flex",
    justifyContent: "flex-end",
    gap: 8,
    background: "#f9fafb",
  },
  emptyState: {
    minHeight: 320,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    gap: 10,
    color: "#6b7280",
    fontSize: 13,
  },
  permissionTopBar: {
    padding: "12px 16px",
    borderBottom: "0.5px solid #e5e7eb",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    gap: 12,
  },
  profile: { display: "flex", alignItems: "center", gap: 10, minWidth: 0 },
  profileName: { fontSize: 14, fontWeight: 600 },
  profileMeta: { fontSize: 11, color: "#6b7280", marginTop: 1 },
  roleChip: {
    fontSize: 12,
    padding: "4px 10px",
    borderRadius: 20,
    background: "#EEEDFE",
    color: "#534AB7",
    fontWeight: 600,
    whiteSpace: "nowrap",
  },
  adminNote: { margin: "12px 16px 0", fontSize: 12, color: "#185FA5", background: "#E6F1FB", borderRadius: 8, padding: "8px 12px" },
  tableWrap: { overflowX: "auto" },
  table: { width: "100%", borderCollapse: "collapse" },
  th: {
    fontSize: 11,
    fontWeight: 700,
    color: "#6b7280",
    textTransform: "uppercase",
    letterSpacing: "0.05em",
    padding: "10px 14px",
    textAlign: "left",
    background: "#f9fafb",
    borderBottom: "0.5px solid #e5e7eb",
  },
  td: { padding: "9px 14px", borderBottom: "0.5px solid #f3f4f6" },
  moduleCell: { display: "flex", alignItems: "center", gap: 10, minWidth: 0 },
  moduleIcon: {
    width: 30,
    height: 30,
    borderRadius: 8,
    background: "#E6F1FB",
    color: "#185FA5",
    display: "inline-flex",
    alignItems: "center",
    justifyContent: "center",
    flexShrink: 0,
  },
  moduleText: { minWidth: 0 },
  moduleName: { fontWeight: 600, fontSize: 13 },
  footer: {
    padding: "12px 16px",
    borderTop: "0.5px solid #e5e7eb",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    gap: 12,
    background: "#f9fafb",
  },
  helperText: { fontSize: 12, color: "#6b7280" },
  avatar: {
    borderRadius: "50%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontWeight: 700,
    flexShrink: 0,
  },
  toggle: { position: "relative", width: 34, height: 18, display: "inline-block" },
  toggleInput: { opacity: 0, width: 0, height: 0, position: "absolute" },
  toggleTrack: { position: "absolute", inset: 0, borderRadius: 9, transition: "background .15s" },
  toggleThumb: {
    position: "absolute",
    width: 12,
    height: 12,
    borderRadius: "50%",
    background: "#fff",
    top: 3,
    transition: "left .15s",
  },
  toast: { padding: "7px 12px", borderRadius: 8, fontSize: 12, fontWeight: 600 },
};
