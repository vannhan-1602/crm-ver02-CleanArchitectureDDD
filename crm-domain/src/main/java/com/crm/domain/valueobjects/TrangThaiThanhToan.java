package com.crm.domain.valueobjects;

public enum TrangThaiThanhToan {
    ChuaThanhToan,
    ThanhToan1Phan,
    HoanTat;

    public static TrangThaiThanhToan from(String value) {
        if (value == null || value.isBlank()) {
            return ChuaThanhToan;
        }
        try {
            return TrangThaiThanhToan.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return ChuaThanhToan;
        }
    }
}
