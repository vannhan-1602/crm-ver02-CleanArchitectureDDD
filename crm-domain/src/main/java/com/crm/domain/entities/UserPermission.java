package com.crm.domain.entities;

public class UserPermission {
    private String moduleCode;
    private Boolean canView;
    private Boolean canCreate;
    private Boolean canUpdate;
    private Boolean canDelete;

    public UserPermission() {
    }

    public UserPermission(String moduleCode, Boolean canView, Boolean canCreate,
                          Boolean canUpdate, Boolean canDelete) {
        this.moduleCode = moduleCode;
        this.canView = canView;
        this.canCreate = canCreate;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
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

    public boolean canView() { return Boolean.TRUE.equals(canView); }
    public boolean canCreate() { return Boolean.TRUE.equals(canCreate); }
    public boolean canUpdate() { return Boolean.TRUE.equals(canUpdate); }
    public boolean canDelete() { return Boolean.TRUE.equals(canDelete); }
}
