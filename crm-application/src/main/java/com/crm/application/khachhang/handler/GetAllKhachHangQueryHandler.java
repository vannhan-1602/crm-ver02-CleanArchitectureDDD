package com.crm.application.khachhang.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.khachhang.query.GetAllKhachHangQuery;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.KhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GetAllKhachHangQueryHandler implements IRequestHandler<GetAllKhachHangQuery, List<KhachHang>> {

    private final KhachHangRepo khachHangRepo;

    public GetAllKhachHangQueryHandler(KhachHangRepo khachHangRepo) {
        this.khachHangRepo = khachHangRepo;
    }

    @Override
    public List<KhachHang> handle(GetAllKhachHangQuery query) {
        if (query.getLoaiKhachHangId() != null) {
            return khachHangRepo.findByLoaiKhachHangId(query.getLoaiKhachHangId());
        }
        return khachHangRepo.findAll();
    }
}