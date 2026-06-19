package com.crm.persistence.repositories;

import com.crm.persistence.jpa.CrmUserAuthJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrmUserAuthJpaRepo extends JpaRepository<CrmUserAuthJpaEntity, Integer> {
    Optional<CrmUserAuthJpaEntity> findByUsername(String username);
}
