package com.crm.persistence.jpa;

import jakarta.persistence.*;


@Entity
@Table(name = "HT_ThongTinNhanSu")
public class ThongTinNhanSuJpaEntity {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "HoTen", length = 100)
    private String hoTen;

    public ThongTinNhanSuJpaEntity() {}

    public Integer getId()   { return id; }
    public String getHoTen() { return hoTen; }
}