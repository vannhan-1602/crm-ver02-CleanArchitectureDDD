package com.crm.application.sanpham.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.SanPham;

public class AddHinhAnhToSanPhamCommand implements IRequest<SanPham> {
    private Integer sanPhamId;
    private String url;
    private int isMain;

    public AddHinhAnhToSanPhamCommand() {
    }

    public AddHinhAnhToSanPhamCommand(Integer sanPhamId, String url, int isMain) {
        this.sanPhamId = sanPhamId;
        this.url = url;
        this.isMain = isMain;
    }

    public Integer getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(Integer sanPhamId) {
        this.sanPhamId = sanPhamId;
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
