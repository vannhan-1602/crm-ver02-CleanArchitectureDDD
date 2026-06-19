package com.crm.application.auth;

import java.util.Set;

public final class RoleCodes {
    public static final String ADMIN = "admin";
    public static final String MANAGER = "manager";
    public static final String SALE = "sale";
    public static final String ACCOUNTANT = "accountant";

    private static final Set<String> ALL = Set.of(ADMIN, MANAGER, SALE, ACCOUNTANT);

    private RoleCodes() {
    }

    public static boolean isValid(String roleCode) {
        return roleCode != null && ALL.contains(roleCode.trim().toLowerCase());
    }

    public static String normalize(String roleCode) {
        if (roleCode == null || roleCode.isBlank()) {
            return SALE;
        }
        String normalized = roleCode.trim().toLowerCase();
        if (!isValid(normalized)) {
            throw new IllegalArgumentException("Role khong hop le");
        }
        return normalized;
    }
}
