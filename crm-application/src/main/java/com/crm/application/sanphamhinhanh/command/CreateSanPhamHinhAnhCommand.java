package com.crm.application.sanphamhinhanh.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.SanPhamHinhAnh;

public class CreateSanPhamHinhAnhCommand implements IRequest<SanPhamHinhAnh> {
    private Integer sanPham_Id;
    private String url;
    private int isMain;

    public CreateSanPhamHinhAnhCommand() {
    }

    public CreateSanPhamHinhAnhCommand(Integer sanPham_Id, String url, int isMain) {
        this.sanPham_Id = sanPham_Id;
        this.url = url;
        this.isMain = isMain;
    }

    public Integer getSanPham_Id() {
        return sanPham_Id;
    }

    public void setSanPham_Id(Integer sanPham_Id) {
        this.sanPham_Id = sanPham_Id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }
}
