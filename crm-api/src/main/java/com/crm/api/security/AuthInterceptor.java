package com.crm.api.security;

import com.crm.persistence.repositories.HtUserJPARepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String CURRENT_USER_ATTRIBUTE = "currentUser";

    private final TokenService tokenService;
    private final AuthService authService;
    private final HtUserJPARepo userRepo;
    private final ModuleRegistry moduleRegistry;

    public AuthInterceptor(TokenService tokenService,
                           AuthService authService,
                           HtUserJPARepo userRepo,
                           ModuleRegistry moduleRegistry) {
        this.tokenService = tokenService;
        this.authService = authService;
        this.userRepo = userRepo;
        this.moduleRegistry = moduleRegistry;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod()) || !request.getRequestURI().startsWith("/api/")) {
            return true;
        }
        if ("/api/auth/login".equals(request.getRequestURI())) {
            return true;
        }

        AuthUser tokenUser;
        try {
            tokenUser = tokenService.verify(readBearerToken(request));
        } catch (Exception ex) {
            send(response, HttpServletResponse.SC_UNAUTHORIZED, "Chua dang nhap hoac token khong hop le");
            return false;
        }

        AuthUser currentUser = userRepo.findById(tokenUser.getId())
                .filter(user -> "Active".equalsIgnoreCase(user.getTrangThai()))
                .map(authService::toAuthUser)
                .orElse(null);
        if (currentUser == null) {
            send(response, HttpServletResponse.SC_UNAUTHORIZED, "Tai khoan khong ton tai hoac da bi khoa");
            return false;
        }
        request.setAttribute(CURRENT_USER_ATTRIBUTE, currentUser);

        String uri = request.getRequestURI();
        if (uri.startsWith("/api/auth/")) {
            return true;
        }
        if (uri.startsWith("/api/admin/")) {
            if (currentUser.isAdmin()) {
                return true;
            }
            send(response, HttpServletResponse.SC_FORBIDDEN, "Chi admin moi duoc truy cap chuc nang nay");
            return false;
        }

        String moduleKey = moduleRegistry.findModule(uri).orElse(null);
        if (moduleKey == null || currentUser.isAdmin()) {
            return true;
        }

        String action = actionFromMethod(request.getMethod());
        if (!authService.hasPermission(currentUser, moduleKey, action)) {
            send(response, HttpServletResponse.SC_FORBIDDEN,
                    "Ban khong co quyen " + action + " module " + moduleKey);
            return false;
        }
        return true;
    }

    private String readBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing bearer token");
        }
        return header.substring("Bearer ".length()).trim();
    }

    private String actionFromMethod(String method) {
        if (HttpMethod.GET.matches(method) || HttpMethod.HEAD.matches(method)) {
            return "READ";
        }
        return "WRITE";
    }

    private void send(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
