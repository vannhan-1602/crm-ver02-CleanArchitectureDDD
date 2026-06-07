package com.crm.application.sanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanpham.command.DeleteSanPhamCommand;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteSanPhamHandler
        implements IRequestHandler<DeleteSanPhamCommand, Boolean> {

    private final SanPhamRepo sanPhamRepo;

    public DeleteSanPhamHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public Boolean handle(DeleteSanPhamCommand request) {
        SanPham sp = sanPhamRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        sanPhamRepo.delete(sp);
        return true;
    }
}