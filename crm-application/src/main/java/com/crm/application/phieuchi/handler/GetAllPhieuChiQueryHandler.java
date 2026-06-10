package com.crm.application.phieuchi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuchi.query.GetAllPhieuChiQuery;
import com.crm.domain.entities.PhieuChi;
import com.crm.domain.repositories.PhieuChiRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllPhieuChiQueryHandler implements IRequestHandler<GetAllPhieuChiQuery, List<PhieuChi>> {
    private final PhieuChiRepo phieuChiRepo;

    public GetAllPhieuChiQueryHandler(PhieuChiRepo phieuChiRepo) {
        this.phieuChiRepo = phieuChiRepo;
    }

    @Override
    public List<PhieuChi> handle(GetAllPhieuChiQuery query) {
        return phieuChiRepo.findAll();
    }
}
