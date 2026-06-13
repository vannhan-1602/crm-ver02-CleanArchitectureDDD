package com.crm.domain.valueobjects;

public enum LoaiHoatDong {
    Call,
    Meeting,
    Email,
    Zalo;

    public static LoaiHoatDong from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("LoaiHoatDong khong duoc de trong");
        }
        try {
            return LoaiHoatDong.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("LoaiHoatDong khong hop le: " + value);
        }
    }
}