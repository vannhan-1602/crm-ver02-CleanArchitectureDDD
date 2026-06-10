package com.crm.application.phieuthu.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuthu.command.CreatePhieuThuCommand;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.PhieuThuRepo;
import com.crm.domain.valueobjects.MaPhieuThu;
import org.springframework.stereotype.Service;

@Service
public class CreatePhieuThuHandler implements IRequestHandler<CreatePhieuThuCommand, PhieuThu> {
    private final PhieuThuRepo phieuThuRepo;
    private final HoaDonPaymentStatusUpdater paymentStatusUpdater;

    public CreatePhieuThuHandler(PhieuThuRepo phieuThuRepo, HoaDonPaymentStatusUpdater paymentStatusUpdater) {
        this.phieuThuRepo = phieuThuRepo;
        this.paymentStatusUpdater = paymentStatusUpdater;
    }

    @Override
    public PhieuThu handle(CreatePhieuThuCommand command) {
        paymentStatusUpdater.ensureHoaDonExists(command.getHoaDonId());
        PhieuThu phieuThu = new PhieuThu(
                new MaPhieuThu(command.getMaPhieuThu()),
                command.getKhachHangId(),
                command.getHoaDonId(),
                command.getSoTien(),
                command.getNguoiLapId()
        );
        PhieuThu saved = phieuThuRepo.save(phieuThu);
        paymentStatusUpdater.recalculate(saved.getHoaDonId());
        return saved;
    }
}
