package com.crm.application.hoadon.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoadon.query.GetHoaDonByIdQuery;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.repositories.HoaDonRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetHoaDonByIdQueryHandler implements IRequestHandler<GetHoaDonByIdQuery, HoaDon> {
    private final HoaDonRepo hoaDonRepo;

    public GetHoaDonByIdQueryHandler(HoaDonRepo hoaDonRepo) {
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public HoaDon handle(GetHoaDonByIdQuery query) {
        return hoaDonRepo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException("Hóa đơn không tồn tại: " + query.getId()));
    }
}
