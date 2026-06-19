package com.crm.application.hoatdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoatdong.query.GetAllHoatDongQuery;
import com.crm.domain.entities.HoatDong;
import com.crm.domain.repositories.HoatDongRepo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GetAllHoatDongQueryHandler implements IRequestHandler<GetAllHoatDongQuery, List<HoatDong>> {

    private final HoatDongRepo hoatDongRepo;

    public GetAllHoatDongQueryHandler(HoatDongRepo hoatDongRepo) {
        this.hoatDongRepo = hoatDongRepo;
    }

    @Override
    public List<HoatDong> handle(GetAllHoatDongQuery query) {
        if (query.getKhachHangId() != null) {
            return hoatDongRepo.findByKhachHangId(query.getKhachHangId());
        }
        if (query.getLeadId() != null) {
            return hoatDongRepo.findByLeadId(query.getLeadId());
        }
        return hoatDongRepo.findAll();
    }
}