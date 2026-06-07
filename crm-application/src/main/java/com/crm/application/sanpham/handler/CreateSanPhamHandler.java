package com.crm.application.sanpham.handler;

import com.crm.application.sanpham.command.CreateSanPhamCommand;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class CreateSanPhamHandler {
    private final SanPhamRepo sanPhamRepo;

    public CreateSanPhamHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    public SanPham handle(CreateSanPhamCommand command) {
        SanPham sp = new SanPham();
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