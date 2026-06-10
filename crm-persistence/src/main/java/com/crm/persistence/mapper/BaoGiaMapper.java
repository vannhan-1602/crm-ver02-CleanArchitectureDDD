package com.crm.persistence.mapper;

import com.crm.domain.entities.BaoGia;
import com.crm.domain.entities.BaoGiaChiTiet;
import com.crm.domain.valueobjects.TrangThaiBaoGia;
import com.crm.persistence.jpa.BaoGiaChiTietJpaEntity;
import com.crm.persistence.jpa.BaoGiaJpaEntity;

import java.util.ArrayList;
import java.util.List;

public final class BaoGiaMapper {
    private BaoGiaMapper() {
    }

    public static BaoGia toDomain(BaoGiaJpaEntity entity) {
        List<BaoGiaChiTiet> chiTiets = new ArrayList<>();
        if (entity.getChiTiets() != null) {
            for (BaoGiaChiTietJpaEntity chiTietJpa : entity.getChiTiets()) {
                chiTiets.add(new BaoGiaChiTiet(
                        chiTietJpa.getId(),
                        chiTietJpa.getSanPhamId(),
                        chiTietJpa.getSoLuong(),
                        chiTietJpa.getDonGia()
                ));
            }
        }
        return new BaoGia(
                entity.getId(),
                entity.getMaBaoGia(),
                entity.getKhachHangId(),
                entity.getNhanVienId(),
                entity.getTongTien(),
                entity.getTrangThai() != null ? entity.getTrangThai() : TrangThaiBaoGia.Nhap,
                chiTiets,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static BaoGiaJpaEntity toJpa(BaoGia domain) {
        List<BaoGiaChiTietJpaEntity> chiTiets = new ArrayList<>();
        if (domain.getChiTiets() != null) {
            for (BaoGiaChiTiet item : domain.getChiTiets()) {
                chiTiets.add(new BaoGiaChiTietJpaEntity(
                        item.getId(),
                        item.getSanPhamId(),
                        item.getSoLuong(),
                        item.getDonGia()
                ));
            }
        }
        BaoGiaJpaEntity entity = new BaoGiaJpaEntity(
                domain.getId(),
                domain.getMaBaoGia(),
                domain.getKhachHangId(),
                domain.getTongTien(),
                domain.getTrangThai(),
                domain.getNhanVienId(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                chiTiets
        );
        entity.setChiTiets(chiTiets);
        return entity;
    }
}
