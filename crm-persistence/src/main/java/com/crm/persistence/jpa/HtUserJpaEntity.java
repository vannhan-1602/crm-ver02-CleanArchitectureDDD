package com.crm.persistence.jpa;

import jakarta.persistence.*;


@Entity
@Table(name = "HT_User")
public class HtUserJpaEntity {

    @Id
    @Column(name = "Id")
    private Integer id;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NhanSu_Id", insertable = false, updatable = false)
    private ThongTinNhanSuJpaEntity thongTinNhanSu;

    @Column(name = "NhanSu_Id")
    private Integer nhanSuId;

    public HtUserJpaEntity() {}

    public Integer getId()                              { return id; }
    public Integer getNhanSuId()                        { return nhanSuId; }
    public ThongTinNhanSuJpaEntity getThongTinNhanSu()  { return thongTinNhanSu; }
}