package com.crm.persistence.repositories;

import com.crm.persistence.jpa.CoHoiBanHangJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoHoiBanHangJpaRepo extends JpaRepository<CoHoiBanHangJpaEntity,Integer> {
    List<CoHoiBanHangJpaEntity> findByIsDeleted(int isDeleted);
    Optional<CoHoiBanHangJpaEntity> findByIdAndIsDeleted(Integer id, int isDeleted);

}
