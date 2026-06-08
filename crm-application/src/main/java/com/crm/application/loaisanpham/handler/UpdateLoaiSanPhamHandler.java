package com.crm.application.loaisanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaisanpham.command.UpdateLoaiSanPhamCommand;
import com.crm.domain.entities.LoaiSanPham;
import com.crm.domain.repositories.LoaiSanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class UpdateLoaiSanPhamHandler
        implements IRequestHandler<UpdateLoaiSanPhamCommand, LoaiSanPham> {
    private final LoaiSanPhamRepo loaiSanPhamRepo;

    public UpdateLoaiSanPhamHandler(LoaiSanPhamRepo loaiSanPhamRepo) {
        this.loaiSanPhamRepo = loaiSanPhamRepo;
    }

    @Override
    public LoaiSanPham handle(UpdateLoaiSanPhamCommand command) {
        LoaiSanPham lsp = loaiSanPhamRepo.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
        lsp.setTenLoai(command.getTenLoai());
        lsp.setMoTa(command.getMoTa());
        return loaiSanPhamRepo.save(lsp);
    }
}
