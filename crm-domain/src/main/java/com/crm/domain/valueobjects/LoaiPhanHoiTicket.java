package com.crm.domain.valueobjects;

public enum LoaiPhanHoiTicket {
    NoiBoXuLy,
    PhanHoiKhachHang,
    YeuCauBoSung,
    DongTicket;

    public static LoaiPhanHoiTicket from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("LoaiPhanHoi khong duoc de trong");
        }
        try {
            return LoaiPhanHoiTicket.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("LoaiPhanHoi khong hop le: " + value);
        }
    }
}
