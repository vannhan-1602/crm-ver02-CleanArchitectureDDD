package com.crm.application.phieuchi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuchi.query.GetPhieuChiByIdQuery;
import com.crm.domain.entities.PhieuChi;
import com.crm.domain.repositories.PhieuChiRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetPhieuChiByIdQueryHandler implements IRequestHandler<GetPhieuChiByIdQuery, PhieuChi> {
    private final PhieuChiRepo phieuChiRepo;

    public GetPhieuChiByIdQueryHandler(PhieuChiRepo phieuChiRepo) {
        this.phieuChiRepo = phieuChiRepo;
    }

    @Override
    public PhieuChi handle(GetPhieuChiByIdQuery query) {
        return phieuChiRepo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException("Phiếu chi không tồn tại: " + query.getId()));
    }
}
