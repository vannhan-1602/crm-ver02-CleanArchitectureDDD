package com.crm.persistence.repositories;

import com.crm.persistence.jpa.BaoGiaJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaoGiaJPARepo extends JpaRepository<BaoGiaJpaEntity, Long> {
    @Override
    @EntityGraph(attributePaths = "chiTiets")
    List<BaoGiaJpaEntity> findAll();

    @Override
    @EntityGraph(attributePaths = "chiTiets")
    Optional<BaoGiaJpaEntity> findById(Long id);
}
