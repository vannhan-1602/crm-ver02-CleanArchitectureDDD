
package com.crm.application.nhanvien.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.nhanvien.query.GetAllNhanVienQuery;
import com.crm.domain.entities.NhanVien;
import com.crm.domain.repositories.NhanVienRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllNhanVienQueryHandler
        implements IRequestHandler<GetAllNhanVienQuery, List<NhanVien>> {

    private final NhanVienRepository nhanVienRepository;

    public GetAllNhanVienQueryHandler(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
    }

    @Override
    public List<NhanVien> handle(GetAllNhanVienQuery query) {
        return nhanVienRepository.findAll();
    }
}