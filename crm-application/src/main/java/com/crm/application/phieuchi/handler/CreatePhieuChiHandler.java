package com.crm.application.phieuchi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuchi.command.CreatePhieuChiCommand;
import com.crm.domain.entities.PhieuChi;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.repositories.PhieuChiRepo;
import com.crm.domain.valueobjects.MaPhieuChi;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CreatePhieuChiHandler implements IRequestHandler<CreatePhieuChiCommand, PhieuChi> {
    private final PhieuChiRepo phieuChiRepo;
    private final HoaDonRepo hoaDonRepo;

    public CreatePhieuChiHandler(PhieuChiRepo phieuChiRepo, HoaDonRepo hoaDonRepo) {
        this.phieuChiRepo = phieuChiRepo;
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public PhieuChi handle(CreatePhieuChiCommand command) {
        ensureHoaDonExists(command.getHoaDonId());
        PhieuChi phieuChi = new PhieuChi(
                new MaPhieuChi(command.getMaPhieuChi()),
                command.getKhachHangId(),
                command.getHoaDonId(),
                command.getSoTien(),
                command.getNguoiLapId()
        );
        return phieuChiRepo.save(phieuChi);
    }

    private void ensureHoaDonExists(Long hoaDonId) {
        hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new NoSuchElementException("Hóa đơn không tồn tại: " + hoaDonId));
    }
}
