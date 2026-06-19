package com.crm.application.auth.handler;

import com.crm.application.auth.RoleCodes;
import com.crm.application.auth.dto.UserResponse;
import com.crm.application.auth.query.GetUsersQuery;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.repositories.CrmUserAuthRepository;
import com.crm.domain.repositories.UserPermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUsersQueryHandler implements IRequestHandler<GetUsersQuery, List<UserResponse>> {
    private final CrmUserAuthRepository userRepository;
    private final UserPermissionRepository permissionRepository;

    public GetUsersQueryHandler(CrmUserAuthRepository userRepository,
                                UserPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<UserResponse> handle(GetUsersQuery query) {
        String roleCode = query.getRoleCode();
        if (roleCode != null && !roleCode.isBlank()) {
            roleCode = RoleCodes.normalize(roleCode);
        }
        return userRepository.findUsers(roleCode, query.getChucVu(), query.getPhongBan())
                .stream()
                .map(user -> AuthResponseMapper.toResponse(user, permissionRepository))
                .toList();
    }
}
