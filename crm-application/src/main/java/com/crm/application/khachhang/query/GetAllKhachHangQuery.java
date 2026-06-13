package com.crm.application.khachhang.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.KhachHang;

import java.util.List;

public class GetAllKhachHangQuery implements IRequest<List<KhachHang>> {

    private Integer loaiKhachHangId; 

    public GetAllKhachHangQuery() {}

    public GetAllKhachHangQuery(Integer loaiKhachHangId) {
        this.loaiKhachHangId = loaiKhachHangId;
    }

    public Integer getLoaiKhachHangId() { return loaiKhachHangId; }
}