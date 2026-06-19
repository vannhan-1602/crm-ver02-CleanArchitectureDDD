package com.crm.application.taichinh.dto;

import java.math.BigDecimal;

public record TaiChinhThongKe(
        BigDecimal tongHoaDon,
        BigDecimal tongDaThu,
        BigDecimal tongConNo,
        BigDecimal tongPhieuThu,
        BigDecimal tongPhieuChi,
        long soHoaDon,
        long soPhieuThu,
        long soPhieuChi,
        long soGiaoDich
) {
}
