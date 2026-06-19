import axios from "axios";

export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081";

export function getToken() {
  return localStorage.getItem("crm_token");
}

export function setSession(loginResult) {
  localStorage.setItem("crm_token", loginResult.token);
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

export const api = axios.create({ baseURL: API_BASE_URL });

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export function authFetch(input, init = {}) {
  const token = getToken();
  const headers = new Headers(init.headers || {});
  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }
  return fetch(input, { ...init, headers });
}
