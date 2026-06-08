package com.crm.domain.valueobjects;

public enum TrangThaiHopDong {
    DangThucHien,
    TamDung,
    ThanhLy;

    public static TrangThaiHopDong from(String value) {
        if (value == null || value.isBlank()) {
            return DangThucHien;
        }
        try {
            return TrangThaiHopDong.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return DangThucHien;
        }
    }
}
