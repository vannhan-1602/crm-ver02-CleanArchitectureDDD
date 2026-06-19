package com.crm.application.hoadon.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoadon.command.CreateHoaDonCommand;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.repositories.HopDongRepo;
import com.crm.domain.repositories.KhachHangRepo;
import com.crm.domain.valueobjects.MaHoaDon;
import com.crm.domain.valueobjects.TrangThaiThanhToan;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CreateHoaDonHandler implements IRequestHandler<CreateHoaDonCommand, HoaDon> {
    private final HoaDonRepo hoaDonRepo;
    private final KhachHangRepo khachHangRepo;
    private final HopDongRepo hopDongRepo;

    public CreateHoaDonHandler(HoaDonRepo hoaDonRepo,
                               KhachHangRepo khachHangRepo,
                               HopDongRepo hopDongRepo) {
        this.hoaDonRepo = hoaDonRepo;
        this.khachHangRepo = khachHangRepo;
        this.hopDongRepo = hopDongRepo;
    }

    @Override
    public HoaDon handle(CreateHoaDonCommand command) {
        validateKhachHang(command.getKhachHangId());
        validateHopDong(command.getHopDongId());

        HoaDon hoaDon = new HoaDon(
                new MaHoaDon(command.getMaHoaDon()),
                command.getHopDongId(),
                command.getKhachHangId(),
                command.getTongTien(),
                command.getSoTienDaThu(),
                command.getTrangThaiThanhToan() == null || command.getTrangThaiThanhToan().isBlank()
                        ? null
                        : TrangThaiThanhToan.from(command.getTrangThaiThanhToan())
        );
        return hoaDonRepo.save(hoaDon);
    }

    private void validateKhachHang(Long khachHangId) {
        if (khachHangId == null || khachHangId <= 0) {
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
