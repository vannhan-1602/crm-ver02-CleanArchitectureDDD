package com.crm.application.cohoibanhang.handler;

import com.crm.application.cohoibanhang.query.GetAllCoHoiQuery;
import com.crm.application.common.IRequestHandler;
import com.crm.application.sanpham.query.GetAllSanPhamQuery;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.CoiHoiBanHangRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GetAllCoHoiHandler implements IRequestHandler<GetAllCoHoiQuery, List<CoHoiBanHang>> {
    private final CoiHoiBanHangRepo repo;
    public GetAllCoHoiHandler(CoiHoiBanHangRepo repo)
    {
        this.repo=repo;
    }
    @Override
    public List<CoHoiBanHang> handle(GetAllCoHoiQuery request) {
        return repo.findAll();
    }
}
