package com.crm.application.taichinh.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.taichinh.dto.TaiChinhThongKe;
import com.crm.application.taichinh.query.GetTaiChinhThongKeQuery;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.entities.PhieuChi;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.repositories.PhieuChiRepo;
import com.crm.domain.repositories.PhieuThuRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class GetTaiChinhThongKeQueryHandler implements IRequestHandler<GetTaiChinhThongKeQuery, TaiChinhThongKe> {
    private final HoaDonRepo hoaDonRepo;
    private final PhieuThuRepo phieuThuRepo;
    private final PhieuChiRepo phieuChiRepo;

    public GetTaiChinhThongKeQueryHandler(HoaDonRepo hoaDonRepo,
                                          PhieuThuRepo phieuThuRepo,
                                          PhieuChiRepo phieuChiRepo) {
        this.hoaDonRepo = hoaDonRepo;
        this.phieuThuRepo = phieuThuRepo;
        this.phieuChiRepo = phieuChiRepo;
    }

    @Override
    public TaiChinhThongKe handle(GetTaiChinhThongKeQuery query) {
        LocalDateTime from = query.getFrom() == null ? null : query.getFrom().atStartOfDay();
        LocalDateTime to = query.getTo() == null ? null : query.getTo().atTime(LocalTime.MAX);
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Khoang thoi gian khong hop le");
        }

        List<HoaDon> hoaDons = hoaDonRepo.findByCreatedAtBetween(from, to);
        List<PhieuThu> phieuThus = phieuThuRepo.findByNgayTaoBetween(from, to);
        List<PhieuChi> phieuChis = phieuChiRepo.findByNgayTaoBetween(from, to);

        BigDecimal tongHoaDon = hoaDons.stream()
                .map(HoaDon::getTongTien)
                .map(this::zeroIfNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongDaThu = hoaDons.stream()
                .map(HoaDon::getSoTienDaThu)
                .map(this::zeroIfNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongConNo = hoaDons.stream()
                .map(this::calculateConNo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongPhieuThu = phieuThus.stream()
                .map(PhieuThu::getSoTien)
                .map(this::zeroIfNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tongPhieuChi = phieuChis.stream()
                .map(PhieuChi::getSoTien)
                .map(this::zeroIfNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long soPhieuThu = phieuThus.size();
        long soPhieuChi = phieuChis.size();
        return new TaiChinhThongKe(
                tongHoaDon,
                tongDaThu,
                tongConNo,
                tongPhieuThu,
                tongPhieuChi,
                hoaDons.size(),
                soPhieuThu,
                soPhieuChi,
                soPhieuThu + soPhieuChi
        );
    }

    private BigDecimal calculateConNo(HoaDon hoaDon) {
        BigDecimal conNo = zeroIfNull(hoaDon.getTongTien()).subtract(zeroIfNull(hoaDon.getSoTienDaThu()));
        return conNo.compareTo(BigDecimal.ZERO) > 0 ? conNo : BigDecimal.ZERO;
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
