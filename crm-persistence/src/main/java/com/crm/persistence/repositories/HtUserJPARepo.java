package com.crm.persistence.repositories;

import com.crm.persistence.jpa.HtUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HtUserJPARepo extends JpaRepository<HtUserJpaEntity,Integer> {
    Optional<HtUserJpaEntity> findByUsername(String username);

    @Query(value = """
            SELECT ns.HoTen
            FROM HT_User u
            INNER JOIN HT_ThongTinNhanSu ns ON u.NhanSu_Id = ns.Id
            WHERE u.Id = :userId
            """, nativeQuery = true)
    Optional<String> findHoTenByUserId(@Param("userId") Integer userId);
    @Query(value = """
            SELECT u.Id   AS id,
                   ns.HoTen AS hoTen
            FROM HT_User u
            INNER JOIN HT_ThongTinNhanSu ns ON u.NhanSu_Id = ns.Id
            ORDER BY ns.HoTen
            """, nativeQuery = true)
    List<NhanVienProjection> findAllWithHoTen();

    @Query(value = """
            SELECT u.Id AS id,
                   u.Username AS username,
                   u.NhanSu_Id AS nhanSuId,
                   ns.HoTen AS hoTen,
                   u.Role_Id AS roleId,
                   r.TenRole AS roleName,
                   u.TrangThai AS trangThai
            FROM HT_User u
            LEFT JOIN HT_ThongTinNhanSu ns ON u.NhanSu_Id = ns.Id
            LEFT JOIN HT_Role r ON u.Role_Id = r.Id
            ORDER BY u.Id
            """, nativeQuery = true)
    List<UserSummaryProjection> findAllUserSummaries();

    interface NhanVienProjection {
        Integer getId();
        String  getHoTen();
    }

    interface UserSummaryProjection {
        Integer getId();
        String getUsername();
        Integer getNhanSuId();
        String getHoTen();
        Integer getRoleId();
        String getRoleName();
        String getTrangThai();
    }
}
