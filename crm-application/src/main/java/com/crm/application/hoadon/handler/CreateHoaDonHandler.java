package com.crm.application.hoadon.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoadon.command.CreateHoaDonCommand;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.valueobjects.MaHoaDon;
import com.crm.domain.valueobjects.TrangThaiThanhToan;
import org.springframework.stereotype.Service;

@Service
public class CreateHoaDonHandler implements IRequestHandler<CreateHoaDonCommand, HoaDon> {
    private final HoaDonRepo hoaDonRepo;

    public CreateHoaDonHandler(HoaDonRepo hoaDonRepo) {
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public HoaDon handle(CreateHoaDonCommand command) {
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
}
