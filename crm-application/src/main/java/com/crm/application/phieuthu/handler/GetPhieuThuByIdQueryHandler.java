package com.crm.application.phieuthu.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuthu.query.GetPhieuThuByIdQuery;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.PhieuThuRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetPhieuThuByIdQueryHandler implements IRequestHandler<GetPhieuThuByIdQuery, PhieuThu> {
    private final PhieuThuRepo phieuThuRepo;

    public GetPhieuThuByIdQueryHandler(PhieuThuRepo phieuThuRepo) {
        this.phieuThuRepo = phieuThuRepo;
    }

    @Override
    public PhieuThu handle(GetPhieuThuByIdQuery query) {
        return phieuThuRepo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException("Phiếu thu không tồn tại: " + query.getId()));
    }
}
