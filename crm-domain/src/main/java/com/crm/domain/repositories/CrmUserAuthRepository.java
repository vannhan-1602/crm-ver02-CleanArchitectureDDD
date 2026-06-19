package com.crm.domain.repositories;

import com.crm.domain.entities.CrmUser;

import java.util.List;
import java.util.Optional;

public interface CrmUserAuthRepository {
    Optional<CrmUser> findByUsername(String username);
    Optional<CrmUser> findById(Integer id);
    List<CrmUser> findUsers(String roleCode, String chucVu, String phongBan);
    CrmUser saveProfile(CrmUser user);
}
