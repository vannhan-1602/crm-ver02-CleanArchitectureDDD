package com.crm.application.phieuchi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuchi.command.UpdatePhieuChiCommand;
import com.crm.domain.entities.PhieuChi;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.repositories.PhieuChiRepo;
import com.crm.domain.valueobjects.MaPhieuChi;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdatePhieuChiHandler implements IRequestHandler<UpdatePhieuChiCommand, PhieuChi> {
    private final PhieuChiRepo phieuChiRepo;
    private final HoaDonRepo hoaDonRepo;

    public UpdatePhieuChiHandler(PhieuChiRepo phieuChiRepo, HoaDonRepo hoaDonRepo) {
        this.phieuChiRepo = phieuChiRepo;
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public PhieuChi handle(UpdatePhieuChiCommand command) {
        PhieuChi existing = phieuChiRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Phiếu chi không tồn tại: " + command.getId()));

        if (command.getHoaDonId() != null) {
            ensureHoaDonExists(command.getHoaDonId());
        }
        if (command.getMaPhieuChi() != null && !command.getMaPhieuChi().isBlank()) {
            existing.changeMaPhieuChi(new MaPhieuChi(command.getMaPhieuChi()));
        }

        existing.updateDetails(
                command.getKhachHangId(),
                command.getHoaDonId(),
                command.getSoTien(),
                command.getNguoiLapId()
        );

        return phieuChiRepo.save(existing);
    }

    private void ensureHoaDonExists(Long hoaDonId) {
        hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new NoSuchElementException("Hóa đơn không tồn tại: " + hoaDonId));
    }
}
