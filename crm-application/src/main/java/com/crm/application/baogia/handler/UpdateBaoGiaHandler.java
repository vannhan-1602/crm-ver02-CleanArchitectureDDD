package com.crm.application.baogia.handler;

import com.crm.application.baogia.command.BaoGiaChiTietCommand;
import com.crm.application.baogia.command.UpdateBaoGiaCommand;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.BaoGia;
import com.crm.domain.entities.BaoGiaChiTiet;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.entities.SanPham;
import com.crm.domain.repositories.BaoGiaRepo;
import com.crm.domain.repositories.KhachHangRepo;
import com.crm.domain.repositories.SanPhamRepo;
import com.crm.domain.valueobjects.TrangThaiBaoGia;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateBaoGiaHandler implements IRequestHandler<UpdateBaoGiaCommand, BaoGia> {
    private final BaoGiaRepo baoGiaRepo;
    private final KhachHangRepo khachHangRepo;
    private final SanPhamRepo sanPhamRepo;

    public UpdateBaoGiaHandler(BaoGiaRepo baoGiaRepo,
                               KhachHangRepo khachHangRepo,
                               SanPhamRepo sanPhamRepo) {
        this.baoGiaRepo = baoGiaRepo;
        this.khachHangRepo = khachHangRepo;
        this.sanPhamRepo = sanPhamRepo;
    }

    @Override
    public BaoGia handle(UpdateBaoGiaCommand request) {
        BaoGia existing = baoGiaRepo.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay bao gia: " + request.getId()));

        Long khachHangId = request.getKhachHangId() != null ? request.getKhachHangId() : existing.getKhachHangId();
        validateKhachHang(khachHangId);

        existing.capNhatThongTin(
                request.getMaBaoGia(),
                request.getKhachHangId(),
                request.getNhanVienId(),
                parseTrangThai(request.getTrangThai())
        );

        if (request.getChiTiets() != null && !request.getChiTiets().isEmpty()) {
            existing.setChiTiets(buildChiTiets(request.getChiTiets()));
        }
        return baoGiaRepo.save(existing);
    }

    private void validateKhachHang(Long khachHangId) {
        KhachHang khachHang = khachHangRepo.findById(khachHangId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay khach hang: " + khachHangId));
        if (khachHang.isDeleted()) {
            throw new IllegalArgumentException("Khach hang da bi xoa");
        }
    }

    private List<BaoGiaChiTiet> buildChiTiets(List<BaoGiaChiTietCommand> commands) {
        List<BaoGiaChiTiet> chiTiets = new ArrayList<>();
        for (BaoGiaChiTietCommand command : commands) {
            if (command == null || command.getSanPhamId() == null) {
                throw new IllegalArgumentException("San pham ID khong duoc rong");
            }
            if (command.getSoLuong() == null || command.getSoLuong() <= 0) {
                throw new IllegalArgumentException("So luong phai lon hon 0");
            }
            SanPham sanPham = sanPhamRepo.findById(command.getSanPhamId())
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay san pham: " + command.getSanPhamId()));
            double donGia = command.getDonGia() != null && command.getDonGia() > 0
                    ? command.getDonGia()
                    : (sanPham.getGiaBan() != null ? sanPham.getGiaBan() : 0D);
            chiTiets.add(new BaoGiaChiTiet(command.getSanPhamId(), command.getSoLuong(), donGia));
        }
        return chiTiets;
    }

    private TrangThaiBaoGia parseTrangThai(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return TrangThaiBaoGia.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return TrangThaiBaoGia.Nhap;
        }
    }
}
