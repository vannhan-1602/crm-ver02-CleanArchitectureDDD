package com.crm.persistence.repositories;

import com.crm.persistence.jpa.HtUserModulePermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HtUserModulePermissionJPARepo extends JpaRepository<HtUserModulePermissionJpaEntity, Long> {
    List<HtUserModulePermissionJpaEntity> findByUserId(Integer userId);

    Optional<HtUserModulePermissionJpaEntity> findByUserIdAndModuleKey(Integer userId, String moduleKey);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from HtUserModulePermissionJpaEntity p where p.userId = :userId")
    int deleteByUserId(@Param("userId") Integer userId);
}
