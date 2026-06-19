package com.crm.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class TokenService {
    private final String secret;
    private final long ttlSeconds;

    public TokenService(
            @Value("${auth.token.secret:crm-dev-secret-change-me}") String secret,
            @Value("${auth.token.ttl-seconds:86400}") long ttlSeconds) {
        this.secret = secret;
        this.ttlSeconds = ttlSeconds;
    }

    public String create(AuthUser user) {
        long expiresAt = Instant.now().plusSeconds(ttlSeconds).getEpochSecond();
        String payload = user.getId() + "|" + user.getUsername() + "|"
                + safe(user.getRoleId()) + "|" + safe(user.getRoleName()) + "|" + expiresAt;
        String encodedPayload = base64Url(payload.getBytes(StandardCharsets.UTF_8));
        return encodedPayload + "." + sign(encodedPayload);
    }

    public AuthUser verify(String token) {
        if (token == null || !token.contains(".")) {
            throw new IllegalArgumentException("Token khong hop le");
        }
        String[] parts = token.split("\\.", 2);
        if (!constantTimeEquals(sign(parts[0]), parts[1])) {
            throw new IllegalArgumentException("Chu ky token khong hop le");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        String[] values = payload.split("\\|", -1);
        if (values.length != 5) {
            throw new IllegalArgumentException("Payload token khong hop le");
        }
        long expiresAt = Long.parseLong(values[4]);
        if (expiresAt < Instant.now().getEpochSecond()) {
            throw new IllegalArgumentException("Token da het han");
        }
        Integer roleId = values[2].isBlank() ? null : Integer.valueOf(values[2]);
        String roleName = values[3].isBlank() ? null : values[3];
        return new AuthUser(Integer.valueOf(values[0]), values[1], roleId, roleName);
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Url(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Khong the tao chu ky token", ex);
        }
    }

    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString();
    }

    private boolean constantTimeEquals(String expected, String actual) {
        if (expected.length() != actual.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < expected.length(); i++) {
            result |= expected.charAt(i) ^ actual.charAt(i);
        }
        return result == 0;
    }
}
