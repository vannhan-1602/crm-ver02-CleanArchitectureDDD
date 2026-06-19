package com.crm.application.auth.query;

import com.crm.application.auth.dto.UserResponse;
import com.crm.application.common.IRequest;

import java.util.List;

public class GetUsersQuery implements IRequest<List<UserResponse>> {
    private final String roleCode;
    private final String chucVu;
    private final String phongBan;

    public GetUsersQuery(String roleCode, String chucVu, String phongBan) {
        this.roleCode = roleCode;
        this.chucVu = chucVu;
        this.phongBan = phongBan;
    }

    public String getRoleCode() { return roleCode; }
    public String getChucVu() { return chucVu; }
    public String getPhongBan() { return phongBan; }
}
