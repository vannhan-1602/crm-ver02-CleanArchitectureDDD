package com.crm.domain.entities;

public class SanPhamHinhAnh {
    private Integer id;
    private Integer SanPham_Id;
    private String Url;
    private  int isMain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSanPham_Id() {
        return SanPham_Id;
    }

    public void setSanPham_Id(Integer sanPham_Id) {
        SanPham_Id = sanPham_Id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }
}
