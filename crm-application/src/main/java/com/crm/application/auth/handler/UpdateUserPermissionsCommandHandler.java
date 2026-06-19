package com.crm.application.auth.handler;

import com.crm.application.auth.PermissionCatalog;
import com.crm.application.auth.command.UpdateUserPermissionsCommand;
import com.crm.application.auth.dto.PermissionDto;
import com.crm.application.auth.dto.UserResponse;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.CrmUser;
import com.crm.domain.entities.UserPermission;
import com.crm.domain.repositories.CrmUserAuthRepository;
import com.crm.domain.repositories.UserPermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateUserPermissionsCommandHandler implements IRequestHandler<UpdateUserPermissionsCommand, UserResponse> {
    private final CrmUserAuthRepository userRepository;
    private final UserPermissionRepository permissionRepository;

    public UpdateUserPermissionsCommandHandler(CrmUserAuthRepository userRepository,
                                               UserPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserResponse handle(UpdateUserPermissionsCommand command) {
        CrmUser user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay user"));

        List<UserPermission> permissions = command.getPermissions().stream()
                .map(this::sanitize)
                .toList();

        permissionRepository.replacePermissions(command.getUserId(), permissions);
        return AuthResponseMapper.toResponse(user, permissionRepository);
    }

    private UserPermission sanitize(PermissionDto dto) {
        if (!PermissionCatalog.isValidModule(dto.getModuleCode())) {
            throw new IllegalArgumentException("Module quyen khong hop le");
        }
        boolean canCreate = Boolean.TRUE.equals(dto.getCanCreate());
        boolean canUpdate = Boolean.TRUE.equals(dto.getCanUpdate());
        boolean canDelete = Boolean.TRUE.equals(dto.getCanDelete());
        boolean canView = Boolean.TRUE.equals(dto.getCanView()) || canCreate || canUpdate || canDelete;

        return new UserPermission(dto.getModuleCode(), canView, canCreate, canUpdate, canDelete);
    }
}
