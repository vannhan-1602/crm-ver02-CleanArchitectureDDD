package com.crm.persistence.repositories;

import com.crm.persistence.jpa.CrmUserPermissionId;
import com.crm.persistence.jpa.CrmUserPermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrmUserPermissionJpaRepo extends JpaRepository<CrmUserPermissionJpaEntity, CrmUserPermissionId> {
    List<CrmUserPermissionJpaEntity> findByUserId(Integer userId);
    void deleteByUserId(Integer userId);
}
