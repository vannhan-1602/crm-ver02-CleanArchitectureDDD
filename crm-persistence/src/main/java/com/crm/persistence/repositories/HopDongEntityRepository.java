package com.crm.persistence.repositories;

import com.crm.persistence.jpa.HopDongJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HopDongEntityRepository extends JpaRepository<HopDongJpaEntity, Long> {
}
