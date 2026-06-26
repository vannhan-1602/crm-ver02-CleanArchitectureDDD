package com.crm.persistence.repositories;

import com.crm.persistence.jpa.DiaChiJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaChiJPARepo extends JpaRepository<DiaChiJpaEntity, Long> {
    List<DiaChiJpaEntity> findByKhachHangId(Long khachHangId);
}
