package com.crm.persistence.adapters;

import com.crm.domain.entities.UserPermission;
import com.crm.domain.repositories.UserPermissionRepository;
import com.crm.persistence.jpa.CrmUserPermissionJpaEntity;
import com.crm.persistence.repositories.CrmUserPermissionJpaRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserPermissionRepoImpl implements UserPermissionRepository {
    private final CrmUserPermissionJpaRepo repo;

    public UserPermissionRepoImpl(CrmUserPermissionJpaRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<UserPermission> findByUserId(Integer userId) {
        if (userId == null) {
            return List.of();
        }
        return repo.findByUserId(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void replacePermissions(Integer userId, List<UserPermission> permissions) {
        repo.deleteByUserId(userId);
        List<CrmUserPermissionJpaEntity> entities = permissions.stream()
                .map(permission -> toEntity(userId, permission))
                .toList();
        repo.saveAll(entities);
    }

    private UserPermission toDomain(CrmUserPermissionJpaEntity entity) {
        return new UserPermission(
                entity.getModuleCode(),
                entity.getCanView(),
                entity.getCanCreate(),
                entity.getCanUpdate(),
                entity.getCanDelete()
        );
    }

    private CrmUserPermissionJpaEntity toEntity(Integer userId, UserPermission permission) {
        CrmUserPermissionJpaEntity entity = new CrmUserPermissionJpaEntity();
        entity.setUserId(userId);
        entity.setModuleCode(permission.getModuleCode());
        entity.setCanView(Boolean.TRUE.equals(permission.getCanView()));
        entity.setCanCreate(Boolean.TRUE.equals(permission.getCanCreate()));
        entity.setCanUpdate(Boolean.TRUE.equals(permission.getCanUpdate()));
        entity.setCanDelete(Boolean.TRUE.equals(permission.getCanDelete()));
        return entity;
    }
}
