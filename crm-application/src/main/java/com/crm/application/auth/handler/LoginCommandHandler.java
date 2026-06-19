package com.crm.application.auth.handler;

import com.crm.application.auth.dto.LoginResponse;
import com.crm.application.auth.command.LoginCommand;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.CrmUser;
import com.crm.domain.repositories.CrmUserAuthRepository;
import com.crm.domain.repositories.UserPermissionRepository;
import com.crm.domain.services.PasswordHasher;
import com.crm.domain.services.TokenProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginCommandHandler implements IRequestHandler<LoginCommand, LoginResponse> {
    private final CrmUserAuthRepository userRepository;
    private final UserPermissionRepository permissionRepository;
    private final PasswordHasher passwordHasher;
    private final TokenProvider tokenProvider;

    public LoginCommandHandler(CrmUserAuthRepository userRepository,
                               UserPermissionRepository permissionRepository,
                               PasswordHasher passwordHasher,
                               TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public LoginResponse handle(LoginCommand request) {
        CrmUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Sai ten dang nhap hoac mat khau"));

        if (!user.isActive() || !passwordHasher.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Sai ten dang nhap hoac mat khau");
        }

        String token = tokenProvider.generateAccessToken(user);
        return new LoginResponse(token, AuthResponseMapper.toResponse(user, permissionRepository));
    }
}
