package com.crm.application.khachhang.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.khachhang.query.GetKhachHangByIdQuery;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.KhachHangRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class GetKhachHangByIdQueryHandler implements IRequestHandler<GetKhachHangByIdQuery, KhachHang> {

    private final KhachHangRepo khachHangRepo;

    public GetKhachHangByIdQueryHandler(KhachHangRepo khachHangRepo) {
        this.khachHangRepo = khachHangRepo;
    }

    @Override
    public KhachHang handle(GetKhachHangByIdQuery query) {
        return khachHangRepo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "KhachHang khong ton tai: " + query.getId()));
    }
}