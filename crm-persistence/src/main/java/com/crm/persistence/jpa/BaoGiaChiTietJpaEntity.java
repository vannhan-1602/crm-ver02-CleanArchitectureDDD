package com.crm.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "HD_BaoGia_ChiTiet")
public class BaoGiaChiTietJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BaoGia_Id", nullable = false)
    private BaoGiaJpaEntity baoGia;

    @Column(name = "SanPham_Id", nullable = false)
    private Integer sanPhamId;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "DonGia", nullable = false)
    private Double donGia;

    protected BaoGiaChiTietJpaEntity() {
    }

    public BaoGiaChiTietJpaEntity(Long id, Integer sanPhamId, Integer soLuong, Double donGia) {
        this.id = id;
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Long getId() {
        return id;
    }

    public BaoGiaJpaEntity getBaoGia() {
        return baoGia;
    }

    public void setBaoGia(BaoGiaJpaEntity baoGia) {
        this.baoGia = baoGia;
    }

    public Integer getSanPhamId() {
        return sanPhamId;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public Double getDonGia() {
        return donGia;
    }
}
