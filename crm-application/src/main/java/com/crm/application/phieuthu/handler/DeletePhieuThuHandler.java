package com.crm.application.phieuthu.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuthu.command.DeletePhieuThuCommand;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.PhieuThuRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeletePhieuThuHandler implements IRequestHandler<DeletePhieuThuCommand, Boolean> {
    private final PhieuThuRepo phieuThuRepo;
    private final HoaDonPaymentStatusUpdater paymentStatusUpdater;

    public DeletePhieuThuHandler(PhieuThuRepo phieuThuRepo, HoaDonPaymentStatusUpdater paymentStatusUpdater) {
        this.phieuThuRepo = phieuThuRepo;
        this.paymentStatusUpdater = paymentStatusUpdater;
    }

    @Override
    public Boolean handle(DeletePhieuThuCommand command) {
        PhieuThu existing = phieuThuRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Phiếu thu không tồn tại: " + command.getId()));
        Long hoaDonId = existing.getHoaDonId();
        phieuThuRepo.deleteById(command.getId());
        paymentStatusUpdater.recalculate(hoaDonId);
        return true;
    }
}
