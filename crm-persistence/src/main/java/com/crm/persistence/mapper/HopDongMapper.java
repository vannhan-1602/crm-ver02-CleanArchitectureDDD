package com.crm.persistence.mapper;

import com.crm.domain.entities.HopDong;
import com.crm.domain.valueobjects.MaHopDong;
import com.crm.persistence.jpa.HopDongJpaEntity;

public final class HopDongMapper {
    private HopDongMapper() {
    }

    public static HopDong toDomain(HopDongJpaEntity entity) {
        return new HopDong(
                entity.getId(),
                new MaHopDong(entity.getMaHopDong()),
                entity.getKhachHangId(),
                entity.getNgayKy(),
                entity.getThoiHan(),
                entity.getTrangThai(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static HopDongJpaEntity toJpa(HopDong hopDong) {
        return new HopDongJpaEntity(
                hopDong.getId(),
                hopDong.getMaHopDong().getValue(),
                hopDong.getKhachHangId(),
                hopDong.getNgayKy(),
                hopDong.getThoiHan(),
                hopDong.getTrangThai(),
                hopDong.getCreatedAt(),
                hopDong.getUpdatedAt()
        );
    }
}
