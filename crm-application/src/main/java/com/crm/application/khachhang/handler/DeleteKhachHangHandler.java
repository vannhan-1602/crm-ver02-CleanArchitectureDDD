package com.crm.application.khachhang.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.khachhang.command.DeleteKhachHangCommand;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.KhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class DeleteKhachHangHandler implements IRequestHandler<DeleteKhachHangCommand, Boolean> {

    private final KhachHangRepo khachHangRepo;

    public DeleteKhachHangHandler(KhachHangRepo khachHangRepo) {
        this.khachHangRepo = khachHangRepo;
    }

    @Override
    public Boolean handle(DeleteKhachHangCommand command) {
        KhachHang khachHang = khachHangRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "KhachHang khong ton tai: " + command.getId()));

        khachHang.xoa();
        khachHangRepo.softDeleteById(command.getId());
        return true;
    }
}