package com.crm.application.auth.handler;

import com.crm.application.auth.PermissionCatalog;
import com.crm.application.auth.RoleCodes;
import com.crm.application.auth.dto.UserResponse;
import com.crm.domain.entities.CrmUser;
import com.crm.domain.entities.UserPermission;
import com.crm.domain.repositories.UserPermissionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class AuthResponseMapper {
    private AuthResponseMapper() {
    }

    static UserResponse toResponse(CrmUser user, UserPermissionRepository permissionRepository) {
        user.setPermissions(effectivePermissions(user, permissionRepository.findByUserId(user.getId())));
        return UserResponse.from(user);
    }

    static List<UserPermission> effectivePermissions(CrmUser user, List<UserPermission> stored) {
        if (RoleCodes.ADMIN.equals(user.getRoleCode())) {
            return PermissionCatalog.fullPermissions();
        }

        Map<String, UserPermission> byModule = new HashMap<>();
        for (UserPermission permission : stored) {
            byModule.put(permission.getModuleCode(), permission);
        }

        return PermissionCatalog.MODULES.stream()
                .map(module -> byModule.getOrDefault(
                        module.getCode(),
                        new UserPermission(module.getCode(), false, false, false, false)
                ))
                .toList();
    }
}
