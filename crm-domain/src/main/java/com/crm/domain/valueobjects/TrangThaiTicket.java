package com.crm.domain.valueobjects;

public enum TrangThaiTicket {
    Moi,
    DangXuLy,
    ChoPhanHoi,
    Dong;

    public static TrangThaiTicket from(String value) {
        if (value == null || value.isBlank()) {
            return Moi;
        }
        try {
            return TrangThaiTicket.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("TrangThai khong hop le: " + value);
        }
    }
}
