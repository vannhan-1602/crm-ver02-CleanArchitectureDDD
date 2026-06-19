package com.crm.application.khachhang.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.khachhang.command.CreateKhachHangCommand;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.KhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CreateKhachHangHandler implements IRequestHandler<CreateKhachHangCommand, KhachHang> {

    private final KhachHangRepo khachHangRepo;

    public CreateKhachHangHandler(KhachHangRepo khachHangRepo) {
        this.khachHangRepo = khachHangRepo;
    }

    @Override
    public KhachHang handle(CreateKhachHangCommand command) {
        String maMoi = generateMaKhachHang();

        KhachHang khachHang = new KhachHang(
                maMoi,
                command.getTenKhachHang(),
                command.getEmail(),
                command.getSoDienThoai(),
                command.getLoaiKhachHangId(),
                command.getTinhTrangId(),
                command.getMaSoThue(),
                command.getNhanVienPhuTrachId()
        );

        return khachHangRepo.save(khachHang);
    }

    private String generateMaKhachHang() {
        Optional<String> maxMa = khachHangRepo.findMaxMaKhachHang();

        if (maxMa.isEmpty()) {
            return "KH0001";
        }

        String current = maxMa.get();
        try {
            int soHienTai = Integer.parseInt(current.replaceAll("[^0-9]", ""));
            return String.format("KH%04d", soHienTai + 1);
        } catch (NumberFormatException e) {
            return "KH" + System.currentTimeMillis();
        }
    }
}