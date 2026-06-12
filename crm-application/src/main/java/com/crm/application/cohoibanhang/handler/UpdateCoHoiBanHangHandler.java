package com.crm.application.cohoibanhang.handler;

import com.crm.application.cohoibanhang.command.UpdateCoHoiBanHangCommand;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.CoiHoiBanHangRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateCoHoiBanHangHandler
        implements IRequestHandler<UpdateCoHoiBanHangCommand, CoHoiBanHang> {

    private final CoiHoiBanHangRepo repo;

    public UpdateCoHoiBanHangHandler(CoiHoiBanHangRepo repo) {
        this.repo = repo;
    }

    @Override
    public CoHoiBanHang handle(UpdateCoHoiBanHangCommand command) {
        System.out.println("DEBUG command: id=" + command.getId()
                + ", tenThuongVu=" + command.getTenThuongVu());
        repo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Co hoi ban hang khong ton tai: " + command.getId()));

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

      return repo.update(command.getId(), cohoi);

    }
}