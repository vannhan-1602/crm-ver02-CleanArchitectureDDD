package com.crm.api.controllers;

import com.crm.api.security.AuthService;
import com.crm.api.security.ModulePermission;
import com.crm.api.security.ModuleRegistry;
import com.crm.persistence.jpa.HtUserJpaEntity;
import com.crm.persistence.repositories.HtUserJPARepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminUserController {
    private final AuthService authService;
    private final ModuleRegistry moduleRegistry;

    public AdminUserController(AuthService authService, ModuleRegistry moduleRegistry) {
        this.authService = authService;
        this.moduleRegistry = moduleRegistry;
    }

    @GetMapping("/modules")
    public Map<String, String> modules() {
        return moduleRegistry.getPathToModule();
    }

    @GetMapping("/users")
    public List<UserSummaryResponse> users() {
        return authService.getUsers().stream()
                .map(UserSummaryResponse::from)
                .toList();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedResponse createUser(@RequestBody AuthService.CreateUserInput request) {
        HtUserJpaEntity user = authService.createUser(request);
        return new UserCreatedResponse(user.getId(), user.getUsername(), user.getNhanSuId(),
                user.getRoleId(), user.getTrangThai());
    }

    @GetMapping("/users/{id}/permissions")
    public List<ModulePermission> getPermissions(@PathVariable Integer id) {
        return authService.getPermissions(id);
    }

    @PutMapping("/users/{id}/permissions")
    public List<ModulePermission> updatePermissions(@PathVariable Integer id,
                                                    @RequestBody PermissionsRequest request) {
        return authService.replacePermissions(id, request.getPermissions());
    }

    static class PermissionsRequest {
        private List<ModulePermission> permissions = List.of();

        public List<ModulePermission> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<ModulePermission> permissions) {
            this.permissions = permissions == null ? List.of() : permissions;
        }
    }

    record UserCreatedResponse(Integer id, String username, Integer nhanSuId,
                               Integer roleId, String trangThai) {
    }

    static class UserSummaryResponse {
        private Integer id;
        private String username;
        private Integer nhanSuId;
        private String hoTen;
        private Integer roleId;
        private String roleName;
        private String trangThai;

        static UserSummaryResponse from(HtUserJPARepo.UserSummaryProjection projection) {
            UserSummaryResponse response = new UserSummaryResponse();
            response.id = projection.getId();
            response.username = projection.getUsername();
            response.nhanSuId = projection.getNhanSuId();
            response.hoTen = projection.getHoTen();
            response.roleId = projection.getRoleId();
            response.roleName = projection.getRoleName();
            response.trangThai = projection.getTrangThai();
            return response;
        }

        public Integer getId() { return id; }
        public String getUsername() { return username; }
        public Integer getNhanSuId() { return nhanSuId; }
        public String getHoTen() { return hoTen; }
        public Integer getRoleId() { return roleId; }
        public String getRoleName() { return roleName; }
        public String getTrangThai() { return trangThai; }
    }
}
