package com.crm.application.hopdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hopdong.query.GetHopDongByIdQuery;
import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetHopDongByIdQueryHandler implements IRequestHandler<GetHopDongByIdQuery, HopDong> {
    private final HopDongRepo hopDongRepo;

    public GetHopDongByIdQueryHandler(HopDongRepo hopDongRepo) {
        this.hopDongRepo = hopDongRepo;
    }

    @Override
    public HopDong handle(GetHopDongByIdQuery query) {
        return hopDongRepo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException("Hop dong khong ton tai: " + query.getId()));
    }
}
