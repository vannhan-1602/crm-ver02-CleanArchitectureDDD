package com.crm.application.auth.command;

import com.crm.application.auth.dto.LoginResponse;
import com.crm.application.common.IRequest;

public class LoginCommand implements IRequest<LoginResponse> {
    private final String username;
    private final String password;

    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
