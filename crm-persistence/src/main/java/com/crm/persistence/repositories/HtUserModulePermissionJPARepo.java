package com.crm.persistence.repositories;

import com.crm.persistence.jpa.HtUserModulePermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HtUserModulePermissionJPARepo extends JpaRepository<HtUserModulePermissionJpaEntity, Long> {
    List<HtUserModulePermissionJpaEntity> findByUserId(Integer userId);

    Optional<HtUserModulePermissionJpaEntity> findByUserIdAndModuleKey(Integer userId, String moduleKey);

    void deleteByUserId(Integer userId);
}
