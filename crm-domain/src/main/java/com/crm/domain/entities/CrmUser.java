package com.crm.domain.entities;

import java.util.ArrayList;
import java.util.List;

public class CrmUser {
    private Integer id;
    private String hoTen;
    private String username;
    private String passwordHash;
    private String roleCode;
    private String chucVu;
    private String phongBan;
    private Boolean active;
    private List<UserPermission> permissions = new ArrayList<>();

    public CrmUser() {
    }

    public CrmUser(Integer id, String hoTen, String username, String passwordHash,
                   String roleCode, String chucVu, String phongBan, Boolean active) {
        this.id = id;
        this.hoTen = hoTen;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleCode = roleCode;
        this.chucVu = chucVu;
        this.phongBan = phongBan;
        this.active = active;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    public String getPhongBan() { return phongBan; }
    public void setPhongBan(String phongBan) { this.phongBan = phongBan; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public List<UserPermission> getPermissions() { return permissions; }
    public void setPermissions(List<UserPermission> permissions) {
        this.permissions = permissions == null ? new ArrayList<>() : permissions;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}
