package com.crm.application.auth.handler;

import com.crm.application.auth.RoleCodes;
import com.crm.application.auth.command.UpdateUserCommand;
import com.crm.application.auth.dto.UserResponse;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.CrmUser;
import com.crm.domain.repositories.CrmUserAuthRepository;
import com.crm.domain.repositories.UserPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserCommandHandler implements IRequestHandler<UpdateUserCommand, UserResponse> {
    private final CrmUserAuthRepository userRepository;
    private final UserPermissionRepository permissionRepository;

    public UpdateUserCommandHandler(CrmUserAuthRepository userRepository,
                                    UserPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserResponse handle(UpdateUserCommand command) {
        CrmUser user = userRepository.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay user"));

        user.setUsername(normalizeUsername(command.getUsername(), user.getId()));
        user.setRoleCode(RoleCodes.normalize(command.getRoleCode()));
        user.setChucVu(command.getChucVu());
        user.setPhongBan(command.getPhongBan());
        user.setActive(command.getActive() == null || command.getActive());

        CrmUser saved = userRepository.saveProfile(user);
        return AuthResponseMapper.toResponse(saved, permissionRepository);
    }

    private String normalizeUsername(String username, Integer userId) {
        if (username == null || username.isBlank()) {
            return "user" + userId;
        }
        return username.trim();
    }
}
