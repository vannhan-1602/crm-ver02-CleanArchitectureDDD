package com.crm.application.baogia.handler;

import com.crm.application.baogia.query.GetAllBaoGiaQuery;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.BaoGia;
import com.crm.domain.repositories.BaoGiaRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllBaoGiaQueryHandler implements IRequestHandler<GetAllBaoGiaQuery, List<BaoGia>> {
    private final BaoGiaRepo baoGiaRepo;

    public GetAllBaoGiaQueryHandler(BaoGiaRepo baoGiaRepo) {
        this.baoGiaRepo = baoGiaRepo;
    }

    @Override
    public List<BaoGia> handle(GetAllBaoGiaQuery query) {
        return baoGiaRepo.findAll();
    }
}
