package com.crm.application.baogia.handler;

import com.crm.application.baogia.query.GetBaoGiaByIdQuery;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.BaoGia;
import com.crm.domain.repositories.BaoGiaRepo;
import org.springframework.stereotype.Service;

@Service
public class GetBaoGiaByIdQueryHandler implements IRequestHandler<GetBaoGiaByIdQuery, BaoGia> {
    private final BaoGiaRepo baoGiaRepo;

    public GetBaoGiaByIdQueryHandler(BaoGiaRepo baoGiaRepo) {
        this.baoGiaRepo = baoGiaRepo;
    }

    @Override
    public BaoGia handle(GetBaoGiaByIdQuery query) {
        return baoGiaRepo.findById(query.getId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay bao gia: " + query.getId()));
    }
}
