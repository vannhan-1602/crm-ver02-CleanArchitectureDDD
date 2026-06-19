package com.crm.persistence.repositories;

import com.crm.persistence.entities.HoaDonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonEntityRepository extends JpaRepository<HoaDonJpaEntity, Long> {
    @Query("""
            SELECT h FROM HoaDonJpaEntity h
            WHERE (:from IS NULL OR h.createdAt >= :from)
              AND (:to IS NULL OR h.createdAt <= :to)
            """)
    List<HoaDonJpaEntity> findByCreatedAtRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
