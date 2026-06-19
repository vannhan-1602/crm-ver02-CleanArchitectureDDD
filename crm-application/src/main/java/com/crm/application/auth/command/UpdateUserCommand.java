package com.crm.application.auth.command;

import com.crm.application.auth.dto.UserResponse;
import com.crm.application.common.IRequest;

public class UpdateUserCommand implements IRequest<UserResponse> {
    private final Integer id;
    private final String username;
    private final String roleCode;
    private final String chucVu;
    private final String phongBan;
    private final Boolean active;

    public UpdateUserCommand(Integer id, String username, String roleCode,
                             String chucVu, String phongBan, Boolean active) {
        this.id = id;
        this.username = username;
        this.roleCode = roleCode;
        this.chucVu = chucVu;
        this.phongBan = phongBan;
        this.active = active;
    }

    public Integer getId() { return id; }
    public String getUsername() { return username; }
    public String getRoleCode() { return roleCode; }
    public String getChucVu() { return chucVu; }
    public String getPhongBan() { return phongBan; }
    public Boolean getActive() { return active; }
}
