package com.crm.presentation.security;

public class AuthenticatedUserPrincipal {
    private final Integer userId;
    private final String username;
    private final String roleCode;

    public AuthenticatedUserPrincipal(Integer userId, String username, String roleCode) {
        this.userId = userId;
        this.username = username;
        this.roleCode = roleCode;
    }

    public Integer getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRoleCode() { return roleCode; }
}
