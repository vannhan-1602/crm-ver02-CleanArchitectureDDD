package com.crm.api.security;

public class ModulePermission {
    private String moduleKey;
    private boolean canView;
    private boolean canRead;
    private boolean canWrite;

    public ModulePermission() {
    }

    public ModulePermission(String moduleKey, boolean canView, boolean canRead, boolean canWrite) {
        this.moduleKey = moduleKey;
        this.canView = canView;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public boolean isCanView() {
        return canView;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }
}
