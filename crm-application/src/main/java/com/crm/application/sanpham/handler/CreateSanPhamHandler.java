package com.crm.application.sanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanpham.command.CreateSanPhamCommand;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class CreateSanPhamHandler
        implements IRequestHandler<CreateSanPhamCommand, SanPham> {

    private final SanPhamRepo sanPhamRepo;

    public CreateSanPhamHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public SanPham handle(CreateSanPhamCommand request) {
        SanPham sp = new SanPham();
        sp.setLoaiSanPham(request.getLoaiSanPham());
        sp.setMaSanPham(request.getMaSanPham());
        sp.setTenSanPham(request.getTenSanPham());
        sp.setDonVi(request.getDonVi());
        sp.setGiaBan(request.getGiaBan());
        sp.setSlTon(request.getSlTon());
        sp.setTrangThai(request.getTrangThai());
        return sanPhamRepo.save(sp);
    }
}