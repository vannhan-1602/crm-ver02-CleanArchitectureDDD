package com.crm.application.loaisanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaisanpham.command.DeleteLoaiSanPhamCommand;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteLoaiSanPhamHandler implements IRequestHandler<DeleteLoaiSanPhamCommand, Boolean> {
    private final LoaiSanPhamRepo loaiSanPhamRepo;

    public DeleteLoaiSanPhamHandler(LoaiSanPhamRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    public Boolean handle(DeleteLoaiSanPhamCommand command) {
        LoaiSanPham lsp = loaiSanPhamRepo.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
        loaiSanPhamRepo.delete(lsp);
        return true;
    }
}
