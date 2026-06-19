package com.crm.application.auth;

import com.crm.domain.entities.UserPermission;

import java.util.List;
import java.util.Set;

public final class PermissionCatalog {
    public static final List<ModuleInfo> MODULES = List.of(
            new ModuleInfo("leads", "Quan ly Lead"),
            new ModuleInfo("khach-hang", "Quan ly Khach hang"),
            new ModuleInfo("hoat-dong", "Quan ly Hoat dong"),
            new ModuleInfo("hop-dong", "Quan ly Hop dong"),
            new ModuleInfo("tai-chinh", "Quan ly Hoa don"),
            new ModuleInfo("sanpham", "Quan ly San pham"),
            new ModuleInfo("tickets", "Quan ly Ticket"),
            new ModuleInfo("cohoi", "Quan ly Co hoi ban hang"),
            new ModuleInfo("baogia", "Quan ly Bao gia"),
            new ModuleInfo("bao-cao-thong-ke", "Bao cao thong ke")
    );

    private static final Set<String> MODULE_CODES = MODULES.stream()
            .map(ModuleInfo::getCode)
            .collect(java.util.stream.Collectors.toUnmodifiableSet());

    private PermissionCatalog() {
    }

    public static boolean isValidModule(String moduleCode) {
        return moduleCode != null && MODULE_CODES.contains(moduleCode);
    }

    public static List<UserPermission> fullPermissions() {
        return MODULES.stream()
                .map(module -> new UserPermission(module.getCode(), true, true, true, true))
                .toList();
    }

    public static class ModuleInfo {
        private final String code;
        private final String name;

        public ModuleInfo(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
    }
}
