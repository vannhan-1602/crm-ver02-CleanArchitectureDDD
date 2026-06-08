package com.crm.application.hopdong.handler;

import com.crm.application.hopdong.query.GetHopDongByIdQuery;
import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetHopDongByIdQueryHandler {
    private final HopDongRepo hopDongRepo;

    public GetHopDongByIdQueryHandler(HopDongRepo hopDongRepo) {
        this.hopDongRepo = hopDongRepo;
    }

    public Optional<HopDong> handle(GetHopDongByIdQuery query) {
        return hopDongRepo.findById(query.getId());
    }
}
