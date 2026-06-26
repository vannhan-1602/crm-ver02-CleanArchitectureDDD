package com.crm.application.sanphamhinhanh.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.sanphamhinhanh.command.SetMainSanPhamHinhAnhCommand;
import com.crm.domain.entities.SanPhamHinhAnh;
import com.crm.domain.repositories.SanPhamHinhAnhRepo;
import com.crm.domain.repositories.SanPhamRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetMainSanPhamHinhAnhHandler implements IRequestHandler<SetMainSanPhamHinhAnhCommand, SanPhamHinhAnh> {
    private final SanPhamHinhAnhRepo sanPhamHinhAnhRepo;
    private final SanPhamRepo sanPhamRepo;

    public SetMainSanPhamHinhAnhHandler(SanPhamHinhAnhRepo sanPhamHinhAnhRepo, SanPhamRepo sanPhamRepo) {
        this.sanPhamHinhAnhRepo = sanPhamHinhAnhRepo;
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public SanPhamHinhAnh handle(SetMainSanPhamHinhAnhCommand command) {
        sanPhamRepo.findById(command.getSanPhamId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay san pham"));

        List<SanPhamHinhAnh> images = sanPhamHinhAnhRepo.findBySanPhamId(command.getSanPhamId());
        SanPhamHinhAnh selected = images.stream()
                .filter(image -> command.getHinhAnhId().equals(image.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Khong tim thay hinh anh san pham"));

        images.forEach(image -> image.setIsMain(command.getHinhAnhId().equals(image.getId()) ? 1 : 0));
        sanPhamHinhAnhRepo.saveAll(images);
        selected.setIsMain(1);
        return selected;
    }
}
