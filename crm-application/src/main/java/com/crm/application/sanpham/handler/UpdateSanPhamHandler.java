package com.crm.application.sanpham.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanpham.command.UpdateSanPhamCommand;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

@Service
public class UpdateSanPhamHandler
        implements IRequestHandler<UpdateSanPhamCommand, SanPham> {

    private final SanPhamRepo sanPhamRepo;

    public UpdateSanPhamHandler(SanPhamRepo sanPhamRepo) {
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public SanPham handle(UpdateSanPhamCommand request) {
        SanPham sp = sanPhamRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
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