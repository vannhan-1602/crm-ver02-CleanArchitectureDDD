package com.crm.domain.repositories;

import com.crm.domain.entities.UserPermission;

import java.util.List;

public interface UserPermissionRepository {
    List<UserPermission> findByUserId(Integer userId);
    void replacePermissions(Integer userId, List<UserPermission> permissions);
}
