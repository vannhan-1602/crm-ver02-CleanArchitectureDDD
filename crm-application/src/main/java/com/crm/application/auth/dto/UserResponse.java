package com.crm.application.auth.dto;

import com.crm.domain.entities.CrmUser;

import java.util.List;

public class UserResponse {
    private Integer id;
    private String hoTen;
    private String username;
    private String roleCode;
    private String chucVu;
    private String phongBan;
    private Boolean active;
    private List<PermissionDto> permissions;

    public static UserResponse from(CrmUser user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.hoTen = user.getHoTen();
        response.username = user.getUsername();
        response.roleCode = user.getRoleCode();
        response.chucVu = user.getChucVu();
        response.phongBan = user.getPhongBan();
        response.active = user.getActive();
        response.permissions = user.getPermissions().stream()
                .map(PermissionDto::from)
                .toList();
        return response;
    }

    public Integer getId() { return id; }
    public String getHoTen() { return hoTen; }
    public String getUsername() { return username; }
    public String getRoleCode() { return roleCode; }
    public String getChucVu() { return chucVu; }
    public String getPhongBan() { return phongBan; }
    public Boolean getActive() { return active; }
    public List<PermissionDto> getPermissions() { return permissions; }
}
