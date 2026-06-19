package com.crm.domain.valueobjects;

public enum MucDoUuTienTicket {
    Thap,
    TrungBinh,
    Cao,
    KhanCap;

    public static MucDoUuTienTicket from(String value) {
        if (value == null || value.isBlank()) {
            return TrungBinh;
        }
        try {
            return MucDoUuTienTicket.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("MucDoUuTien khong hop le: " + value);
        }
    }
}
