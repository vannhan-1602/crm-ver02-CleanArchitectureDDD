package com.crm.application.hoatdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoatdong.query.GetHoatDongByIdQuery;
import com.crm.domain.entities.HoatDong;
import com.crm.domain.repositories.HoatDongRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class GetHoatDongByIdQueryHandler implements IRequestHandler<GetHoatDongByIdQuery, HoatDong> {

    private final HoatDongRepo hoatDongRepo;

    public GetHoatDongByIdQueryHandler(HoatDongRepo hoatDongRepo) {
        this.hoatDongRepo = hoatDongRepo;
    }

    @Override
    public HoatDong handle(GetHoatDongByIdQuery query) {
        return hoatDongRepo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "HoatDong khong ton tai: " + query.getId()));
    }
}