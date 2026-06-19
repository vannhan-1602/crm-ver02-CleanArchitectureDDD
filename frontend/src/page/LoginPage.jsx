import { useState } from "react";

const API_BASE_URL = "http://localhost:8081";

export default function LoginPage({ onLogin }) {
  const [form, setForm] = useState({ username: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const submit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      const res = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      if (!res.ok) throw new Error("Tên đăng nhập hoặc mật khẩu không đúng");
      const data = await res.json();
      onLogin(data);
    } catch (err) {
      setError(err.message || "Đăng nhập thất bại");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.wrap}>
      <form onSubmit={submit} style={styles.form}>
        <h2 style={styles.title}>Đăng nhập CRM</h2>
        <label style={styles.label}>Tên đăng nhập</label>
        <input
          value={form.username}
          onChange={(e) => setForm((prev) => ({ ...prev, username: e.target.value }))}
          style={styles.input}
          autoComplete="username"
        />
        <label style={styles.label}>Mật khẩu</label>
        <input
          type="password"
          value={form.password}
          onChange={(e) => setForm((prev) => ({ ...prev, password: e.target.value }))}
          style={styles.input}
          autoComplete="current-password"
        />
        {error && <div style={styles.error}>{error}</div>}
        <button disabled={loading} style={styles.button}>
          {loading ? "Đang đăng nhập..." : "Đăng nhập"}
        </button>
      </form>
    </div>
  );
}

const styles = {
  wrap: {
    minHeight: "calc(100vh - 100px)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  form: {
    width: "360px",
    background: "#fff",
    border: "1px solid #e5e7eb",
    borderRadius: "8px",
    padding: "24px",
    boxShadow: "0 10px 30px rgba(15, 23, 42, 0.08)",
  },
  title: { margin: "0 0 20px", fontSize: "22px", color: "#111827" },
  label: { display: "block", marginBottom: "6px", fontSize: "14px", color: "#374151" },
  input: {
    width: "100%",
    boxSizing: "border-box",
    padding: "10px 12px",
    marginBottom: "14px",
    border: "1px solid #d1d5db",
    borderRadius: "6px",
    fontSize: "14px",
  },
  error: {
    background: "#fef2f2",
    color: "#b91c1c",
    padding: "10px 12px",
    borderRadius: "6px",
    marginBottom: "14px",
    fontSize: "14px",
  },
  button: {
    width: "100%",
    border: "none",
    borderRadius: "6px",
    padding: "11px 14px",
    background: "#2563eb",
    color: "#fff",
    fontWeight: 700,
    cursor: "pointer",
  },
};
