package com.crm.application.loaisanpham.handler;

import com.crm.application.loaisanpham.command.DeleteLoaiSanPhamCommand;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteLoaiSanPhamHandler {
    private final LoaiSanPhamRepo loaiSanPhamRepo;

    public DeleteLoaiSanPhamHandler(LoaiSanPhamRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    public boolean handle(DeleteLoaiSanPhamCommand command) {
        LoaiSanPham lsp = loaiSanPhamRepo.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
        loaiSanPhamRepo.Delete(lsp);
        return true;
    }
}