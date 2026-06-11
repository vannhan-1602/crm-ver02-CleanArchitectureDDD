package com.crm.persistence.repositories;

import com.crm.persistence.jpa.HoatDongJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HoatDongJPARepo extends JpaRepository<HoatDongJpaEntity, Long> {

    List<HoatDongJpaEntity> findByKhachHangId(Long khachHangId);

    List<HoatDongJpaEntity> findByLeadId(Long leadId);
}