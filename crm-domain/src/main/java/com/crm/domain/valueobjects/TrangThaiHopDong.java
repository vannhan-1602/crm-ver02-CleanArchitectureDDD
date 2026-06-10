package com.crm.domain.valueobjects;

public enum TrangThaiHopDong {
    DangThucHien,
    TamDung,
    ThanhLy;

    public static TrangThaiHopDong from(String value) {
        if (value == null || value.isBlank()) {
            return DangThucHien;
        }
        String normalized = value.trim();
        for (TrangThaiHopDong trangThaiHopDong : values()) {
            if (trangThaiHopDong.name().equalsIgnoreCase(normalized)) {
                return trangThaiHopDong;
            }
        }
        throw new IllegalArgumentException("Trang thai hop dong khong hop le: " + value);
    }
}
