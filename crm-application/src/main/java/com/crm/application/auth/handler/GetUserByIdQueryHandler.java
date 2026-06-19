package com.crm.application.auth.handler;

import com.crm.application.auth.dto.UserResponse;
import com.crm.application.auth.query.GetUserByIdQuery;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.repositories.CrmUserAuthRepository;
import com.crm.domain.repositories.UserPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdQueryHandler implements IRequestHandler<GetUserByIdQuery, UserResponse> {
    private final CrmUserAuthRepository userRepository;
    private final UserPermissionRepository permissionRepository;

    public GetUserByIdQueryHandler(CrmUserAuthRepository userRepository,
                                   UserPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserResponse handle(GetUserByIdQuery query) {
        return AuthResponseMapper.toResponse(
                userRepository.findById(query.getUserId())
                        .orElseThrow(() -> new RuntimeException("Khong tim thay user")),
                permissionRepository
        );
    }
}
