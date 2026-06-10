package com.crm.application.sanphamhinhanh.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.SanPhamHinhAnh;

public class UpdateSanPhamHinhAnhCommand implements IRequest<SanPhamHinhAnh> {
    private Integer id;
    private Integer sanPham_Id;
    private String url;
    private int isMain;

    public UpdateSanPhamHinhAnhCommand() {
    }

    public UpdateSanPhamHinhAnhCommand(Integer id, Integer sanPham_Id, String url, int isMain) {
        this.id = id;
        this.sanPham_Id = sanPham_Id;
        this.url = url;
        this.isMain = isMain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSanPham_Id() {
        return sanPham_Id;
    }

    public String getUrl() {
        return url;
    }

    public int getIsMain() {
        return isMain;
    }
}
