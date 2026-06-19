package com.crm.domain.valueobjects;

public enum NguonTiepNhanTicket {
    Email,
    Phone,
    Web,
    Zalo,
    TrucTiep;

    public static NguonTiepNhanTicket from(String value) {
        if (value == null || value.isBlank()) {
            return Phone;
        }
        try {
            return NguonTiepNhanTicket.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("NguonTiepNhan khong hop le: " + value);
        }
    }
}
