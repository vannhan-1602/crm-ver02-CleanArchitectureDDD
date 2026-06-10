package com.crm.domain.valueobjects;

public class MaPhieuChi {
    private final String value;

    public MaPhieuChi(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ma phieu chi khong duoc rong");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Ma phieu chi khong duoc vuot qua 50 ky tu");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
