package com.crm.application.auth.query;

import com.crm.application.auth.dto.UserResponse;
import com.crm.application.common.IRequest;

public class GetUserByIdQuery implements IRequest<UserResponse> {
    private final Integer userId;

    public GetUserByIdQuery(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() { return userId; }
}
