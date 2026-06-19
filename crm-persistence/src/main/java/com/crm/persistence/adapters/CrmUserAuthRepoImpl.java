package com.crm.persistence.adapters;

import com.crm.domain.entities.CrmUser;
import com.crm.domain.repositories.CrmUserAuthRepository;
import com.crm.persistence.jpa.CrmUserAuthJpaEntity;
import com.crm.persistence.repositories.CrmUserAuthJpaRepo;
import com.crm.persistence.repositories.HtUserJPARepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CrmUserAuthRepoImpl implements CrmUserAuthRepository {
    private final CrmUserAuthJpaRepo authJpaRepo;
    private final HtUserJPARepo htUserJPARepo;

    public CrmUserAuthRepoImpl(CrmUserAuthJpaRepo authJpaRepo, HtUserJPARepo htUserJPARepo) {
        this.authJpaRepo = authJpaRepo;
        this.htUserJPARepo = htUserJPARepo;
    }

    @Override
    public Optional<CrmUser> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }

        return authJpaRepo.findByUsername(username.trim())
                .flatMap(auth -> findById(auth.getUserId())
                        .map(user -> {
                            user.setPasswordHash(auth.getPasswordHash());
                            return user;
                        }));
    }

    @Override
    public Optional<CrmUser> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return htUserJPARepo.findUserWithAuthById(id).map(this::toDomain);
    }

    @Override
    public List<CrmUser> findUsers(String roleCode, String chucVu, String phongBan) {
        return htUserJPARepo.findUsersWithAuth(roleCode, chucVu, phongBan)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public CrmUser saveProfile(CrmUser user) {
        htUserJPARepo.findUserWithAuthById(user.getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay user"));

        CrmUserAuthJpaEntity entity = authJpaRepo.findById(user.getId())
                .orElseGet(CrmUserAuthJpaEntity::new);
        entity.setUserId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setRoleCode(user.getRoleCode());
        entity.setChucVu(user.getChucVu());
        entity.setPhongBan(user.getPhongBan());
        entity.setActive(Boolean.TRUE.equals(user.getActive()));
        if (entity.getPasswordHash() == null) {
            entity.setPasswordHash(user.getPasswordHash());
        }
        authJpaRepo.save(entity);
        return findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay user"));
    }

    private CrmUser toDomain(HtUserJPARepo.UserAuthProjection row) {
        return new CrmUser(
                row.getId(),
                row.getHoTen(),
                row.getUsername(),
                row.getPasswordHash(),
                row.getRoleCode(),
                row.getChucVu(),
                row.getPhongBan(),
                row.getActive() != null && row.getActive() == 1
        );
    }
}
