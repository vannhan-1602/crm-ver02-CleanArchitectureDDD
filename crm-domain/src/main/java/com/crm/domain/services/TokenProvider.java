package com.crm.domain.services;

import com.crm.domain.entities.CrmUser;

public interface TokenProvider {
    String generateAccessToken(CrmUser user);
    boolean validateToken(String token);
    Integer getUserId(String token);
}
