package com.crm.persistence.repositories;

import com.crm.persistence.jpa.HtUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HtUserJPARepo extends JpaRepository<HtUserJpaEntity,Integer> {
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
    interface NhanVienProjection {
        Integer getId();
        String  getHoTen();
    }
}
