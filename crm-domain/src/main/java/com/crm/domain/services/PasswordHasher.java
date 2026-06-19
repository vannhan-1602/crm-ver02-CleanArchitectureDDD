package com.crm.domain.services;

public interface PasswordHasher {
    boolean matches(String rawPassword, String passwordHash);
}
