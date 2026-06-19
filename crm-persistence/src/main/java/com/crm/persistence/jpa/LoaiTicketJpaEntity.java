package com.crm.persistence.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "TK_LoaiTicket")
public class LoaiTicketJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Short id;

    @Column(name = "TenLoai", nullable = false, length = 100, unique = true)
    private String tenLoai;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "IsActive")
    private Boolean isActive;

    public Short getId() { return id; }
    public void setId(Short id) { this.id = id; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}
