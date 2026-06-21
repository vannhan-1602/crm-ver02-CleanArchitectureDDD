package com.crm.api.controllers;

import com.crm.api.security.AuthInterceptor;
import com.crm.api.security.AuthService;
import com.crm.api.security.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthService.LoginResult login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }

    @PostMapping("/logout")
    public Map<String, String> logout() {
        return Map.of("message", "Dang xuat thanh cong");
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request) {
        AuthUser user = (AuthUser) request.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        return Map.of(
                "user", user,
                "permissions", authService.getEffectivePermissions(user)
        );
    }

    static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
