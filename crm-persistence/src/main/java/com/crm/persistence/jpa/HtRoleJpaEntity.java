package com.crm.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "HT_Role")
public class HtRoleJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "TenRole")
    private String tenRole;

    @Column(name = "MoTa")
    private String moTa;

    public Integer getId() {
        return id;
    }

    public String getTenRole() {
        return tenRole;
    }

    public String getMoTa() {
        return moTa;
    }
}
