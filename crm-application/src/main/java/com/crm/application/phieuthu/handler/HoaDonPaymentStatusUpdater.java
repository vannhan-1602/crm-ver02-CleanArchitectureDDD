package com.crm.application.phieuthu.handler;

import com.crm.domain.entities.HoaDon;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.repositories.PhieuThuRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class HoaDonPaymentStatusUpdater {
    private final HoaDonRepo hoaDonRepo;
    private final PhieuThuRepo phieuThuRepo;

    public HoaDonPaymentStatusUpdater(HoaDonRepo hoaDonRepo, PhieuThuRepo phieuThuRepo) {
        this.hoaDonRepo = hoaDonRepo;
        this.phieuThuRepo = phieuThuRepo;
    }

    public void ensureHoaDonExists(Long hoaDonId) {
        hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new NoSuchElementException("Hóa đơn không tồn tại: " + hoaDonId));
    }

    public HoaDon recalculate(Long hoaDonId) {
        HoaDon hoaDon = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new NoSuchElementException("Hóa đơn không tồn tại: " + hoaDonId));
        BigDecimal tongDaThu = phieuThuRepo.findByHoaDonId(hoaDonId)
                .stream()
                .map(PhieuThu::getSoTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        hoaDon.capNhatTrangThaiThanhToan(tongDaThu);
        return hoaDonRepo.save(hoaDon);
    }
}
