package com.crm.application.hopdong.handler;

import com.crm.application.hopdong.query.GetAllHopDongQuery;
import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllHopDongQueryHandler {
    private final HopDongRepo hopDongRepo;

    public GetAllHopDongQueryHandler(HopDongRepo hopDongRepo) {
        this.hopDongRepo = hopDongRepo;
    }

    public List<HopDong> handle(GetAllHopDongQuery query) {
        return hopDongRepo.findAll();
    }
}
