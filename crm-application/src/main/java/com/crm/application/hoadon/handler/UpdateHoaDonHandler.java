package com.crm.application.hoadon.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoadon.command.UpdateHoaDonCommand;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.valueobjects.MaHoaDon;
import com.crm.domain.valueobjects.TrangThaiThanhToan;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateHoaDonHandler implements IRequestHandler<UpdateHoaDonCommand, HoaDon> {
    private final HoaDonRepo hoaDonRepo;

    public UpdateHoaDonHandler(HoaDonRepo hoaDonRepo) {
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public HoaDon handle(UpdateHoaDonCommand command) {
        HoaDon existing = hoaDonRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Hóa đơn không tồn tại: " + command.getId()));

        if (command.getMaHoaDon() != null && !command.getMaHoaDon().isBlank()) {
            existing.changeMaHoaDon(new MaHoaDon(command.getMaHoaDon()));
        }

        existing.updateDetails(
                command.getHopDongId(),
                command.getKhachHangId(),
                command.getTongTien(),
                command.getTrangThaiThanhToan() == null
                        ? null
                        : TrangThaiThanhToan.from(command.getTrangThaiThanhToan())
        );

        return hoaDonRepo.save(existing);
    }
}
