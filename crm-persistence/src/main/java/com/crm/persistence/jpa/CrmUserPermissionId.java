package com.crm.persistence.jpa;

import java.io.Serializable;
import java.util.Objects;

public class CrmUserPermissionId implements Serializable {
    private Integer userId;
    private String moduleCode;

    public CrmUserPermissionId() {
    }

    public CrmUserPermissionId(Integer userId, String moduleCode) {
        this.userId = userId;
        this.moduleCode = moduleCode;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrmUserPermissionId that)) return false;
        return Objects.equals(userId, that.userId)
                && Objects.equals(moduleCode, that.moduleCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, moduleCode);
    }
}
