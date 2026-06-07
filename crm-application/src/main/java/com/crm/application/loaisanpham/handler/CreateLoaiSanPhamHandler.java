package com.crm.application.loaisanpham.handler;

import com.crm.application.loaisanpham.command.CreateLoaiSanPhamCommand;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class CreateLoaiSanPhamHandler {
    private final LoaiSanPhamRepo loaiSanPhamRepo;

    public CreateLoaiSanPhamHandler(LoaiSanPhamRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    public LoaiSanPham handle(CreateLoaiSanPhamCommand command) {
        LoaiSanPham lsp = new LoaiSanPham();
        lsp.setTenLoai(command.getTenLoai());
        lsp.setMoTa(command.getMoTa());
        return loaiSanPhamRepo.save(lsp);
    }
}