package com.crm.presentation.controllers;

import com.crm.application.auth.command.LoginCommand;
import com.crm.application.auth.dto.LoginResponse;
import com.crm.application.auth.dto.UserResponse;
import com.crm.application.auth.query.GetCurrentUserQuery;
import com.crm.application.common.Mediator;
import com.crm.presentation.security.AuthenticatedUserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {
    private final Mediator mediator;

    public AuthController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return mediator.send(new LoginCommand(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
    }

    @GetMapping("/me")
    public UserResponse me(Principal principal) {
        if (!(principal instanceof org.springframework.security.core.Authentication authentication)
                || !(authentication.getPrincipal() instanceof AuthenticatedUserPrincipal user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chua dang nhap");
        }
        return mediator.send(new GetCurrentUserQuery(user.getUserId()));
    }

    static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
