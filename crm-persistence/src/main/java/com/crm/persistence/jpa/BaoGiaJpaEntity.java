package com.crm.persistence.jpa;

import com.crm.domain.valueobjects.TrangThaiBaoGia;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HD_BaoGia")
public class BaoGiaJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MaBaoGia", nullable = false, length = 50)
    private String maBaoGia;

    @Column(name = "KhachHang_Id", nullable = false)
    private Long khachHangId;

    @Column(name = "TongTien")
    private Double tongTien;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    private TrangThaiBaoGia trangThai;

    @Column(name = "NhanVien_Id")
    private Integer nhanVienId;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "baoGia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BaoGiaChiTietJpaEntity> chiTiets = new ArrayList<>();

    protected BaoGiaJpaEntity() {
    }

    public BaoGiaJpaEntity(Long id,
                           String maBaoGia,
                           Long khachHangId,
                           Double tongTien,
                           TrangThaiBaoGia trangThai,
                           Integer nhanVienId,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt,
                           List<BaoGiaChiTietJpaEntity> chiTiets) {
        this.id = id;
        this.maBaoGia = maBaoGia;
        this.khachHangId = khachHangId;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.nhanVienId = nhanVienId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        setChiTiets(chiTiets);
    }

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getMaBaoGia() {
        return maBaoGia;
    }

    public Long getKhachHangId() {
        return khachHangId;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public TrangThaiBaoGia getTrangThai() {
        return trangThai;
    }

    public Integer getNhanVienId() {
        return nhanVienId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<BaoGiaChiTietJpaEntity> getChiTiets() {
        return chiTiets;
    }

    public void setChiTiets(List<BaoGiaChiTietJpaEntity> chiTiets) {
        this.chiTiets.clear();
        if (chiTiets == null) {
            return;
        }
        this.chiTiets.addAll(chiTiets);
        for (BaoGiaChiTietJpaEntity chiTiet : this.chiTiets) {
            chiTiet.setBaoGia(this);
        }
    }
}
