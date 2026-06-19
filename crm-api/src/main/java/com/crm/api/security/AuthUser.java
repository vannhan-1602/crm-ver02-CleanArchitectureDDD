package com.crm.api.security;

public class AuthUser {
    private final Integer id;
    private final String username;
    private final Integer roleId;
    private final String roleName;

    public AuthUser(Integer id, String username, Integer roleId, String roleName) {
        this.id = id;
        this.username = username;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public boolean isAdmin() {
        return Integer.valueOf(1).equals(roleId) || "Admin".equalsIgnoreCase(roleName);
    }
}
