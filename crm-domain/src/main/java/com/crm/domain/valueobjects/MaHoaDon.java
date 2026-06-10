package com.crm.domain.valueobjects;

public class MaHoaDon {
    private final String value;

    public MaHoaDon(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ma hoa don khong duoc rong");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Ma hoa don khong duoc vuot qua 50 ky tu");
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
