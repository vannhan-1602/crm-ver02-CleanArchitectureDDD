package com.crm.application.phieuthu.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuthu.command.UpdatePhieuThuCommand;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.PhieuThuRepo;
import com.crm.domain.valueobjects.MaPhieuThu;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdatePhieuThuHandler implements IRequestHandler<UpdatePhieuThuCommand, PhieuThu> {
    private final PhieuThuRepo phieuThuRepo;
    private final HoaDonPaymentStatusUpdater paymentStatusUpdater;

    public UpdatePhieuThuHandler(PhieuThuRepo phieuThuRepo, HoaDonPaymentStatusUpdater paymentStatusUpdater) {
        this.phieuThuRepo = phieuThuRepo;
        this.paymentStatusUpdater = paymentStatusUpdater;
    }

    @Override
    public PhieuThu handle(UpdatePhieuThuCommand command) {
        PhieuThu existing = phieuThuRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Phiếu thu không tồn tại: " + command.getId()));
        Long oldHoaDonId = existing.getHoaDonId();

        if (command.getHoaDonId() != null) {
            paymentStatusUpdater.ensureHoaDonExists(command.getHoaDonId());
        }
        if (command.getMaPhieuThu() != null && !command.getMaPhieuThu().isBlank()) {
            existing.changeMaPhieuThu(new MaPhieuThu(command.getMaPhieuThu()));
        }

        existing.updateDetails(
                command.getKhachHangId(),
                command.getHoaDonId(),
                command.getSoTien(),
                command.getNguoiLapId()
        );

        PhieuThu saved = phieuThuRepo.save(existing);
        if (!oldHoaDonId.equals(saved.getHoaDonId())) {
            paymentStatusUpdater.recalculate(oldHoaDonId);
        }
        paymentStatusUpdater.recalculate(saved.getHoaDonId());
        return saved;
    }
}
