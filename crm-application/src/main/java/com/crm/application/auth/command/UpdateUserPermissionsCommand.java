package com.crm.application.auth.command;

import com.crm.application.auth.dto.PermissionDto;
import com.crm.application.auth.dto.UserResponse;
import com.crm.application.common.IRequest;

import java.util.List;

public class UpdateUserPermissionsCommand implements IRequest<UserResponse> {
    private final Integer userId;
    private final List<PermissionDto> permissions;

    public UpdateUserPermissionsCommand(Integer userId, List<PermissionDto> permissions) {
        this.userId = userId;
        this.permissions = permissions;
    }

    public Integer getUserId() { return userId; }
    public List<PermissionDto> getPermissions() { return permissions; }
}
