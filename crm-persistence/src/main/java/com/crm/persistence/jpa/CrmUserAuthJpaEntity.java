package com.crm.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CRM_UserAuth")
public class CrmUserAuthJpaEntity {
    @Id
    @Column(name = "User_Id")
    private Integer userId;

    @Column(name = "Username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "PasswordHash", length = 255)
    private String passwordHash;

    @Column(name = "RoleCode", nullable = false, length = 30)
    private String roleCode;

    @Column(name = "ChucVu", length = 100)
    private String chucVu;

    @Column(name = "PhongBan", length = 100)
    private String phongBan;

    @Column(name = "Active", nullable = false)
    private Boolean active;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
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
}
