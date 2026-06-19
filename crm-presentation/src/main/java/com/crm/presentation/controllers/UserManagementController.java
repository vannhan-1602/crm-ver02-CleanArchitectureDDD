package com.crm.presentation.controllers;

import com.crm.application.auth.command.UpdateUserCommand;
import com.crm.application.auth.command.UpdateUserPermissionsCommand;
import com.crm.application.auth.dto.PermissionDto;
import com.crm.application.auth.dto.UserResponse;
import com.crm.application.auth.query.GetUserByIdQuery;
import com.crm.application.auth.query.GetUsersQuery;
import com.crm.application.common.Mediator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserManagementController {
    private final Mediator mediator;

    public UserManagementController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping
    public List<UserResponse> getUsers(@RequestParam(required = false) String roleCode,
                                       @RequestParam(required = false) String chucVu,
                                       @RequestParam(required = false) String phongBan) {
        return mediator.send(new GetUsersQuery(roleCode, chucVu, phongBan));
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Integer id) {
        return mediator.send(new GetUserByIdQuery(id));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Integer id, @RequestBody UpdateUserRequest request) {
        return mediator.send(new UpdateUserCommand(
                id,
                request.getUsername(),
                request.getRoleCode(),
                request.getChucVu(),
                request.getPhongBan(),
                request.getActive()
        ));
    }

    @PutMapping("/{id}/permissions")
    public UserResponse updatePermissions(@PathVariable Integer id,
                                          @RequestBody UpdatePermissionsRequest request) {
        return mediator.send(new UpdateUserPermissionsCommand(id, request.getPermissions()));
    }

    static class UpdateUserRequest {
        private String username;
        private String roleCode;
        private String chucVu;
        private String phongBan;
        private Boolean active;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRoleCode() { return roleCode; }
        public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
        public String getChucVu() { return chucVu; }
        public void setChucVu(String chucVu) { this.chucVu = chucVu; }
        public String getPhongBan() { return phongBan; }
        public void setPhongBan(String phongBan) { this.phongBan = phongBan; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }

    static class UpdatePermissionsRequest {
        private List<PermissionDto> permissions;

        public List<PermissionDto> getPermissions() { return permissions; }
        public void setPermissions(List<PermissionDto> permissions) { this.permissions = permissions; }
    }
}
