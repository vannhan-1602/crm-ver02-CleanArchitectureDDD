package com.crm.application.phieuthu.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuthu.query.GetAllPhieuThuQuery;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.PhieuThuRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllPhieuThuQueryHandler implements IRequestHandler<GetAllPhieuThuQuery, List<PhieuThu>> {
    private final PhieuThuRepo phieuThuRepo;

    public GetAllPhieuThuQueryHandler(PhieuThuRepo phieuThuRepo) {
        this.phieuThuRepo = phieuThuRepo;
    }

    @Override
    public List<PhieuThu> handle(GetAllPhieuThuQuery query) {
        return phieuThuRepo.findAll();
    }
}
