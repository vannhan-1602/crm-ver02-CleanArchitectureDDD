package com.crm.application.auth.dto;

import com.crm.domain.entities.UserPermission;

public class PermissionDto {
    private String moduleCode;
    private Boolean canView;
    private Boolean canCreate;
    private Boolean canUpdate;
    private Boolean canDelete;

    public PermissionDto() {
    }

    public PermissionDto(String moduleCode, Boolean canView, Boolean canCreate,
                         Boolean canUpdate, Boolean canDelete) {
        this.moduleCode = moduleCode;
        this.canView = canView;
        this.canCreate = canCreate;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
    }

    public static PermissionDto from(UserPermission permission) {
        return new PermissionDto(
                permission.getModuleCode(),
                permission.getCanView(),
                permission.getCanCreate(),
                permission.getCanUpdate(),
                permission.getCanDelete()
        );
    }

    public UserPermission toDomain() {
        return new UserPermission(moduleCode, canView, canCreate, canUpdate, canDelete);
    }

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
