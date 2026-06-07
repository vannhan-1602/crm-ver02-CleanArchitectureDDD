package com.crm.application.sanpham.handler;

import com.crm.application.sanpham.command.UpdateSanPhamCommand;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class UpdateSanPhamHandler {
    private final SanPhamRepo sanPhamRepo;

    public UpdateSanPhamHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    public SanPham handle(UpdateSanPhamCommand command) {
        SanPham sp = sanPhamRepo.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        sp.setLoaiSanPham(command.getLoaiSanPham());
        sp.setMaSanPham(command.getMaSanPham());
        sp.setTenSanPham(command.getTenSanPham());
        sp.setDonVi(command.getDonVi());
        sp.setGiaBan(command.getGiaBan());
        sp.setSlTon(command.getSlTon());
        sp.setTrangThai(command.getTrangThai());
        return sanPhamRepo.save(sp);
    }
}