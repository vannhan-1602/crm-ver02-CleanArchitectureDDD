package com.crm.application.sanpham.handler;

import com.crm.application.sanpham.command.DeleteSanPhamCommand;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteSanPhamHandler {
    private final SanPhamRepo sanPhamRepo;

    public DeleteSanPhamHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    public boolean handle(DeleteSanPhamCommand command) {
        SanPham sp = sanPhamRepo.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        sanPhamRepo.delete(sp);
        return true;
    }
}