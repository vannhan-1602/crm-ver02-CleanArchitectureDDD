import axios from "axios";

export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

const MODULE_ALIASES = {
  LEAD: ["LEAD", "leads"],
  KHACH_HANG: ["KHACH_HANG", "khach-hang"],
  HOAT_DONG: ["HOAT_DONG", "hoat-dong"],
  HOP_DONG: ["HOP_DONG", "hop-dong"],
  HOA_DON: ["HOA_DON", "hoa-don", "TAI_CHINH", "tai-chinh"],
  TAI_CHINH: ["TAI_CHINH", "tai-chinh", "HOA_DON", "hoa-don"],
  SAN_PHAM: ["SAN_PHAM", "sanpham", "loaisanpham"],
  TICKET: ["TICKET", "tickets", "loai-ticket", "ticket-phan-hoi"],
  CO_HOI: ["CO_HOI", "cohoi"],
  BAO_GIA: ["BAO_GIA", "bao-gia"],
  BAO_CAO: ["BAO_CAO", "bao-cao-thong-ke"],
  NHAN_VIEN: ["NHAN_VIEN", "nhan-vien"],
};

const PATH_MODULES = [
  ["/api/leads", "LEAD"],
  ["/api/khach-hang", "KHACH_HANG"],
  ["/api/hoat-dong", "HOAT_DONG"],
  ["/api/hop-dong", "HOP_DONG"],
  ["/api/hoa-don", "HOA_DON"],
  ["/api/phieu-thu", "TAI_CHINH"],
  ["/api/phieu-chi", "TAI_CHINH"],
  ["/api/tai-chinh", "TAI_CHINH"],
  ["/api/sanpham", "SAN_PHAM"],
  ["/api/loaisanpham", "SAN_PHAM"],
  ["/api/tickets", "TICKET"],
  ["/api/loai-ticket", "TICKET"],
  ["/api/ticket-phan-hoi", "TICKET"],
  ["/api/cohoi", "CO_HOI"],
  ["/api/bao-gia", "BAO_GIA"],
  ["/api/bao-cao-thong-ke", "BAO_CAO"],
  ["/api/nhan-vien", "NHAN_VIEN"],
];

export function getToken() {
  return localStorage.getItem("crm_token");
}

export function setSession(loginResult) {
  localStorage.setItem("crm_token", loginResult.token ?? loginResult.accessToken);
  localStorage.setItem("crm_user", JSON.stringify(loginResult.user));
  localStorage.setItem("crm_permissions", JSON.stringify(loginResult.permissions ?? []));
}

export function clearSession() {
  localStorage.removeItem("crm_token");
  localStorage.removeItem("crm_user");
  localStorage.removeItem("crm_permissions");
  localStorage.removeItem("crm_admin_selected_user_id");
  localStorage.removeItem("crm_admin_permissions_drafts");
  localStorage.removeItem("crm_admin_permissions_draft");
}

export function getCurrentUser() {
  try {
    return JSON.parse(localStorage.getItem("crm_user") || "null");
  } catch {
    return null;
  }
}

export function getPermissions() {
  try {
    return JSON.parse(localStorage.getItem("crm_permissions") || "[]");
  } catch {
    return [];
  }
}

export function isAdminUser(user = getCurrentUser()) {
  return Boolean(
    user?.admin ||
      user?.roleId === 1 ||
      String(user?.roleName || user?.roleCode || "").toLowerCase() === "admin",
  );
}

function permissionKeys(permission) {
  return [
    permission?.moduleKey,
    permission?.moduleCode,
    permission?.module,
    permission?.code,
  ].filter(Boolean).map(String);
}

function hasPermissionForModule(permission, moduleKey) {
  const aliases = new Set([moduleKey, ...(MODULE_ALIASES[moduleKey] ?? [])].map(String));
  return permissionKeys(permission).some((key) => aliases.has(key));
}

export function getModulePermission(moduleKey, permissions = getPermissions()) {
  return permissions.find((permission) => hasPermissionForModule(permission, moduleKey));
}

export function canAccessModule(moduleKey, user = getCurrentUser(), permissions = getPermissions()) {
  if (isAdminUser(user)) return true;
  const permission = getModulePermission(moduleKey, permissions);
  return Boolean(
    permission?.canView ||
      permission?.canRead ||
      permission?.canWrite ||
      permission?.canCreate ||
      permission?.canUpdate ||
      permission?.canDelete,
  );
}

export function canWriteModule(moduleKey, user = getCurrentUser(), permissions = getPermissions()) {
  if (isAdminUser(user)) return true;
  const permission = getModulePermission(moduleKey, permissions);
  return Boolean(
    permission?.canWrite ||
      permission?.canCreate ||
      permission?.canUpdate ||
      permission?.canDelete,
  );
}

function canWriteModuleForMethod(moduleKey, method) {
  if (isAdminUser()) return true;
  const permission = getModulePermission(moduleKey);
  if (permission?.canWrite) return true;

  const normalizedMethod = String(method || "GET").toUpperCase();
  if (normalizedMethod === "POST") return Boolean(permission?.canCreate);
  if (normalizedMethod === "PUT" || normalizedMethod === "PATCH") return Boolean(permission?.canUpdate);
  if (normalizedMethod === "DELETE") return Boolean(permission?.canDelete);
  return false;
}

function pathFromUrl(input) {
  try {
    return new URL(input, API_BASE_URL).pathname;
  } catch {
    return String(input || "");
  }
}

function moduleFromUrl(input) {
  const path = pathFromUrl(input);
  return PATH_MODULES.find(([prefix]) => path === prefix || path.startsWith(`${prefix}/`))?.[1] ?? null;
}

function isWriteMethod(method = "GET") {
  return !["GET", "HEAD", "OPTIONS"].includes(String(method).toUpperCase());
}

function assertCanWriteRequest(input, method = "GET") {
  if (!isWriteMethod(method)) return;
  const path = pathFromUrl(input);
  if (path.startsWith("/api/auth/") || path.startsWith("/api/admin/")) return;

  const moduleKey = moduleFromUrl(input);
  if (moduleKey === "SAN_PHAM" && path.includes("/hinhanh") && !canWriteModule(moduleKey)) {
    throw new Error("Bạn không có quyền ghi nên không thể thêm, sửa hoặc xóa dữ liệu.");
  }
  if (moduleKey === "SAN_PHAM" && path.includes("/hinhanh")) {
    return;
  }
  if (moduleKey === "TICKET" && path.includes("/attachments") && !canWriteModule(moduleKey)) {
    throw new Error("Bạn không có quyền ghi nên không thể thêm, sửa hoặc xóa dữ liệu.");
  }
  if (moduleKey === "TICKET" && path.includes("/attachments")) {
    return;
  }
  if (moduleKey && !canWriteModuleForMethod(moduleKey, method)) {
    throw new Error("Bạn không có quyền ghi nên không thể thêm, sửa hoặc xóa dữ liệu.");
  }
}

export const api = axios.create({ baseURL: API_BASE_URL });

api.interceptors.request.use((config) => {
  assertCanWriteRequest(config.url, config.method);
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export function authFetch(input, init = {}) {
  try {
    assertCanWriteRequest(input, init.method || "GET");
  } catch (error) {
    return Promise.resolve(
      new Response(JSON.stringify({ message: error.message }), {
        status: 403,
        headers: { "Content-Type": "application/json" },
      }),
    );
  }
  const token = getToken();
  const headers = new Headers(init.headers || {});
  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }
  return fetch(input, { ...init, headers });
}
