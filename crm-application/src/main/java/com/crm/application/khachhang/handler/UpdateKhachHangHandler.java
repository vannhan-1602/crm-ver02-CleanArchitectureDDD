package com.crm.application.khachhang.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.khachhang.command.UpdateKhachHangCommand;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.KhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class UpdateKhachHangHandler implements IRequestHandler<UpdateKhachHangCommand, KhachHang> {

    private final KhachHangRepo khachHangRepo;

    public UpdateKhachHangHandler(KhachHangRepo khachHangRepo) {
        this.khachHangRepo = khachHangRepo;
    }

    @Override
    public KhachHang handle(UpdateKhachHangCommand command) {
        KhachHang khachHang = khachHangRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "KhachHang khong ton tai: " + command.getId()));

        khachHang.capNhatThongTin(
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
}