package com.crm.application.cohoibanhang.handler;

import com.crm.application.cohoibanhang.query.GetCoHoiBanHangByIdQuery;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.CoiHoiBanHangRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetCoHoiBanHangByIdQueryHandler
        implements IRequestHandler<GetCoHoiBanHangByIdQuery, CoHoiBanHang> {

    private final CoiHoiBanHangRepo repo;

    public GetCoHoiBanHangByIdQueryHandler(CoiHoiBanHangRepo repo) {
        this.repo = repo;
    }

    @Override
    public CoHoiBanHang handle(GetCoHoiBanHangByIdQuery query) {
        return repo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Co hoi ban hang khong ton tai: " + query.getId()));
    }
}