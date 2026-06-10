package com.crm.application.hoadon.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoadon.query.GetAllHoaDonQuery;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.repositories.HoaDonRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllHoaDonQueryHandler implements IRequestHandler<GetAllHoaDonQuery, List<HoaDon>> {
    private final HoaDonRepo hoaDonRepo;

    public GetAllHoaDonQueryHandler(HoaDonRepo hoaDonRepo) {
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public List<HoaDon> handle(GetAllHoaDonQuery query) {
        return hoaDonRepo.findAll();
    }
}
