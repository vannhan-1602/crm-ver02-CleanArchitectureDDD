package com.crm.application.sanphamhinhanh.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanphamhinhanh.command.DeleteSanPhamHinhAnhCommand;
import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteSanPhamHinhAnhHandler implements IRequestHandler<DeleteSanPhamHinhAnhCommand, Boolean> {
    private final SanPhamHinhAnhRepo sanPhamHinhAnhRepo;

    public DeleteSanPhamHinhAnhHandler(SanPhamHinhAnhRepo sanPhamHinhAnhRepo) {
        this.sanPhamHinhAnhRepo = sanPhamHinhAnhRepo;
    }

    @Override
    public Boolean handle(DeleteSanPhamHinhAnhCommand command) {
        SanPhamHinhAnh hinhAnh = sanPhamHinhAnhRepo.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay hinh anh san pham"));
        sanPhamHinhAnhRepo.delete(hinhAnh);
        return true;
    }
}
