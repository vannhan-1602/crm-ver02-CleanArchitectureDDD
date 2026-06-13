package com.crm.application.cohoibanhang.handler;

import com.crm.application.cohoibanhang.command.CreateCoHoiBanHangCommand;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.CoiHoiBanHangRepo;
import org.springframework.stereotype.Service;

@Service
public class CreateCoHoiBanHangHandler
        implements IRequestHandler<CreateCoHoiBanHangCommand, CoHoiBanHang> {

    private final CoiHoiBanHangRepo repo;

    public CreateCoHoiBanHangHandler(CoiHoiBanHangRepo repo) {
        this.repo = repo;
    }

    @Override
    public CoHoiBanHang handle(CreateCoHoiBanHangCommand command) {
        if (command.getTenThuongVu() == null || command.getTenThuongVu().isBlank()) {
            throw new IllegalArgumentException("TenThuongVu khong duoc de trong");
        }
        CoHoiBanHang cohoi = new CoHoiBanHang(
                command.getTenThuongVu(),
                command.getGiaiDoan(),
                command.getKhachHangId(),
                command.getLeadId(),
                command.getTyLeThanhCong(),
                command.getDoanhThuKyVong(),
                command.getGhiChu(),
                command.getNgayDuKien(),
                command.getNhanVienPhuTrachId()
        );
        return repo.save(cohoi);
    }
}