package com.crm.persistence.jpa;

import jakarta.persistence.*;

@Entity

@Table(name = "BH_SanPham_HinhAnh")
public class SanPhamHinhAnhJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Integer Id;
    @Column(name = "UrlHinhAnh")
    private String Url;
    @Column(name = "IsMain")
    private  int isMain;

    public SanPhamJpaEntity getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPhamJpaEntity sanPham) {
        this.sanPham = sanPham;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SanPham_Id")
    private SanPhamJpaEntity sanPham;
}
