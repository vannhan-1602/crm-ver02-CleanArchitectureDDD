package com.crm.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "CRM_UserPermission")
@IdClass(CrmUserPermissionId.class)
public class CrmUserPermissionJpaEntity {
    @Id
    @Column(name = "User_Id")
    private Integer userId;

    @Id
    @Column(name = "ModuleCode", length = 50)
    private String moduleCode;

    @Column(name = "CanView", nullable = false)
    private Boolean canView;

    @Column(name = "CanCreate", nullable = false)
    private Boolean canCreate;

    @Column(name = "CanUpdate", nullable = false)
    private Boolean canUpdate;

    @Column(name = "CanDelete", nullable = false)
    private Boolean canDelete;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public Boolean getCanView() { return canView; }
    public void setCanView(Boolean canView) { this.canView = canView; }
    public Boolean getCanCreate() { return canCreate; }
    public void setCanCreate(Boolean canCreate) { this.canCreate = canCreate; }
    public Boolean getCanUpdate() { return canUpdate; }
    public void setCanUpdate(Boolean canUpdate) { this.canUpdate = canUpdate; }
    public Boolean getCanDelete() { return canDelete; }
    public void setCanDelete(Boolean canDelete) { this.canDelete = canDelete; }
}
