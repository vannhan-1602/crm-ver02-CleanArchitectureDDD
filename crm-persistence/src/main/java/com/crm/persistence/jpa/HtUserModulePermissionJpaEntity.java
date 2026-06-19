package com.crm.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "HT_UserModulePermission")
public class HtUserModulePermissionJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "User_Id", nullable = false)
    private Integer userId;

    @Column(name = "ModuleKey", nullable = false, length = 80)
    private String moduleKey;

    @Column(name = "CanView", nullable = false)
    private Boolean canView = false;

    @Column(name = "CanRead", nullable = false)
    private Boolean canRead = false;

    @Column(name = "CanWrite", nullable = false)
    private Boolean canWrite = false;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public Boolean getCanView() {
        return canView;
    }

    public Boolean getCanRead() {
        return canRead;
    }

    public Boolean getCanWrite() {
        return canWrite;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public void setCanView(Boolean canView) {
        this.canView = canView;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
