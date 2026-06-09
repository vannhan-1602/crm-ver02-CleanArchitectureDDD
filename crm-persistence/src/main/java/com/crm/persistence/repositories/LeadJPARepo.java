package com.crm.persistence.repositories;

import com.crm.persistence.jpa.LeadJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface LeadJPARepo extends JpaRepository<LeadJpaEntity, Long> {


    List<LeadJpaEntity> findByIsDeletedFalse();


    Optional<LeadJpaEntity> findByIdAndIsDeletedFalse(Long id);

   
    List<LeadJpaEntity> findByNhanVienPhuTrachIdAndIsDeletedFalse(Integer nhanVienId);


    @Modifying
    @Transactional
    @Query("UPDATE LeadJpaEntity l SET l.isDeleted = true WHERE l.id = :id")
    void softDeleteById(@Param("id") Long id);
}