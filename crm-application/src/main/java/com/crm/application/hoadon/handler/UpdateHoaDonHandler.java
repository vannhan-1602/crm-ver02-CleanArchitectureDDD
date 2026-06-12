package com.crm.application.hoadon.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoadon.command.UpdateHoaDonCommand;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.repositories.HopDongRepo;
import com.crm.domain.repositories.KhachHangRepo;
import com.crm.domain.valueobjects.MaHoaDon;
import com.crm.domain.valueobjects.TrangThaiThanhToan;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateHoaDonHandler implements IRequestHandler<UpdateHoaDonCommand, HoaDon> {
    private final HoaDonRepo hoaDonRepo;
    private final KhachHangRepo khachHangRepo;
    private final HopDongRepo hopDongRepo;

    public UpdateHoaDonHandler(HoaDonRepo hoaDonRepo,
                               KhachHangRepo khachHangRepo,
                               HopDongRepo hopDongRepo) {
        this.hoaDonRepo = hoaDonRepo;
        this.khachHangRepo = khachHangRepo;
        this.hopDongRepo = hopDongRepo;
    }

    @Override
    public HoaDon handle(UpdateHoaDonCommand command) {
        HoaDon existing = hoaDonRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Hoa don khong ton tai: " + command.getId()));

        validateKhachHang(command.getKhachHangId());
        validateHopDong(command.getHopDongId());

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

    private void validateKhachHang(Long khachHangId) {
        if (khachHangId == null) {
            return;
        }
        if (khachHangId <= 0) {
            throw new IllegalArgumentException("Khach hang id khong hop le");
        }
        khachHangRepo.findById(khachHangId)
                .orElseThrow(() -> new NoSuchElementException("Khach hang khong ton tai: " + khachHangId));
    }

    private void validateHopDong(Long hopDongId) {
        if (hopDongId == null) {
            return;
        }
        hopDongRepo.findById(hopDongId)
                .orElseThrow(() -> new NoSuchElementException("Hop dong khong ton tai: " + hopDongId));
    }
}
