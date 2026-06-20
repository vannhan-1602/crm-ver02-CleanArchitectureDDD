package com.crm.application.baocao.handler;

import com.crm.application.baocao.dto.BaoCaoTongHopResponse;
import com.crm.application.baocao.dto.BaoCaoTongHopResponse.BreakdownItem;
import com.crm.application.baocao.dto.BaoCaoTongHopResponse.MetricCard;
import com.crm.application.baocao.dto.BaoCaoTongHopResponse.MonthlySummary;
import com.crm.application.baocao.dto.BaoCaoTongHopResponse.OverviewSection;
import com.crm.application.baocao.query.GetBaoCaoTongHopQuery;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.BaoGia;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.entities.HoatDong;
import com.crm.domain.entities.HopDong;
import com.crm.domain.entities.Lead;
import com.crm.domain.entities.PhieuChi;
import com.crm.domain.entities.PhieuThu;
import com.crm.domain.repositories.BaoGiaRepo;
import com.crm.domain.repositories.CoiHoiBanHangRepo;
import com.crm.domain.repositories.HoaDonRepo;
import com.crm.domain.repositories.HoatDongRepo;
import com.crm.domain.repositories.HopDongRepo;
import com.crm.domain.repositories.LeadRepo;
import com.crm.domain.repositories.PhieuChiRepo;
import com.crm.domain.repositories.PhieuThuRepo;
import com.crm.domain.valueobjects.TrangThaiBaoGia;
import com.crm.domain.valueobjects.TrangThaiHopDong;
import com.crm.domain.valueobjects.TrangThaiThanhToan;
import com.crm.domain.valueobjects.TinhTrangLead;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GetBaoCaoTongHopQueryHandler implements IRequestHandler<GetBaoCaoTongHopQuery, BaoCaoTongHopResponse> {
    private static final DateTimeFormatter MONTH_LABEL = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final int MONTH_WINDOW = 6;

    private final LeadRepo leadRepo;
    private final HopDongRepo hopDongRepo;
    private final HoaDonRepo hoaDonRepo;
    private final PhieuThuRepo phieuThuRepo;
    private final PhieuChiRepo phieuChiRepo;
    private final HoatDongRepo hoatDongRepo;
    private final BaoGiaRepo baoGiaRepo;
    private final CoiHoiBanHangRepo coHoiBanHangRepo;

    public GetBaoCaoTongHopQueryHandler(LeadRepo leadRepo,
                                        HopDongRepo hopDongRepo,
                                        HoaDonRepo hoaDonRepo,
                                        PhieuThuRepo phieuThuRepo,
                                        PhieuChiRepo phieuChiRepo,
                                        HoatDongRepo hoatDongRepo,
                                        BaoGiaRepo baoGiaRepo,
                                        CoiHoiBanHangRepo coHoiBanHangRepo) {
        this.leadRepo = leadRepo;
        this.hopDongRepo = hopDongRepo;
        this.hoaDonRepo = hoaDonRepo;
        this.phieuThuRepo = phieuThuRepo;
        this.phieuChiRepo = phieuChiRepo;
        this.hoatDongRepo = hoatDongRepo;
        this.baoGiaRepo = baoGiaRepo;
        this.coHoiBanHangRepo = coHoiBanHangRepo;
    }

    @Override
    @SuppressWarnings("unused")
    public BaoCaoTongHopResponse handle(GetBaoCaoTongHopQuery query) {
        List<Lead> leads = safeLoad("lead", () -> leadRepo.findAll());
        List<HopDong> hopDongs = safeLoad("hop dong", () -> hopDongRepo.findAll());
        List<HoaDon> hoaDons = safeLoad("hoa don", () -> hoaDonRepo.findAll());
        List<PhieuThu> phieuThus = safeLoad("phieu thu", () -> phieuThuRepo.findAll());
        List<PhieuChi> phieuChis = safeLoad("phieu chi", () -> phieuChiRepo.findAll());
        List<HoatDong> hoatDongs = safeLoad("hoat dong", () -> hoatDongRepo.findAll());
        List<BaoGia> baoGias = safeLoad("bao gia", () -> baoGiaRepo.findAll());
        List<CoHoiBanHang> coHois = safeLoad("co hoi ban hang", () -> coHoiBanHangRepo.findAll());

        BigDecimal invoiceValue = sumBigDecimal(hoaDons, HoaDon::getTongTien);
        BigDecimal cashReceived = sumBigDecimal(phieuThus, PhieuThu::getSoTien);
        BigDecimal expenseValue = sumBigDecimal(phieuChis, PhieuChi::getSoTien);
        BigDecimal outstanding = hoaDons.stream()
                .map(hoaDon -> safeSubtract(hoaDon.getTongTien(), hoaDon.getSoTienDaThu()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paidInvoices = hoaDons.stream()
                .filter(hoaDon -> hoaDon.getTrangThaiThanhToan() == TrangThaiThanhToan.HoanTat)
                .count();
        long partialInvoices = hoaDons.stream()
                .filter(hoaDon -> hoaDon.getTrangThaiThanhToan() == TrangThaiThanhToan.ThanhToan1Phan)
                .count();
        long unpaidInvoices = hoaDons.stream()
                .filter(hoaDon -> hoaDon.getTrangThaiThanhToan() == TrangThaiThanhToan.ChuaThanhToan)
                .count();

        long convertedLeads = leads.stream()
                .filter(lead -> lead.getTinhTrang() == TinhTrangLead.DaChuyenDoi)
                .count();
        long nurturingLeads = leads.stream()
                .filter(lead -> lead.getTinhTrang() == TinhTrangLead.DangChamSoc)
                .count();
        long freshLeads = leads.stream()
                .filter(lead -> lead.getTinhTrang() == TinhTrangLead.Moi)
                .count();
        long stoppedLeads = leads.stream()
                .filter(lead -> lead.getTinhTrang() == TinhTrangLead.NgungChamSoc)
                .count();

        long activeContracts = hopDongs.stream()
                .filter(hopDong -> hopDong.getTrangThai() == TrangThaiHopDong.DangThucHien)
                .count();
        long pausedContracts = hopDongs.stream()
                .filter(hopDong -> hopDong.getTrangThai() == TrangThaiHopDong.TamDung)
                .count();
        long closedContracts = hopDongs.stream()
                .filter(hopDong -> hopDong.getTrangThai() == TrangThaiHopDong.ThanhLy)
                .count();

        BigDecimal expectedOpportunityRevenue = sumDoubleBigDecimal(coHois, CoHoiBanHang::getDoanhThuKyVong);
        double averageOpportunitySuccessRate = averageInt(coHois, CoHoiBanHang::getTyLeThanhCong);
        long acceptedQuotes = baoGias.stream()
                .filter(baoGia -> baoGia.getTrangThai() == TrangThaiBaoGia.ChapNhan)
                .count();
        long sentQuotes = baoGias.stream()
                .filter(baoGia -> baoGia.getTrangThai() == TrangThaiBaoGia.DaGui)
                .count();
        long draftQuotes = baoGias.stream()
                .filter(baoGia -> baoGia.getTrangThai() == TrangThaiBaoGia.Nhap)
                .count();
        long rejectedQuotes = baoGias.stream()
                .filter(baoGia -> baoGia.getTrangThai() == TrangThaiBaoGia.TuChoi)
                .count();

        BigDecimal opportunitySuccessRate = rate(convertedLeads, leads.size());
        BigDecimal paymentCollectionRate = rate(cashReceived, invoiceValue);
        BigDecimal quoteAcceptanceRate = rate(acceptedQuotes, baoGias.size());
        BigDecimal contractCompletionRate = rate(closedContracts, hopDongs.size());
        BigDecimal netCashFlow = cashReceived.subtract(expenseValue);

        OverviewSection finance = new OverviewSection(
                "Tài chính",
                List.of(
                        metric("Doanh số hóa đơn", invoiceValue, "currency", "primary", "Tổng giá trị ghi nhận"),
                        metric("Đã thu", cashReceived, "currency", "success", "Tiền đã thực nhận"),
                        metric("Chi phí", expenseValue, "currency", "danger", "Tổng phiếu chi"),
                        metric("Dư nợ", outstanding, "currency", "warning", "Phần chưa thu"),
                        metric("Dòng tiền ròng", netCashFlow, "currency", "primary", "Thu trừ chi"),
                        metric("Tỷ lệ thu tiền", paymentCollectionRate, "percent", "success", "Đã thu / doanh số")
                )
        );

        OverviewSection pipeline = new OverviewSection(
                "KPI quy trình",
                List.of(
                        metric("Lead", BigDecimal.valueOf(leads.size()), "count", "primary", "Tổng lead đang theo dõi"),
                        metric("Lead mới", BigDecimal.valueOf(freshLeads), "count", "neutral", "Lead mới tạo"),
                        metric("Lead đang chăm sóc", BigDecimal.valueOf(nurturingLeads), "count", "primary", "Đang nuôi dưỡng"),
                        metric("Lead đã chuyển đổi", BigDecimal.valueOf(convertedLeads), "count", "success", "Đã thành khách hàng"),
                        metric("Tỷ lệ chuyển đổi", opportunitySuccessRate, "percent", "success", "Lead chuyển đổi / tổng lead"),
                        metric("Hợp đồng thanh lý", BigDecimal.valueOf(closedContracts), "count", "success", "Hợp đồng hoàn tất"),
                        metric("Tỷ lệ hoàn tất hợp đồng", contractCompletionRate, "percent", "primary", "Hợp đồng thanh lý / tổng hợp đồng"),
                        metric("Cơ hội bán hàng", BigDecimal.valueOf(coHois.size()), "count", "warning", "Tổng opportunity"),
                        metric("Doanh thu kỳ vọng", expectedOpportunityRevenue, "currency", "warning", "Tổng doanh thu dự kiến"),
                        metric("Điểm thắng TB", BigDecimal.valueOf(round(averageOpportunitySuccessRate)), "percent", "primary", "Trung bình tỷ lệ thành công")
                )
        );

        OverviewSection operation = new OverviewSection(
                "Hiệu quả hoạt động",
                List.of(
                        metric("Hoạt động", BigDecimal.valueOf(hoatDongs.size()), "count", "primary", "Tổng tương tác ghi nhận"),
                        metric("Cuộc gọi", BigDecimal.valueOf(countActivity(hoatDongs, "Call")), "count", "primary", "Tương tác qua điện thoại"),
                        metric("Cuộc họp", BigDecimal.valueOf(countActivity(hoatDongs, "Meeting")), "count", "warning", "Lịch hẹn / gặp trực tiếp"),
                        metric("Email", BigDecimal.valueOf(countActivity(hoatDongs, "Email")), "count", "neutral", "Giao tiếp qua email"),
                        metric("Zalo", BigDecimal.valueOf(countActivity(hoatDongs, "Zalo")), "count", "success", "Trao đổi qua Zalo"),
                        metric("Báo giá", BigDecimal.valueOf(baoGias.size()), "count", "primary", "Tổng báo giá tạo ra"),
                        metric("Tỷ lệ chấp nhận báo giá", quoteAcceptanceRate, "percent", "success", "Báo giá chấp nhận / tổng báo giá"),
                        metric("Báo giá bị từ chối", BigDecimal.valueOf(rejectedQuotes), "count", "danger", "Đơn hàng chưa chốt"),
                        metric("Báo giá đã gửi", BigDecimal.valueOf(sentQuotes), "count", "warning", "Đang chờ phản hồi"),
                        metric("Báo giá nháp", BigDecimal.valueOf(draftQuotes), "count", "neutral", "Chưa phát hành")
                )
        );

        List<MonthlySummary> monthlySummaries = buildMonthlySummaries(
                hoaDons,
                phieuThus,
                phieuChis,
                leads,
                hopDongs,
                hoatDongs,
                baoGias,
                coHois
        );

        List<BreakdownItem> opportunityStages = buildOpportunityStages(coHois);
        List<BreakdownItem> quoteStatuses = buildQuoteStatuses(baoGias);

        return new BaoCaoTongHopResponse(
                LocalDateTime.now(),
                finance,
                pipeline,
                operation,
                monthlySummaries,
                opportunityStages,
                quoteStatuses
        );
    }

    private List<MonthlySummary> buildMonthlySummaries(List<HoaDon> hoaDons,
                                                       List<PhieuThu> phieuThus,
                                                       List<PhieuChi> phieuChis,
                                                       List<Lead> leads,
                                                       List<HopDong> hopDongs,
                                                       List<HoatDong> hoatDongs,
                                                       List<BaoGia> baoGias,
                                                       List<CoHoiBanHang> coHois) {
        YearMonth current = YearMonth.now();
        List<YearMonth> months = IntStream.range(0, MONTH_WINDOW)
                .mapToObj(offset -> current.minusMonths(MONTH_WINDOW - 1L - offset))
                .toList();

        Map<YearMonth, List<HoaDon>> hoaDonByMonth = groupByMonth(hoaDons, HoaDon::getCreatedAt);
        Map<YearMonth, List<PhieuThu>> phieuThuByMonth = groupByMonth(phieuThus, PhieuThu::getNgayTao);
        Map<YearMonth, List<PhieuChi>> phieuChiByMonth = groupByMonth(phieuChis, PhieuChi::getNgayTao);
        Map<YearMonth, List<Lead>> leadByMonth = groupByMonth(leads, Lead::getCreatedAt);
        Map<YearMonth, List<HopDong>> hopDongByMonth = groupByMonth(hopDongs, HopDong::getCreatedAt);
        Map<YearMonth, List<HoatDong>> hoatDongByMonth = groupByMonth(hoatDongs, HoatDong::getCreatedAt);
        Map<YearMonth, List<BaoGia>> baoGiaByMonth = groupByMonth(baoGias, BaoGia::getCreatedAt);
        Map<YearMonth, List<CoHoiBanHang>> coHoiByMonth = groupByMonth(coHois, CoHoiBanHang::getCreatedAt);

        List<MonthlySummary> result = new ArrayList<>();
        for (YearMonth month : months) {
            List<HoaDon> monthHoaDons = hoaDonByMonth.getOrDefault(month, List.of());
            List<PhieuThu> monthThu = phieuThuByMonth.getOrDefault(month, List.of());
            List<PhieuChi> monthChi = phieuChiByMonth.getOrDefault(month, List.of());
            List<Lead> monthLeads = leadByMonth.getOrDefault(month, List.of());
            List<HopDong> monthHopDongs = hopDongByMonth.getOrDefault(month, List.of());
            List<HoatDong> monthHoatDongs = hoatDongByMonth.getOrDefault(month, List.of());
            List<BaoGia> monthBaoGias = baoGiaByMonth.getOrDefault(month, List.of());
            List<CoHoiBanHang> monthCoHois = coHoiByMonth.getOrDefault(month, List.of());

            BigDecimal monthInvoiceValue = sumBigDecimal(monthHoaDons, HoaDon::getTongTien);
            BigDecimal monthCashReceived = sumBigDecimal(monthThu, PhieuThu::getSoTien);
            BigDecimal monthExpenseValue = sumBigDecimal(monthChi, PhieuChi::getSoTien);
            BigDecimal monthOpportunityValue = sumDoubleBigDecimal(monthCoHois, CoHoiBanHang::getDoanhThuKyVong);

            result.add(new MonthlySummary(
                    month.format(MONTH_LABEL),
                    monthInvoiceValue,
                    monthCashReceived,
                    monthExpenseValue,
                    monthCashReceived.subtract(monthExpenseValue),
                    BigDecimal.valueOf(monthLeads.size()),
                    BigDecimal.valueOf(monthHopDongs.size()),
                    BigDecimal.valueOf(monthHoatDongs.size()),
                    BigDecimal.valueOf(monthBaoGias.size()),
                    monthOpportunityValue
            ));
        }

        return result;
    }

    private List<BreakdownItem> buildOpportunityStages(List<CoHoiBanHang> coHois) {
        if (coHois.isEmpty()) {
            return List.of();
        }
        Map<String, List<CoHoiBanHang>> grouped = coHois.stream()
                .collect(Collectors.groupingBy(coHoiBanHang -> normalizeStage(coHoiBanHang.getGiaiDoan()), LinkedHashMap::new, Collectors.toList()));

        return grouped.entrySet().stream()
                .map(entry -> new BreakdownItem(
                        labelForStage(entry.getKey()),
                        BigDecimal.valueOf(entry.getValue().size()),
                        sumDoubleBigDecimal(entry.getValue(), CoHoiBanHang::getDoanhThuKyVong),
                        BigDecimal.valueOf(round(averageInt(entry.getValue(), CoHoiBanHang::getTyLeThanhCong)))
                ))
                .sorted(Comparator.comparing(BreakdownItem::count).reversed())
                .toList();
    }

    private List<BreakdownItem> buildQuoteStatuses(List<BaoGia> baoGias) {
        if (baoGias.isEmpty()) {
            return List.of();
        }

        Map<TrangThaiBaoGia, List<BaoGia>> grouped = new EnumMap<>(TrangThaiBaoGia.class);
        for (BaoGia baoGia : baoGias) {
            TrangThaiBaoGia status = baoGia.getTrangThai() != null ? baoGia.getTrangThai() : TrangThaiBaoGia.Nhap;
            grouped.computeIfAbsent(status, key -> new ArrayList<>()).add(baoGia);
        }

        List<TrangThaiBaoGia> order = List.of(
                TrangThaiBaoGia.Nhap,
                TrangThaiBaoGia.DaGui,
                TrangThaiBaoGia.ChapNhan,
                TrangThaiBaoGia.TuChoi
        );

        return order.stream()
                .filter(grouped::containsKey)
                .map(status -> {
                    List<BaoGia> items = grouped.get(status);
                    return new BreakdownItem(
                            labelForQuoteStatus(status),
                            BigDecimal.valueOf(items.size()),
                            sumDoubleBigDecimal(items, BaoGia::getTongTien),
                            BigDecimal.valueOf(round(rate(items.size(), baoGias.size()).doubleValue()))
                    );
                })
                .toList();
    }

    private String labelForStage(String stage) {
        if (stage == null || stage.isBlank()) {
            return "Chưa xác định";
        }
        return switch (stage.trim().toLowerCase(Locale.ROOT)) {
            case "moi", "new" -> "Mới";
            case "khampha", "kham_pha", "discover" -> "Khám phá";
            case "de_xuat", "proposal", "proposal_sent" -> "Đề xuất";
            case "damphan", "negotiation" -> "Đàm phán";
            case "thanhcong", "won", "closed_won" -> "Thành công";
            case "thatbai", "lost", "closed_lost" -> "Thất bại";
            default -> toTitleCase(stage);
        };
    }

    private String normalizeStage(String stage) {
        return stage == null ? "Chưa xác định" : stage.trim();
    }

    private String labelForQuoteStatus(TrangThaiBaoGia status) {
        return switch (status) {
            case Nhap -> "Nháp";
            case DaGui -> "Đã gửi";
            case TuChoi -> "Từ chối";
            case ChapNhan -> "Chấp nhận";
        };
    }

    private String toTitleCase(String value) {
        if (value == null || value.isBlank()) {
            return "Chưa xác định";
        }
        String[] parts = value.trim().split("\\s+");
        return IntStream.range(0, parts.length)
                .mapToObj(index -> {
                    String part = parts[index];
                    if (part.isEmpty()) {
                        return part;
                    }
                    return Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase(Locale.ROOT);
                })
                .collect(Collectors.joining(" "));
    }

    private MetricCard metric(String label, BigDecimal value, String format, String tone, String hint) {
        return new MetricCard(label, value, format, tone, hint);
    }

    private <T> Map<YearMonth, List<T>> groupByMonth(List<T> items, Function<T, LocalDateTime> extractor) {
        Map<YearMonth, List<T>> grouped = new HashMap<>();
        for (T item : items) {
            LocalDateTime timestamp = extractor.apply(item);
            if (timestamp == null) {
                continue;
            }
            grouped.computeIfAbsent(YearMonth.from(timestamp), key -> new ArrayList<>()).add(item);
        }
        return grouped;
    }

    private <T> BigDecimal sumBigDecimal(List<T> items, Function<T, BigDecimal> extractor) {
        return items.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private <T> BigDecimal sumDoubleBigDecimal(List<T> items, Function<T, Double> extractor) {
        BigDecimal total = BigDecimal.ZERO;
        for (T item : items) {
            Double value = extractor.apply(item);
            if (value != null) {
                total = total.add(BigDecimal.valueOf(value));
            }
        }
        return total;
    }

    private BigDecimal safeSubtract(BigDecimal total, BigDecimal paid) {
        BigDecimal left = total != null ? total : BigDecimal.ZERO;
        BigDecimal right = paid != null ? paid : BigDecimal.ZERO;
        BigDecimal value = left.subtract(right);
        return value.compareTo(BigDecimal.ZERO) > 0 ? value : BigDecimal.ZERO;
    }

    private BigDecimal rate(long numerator, long denominator) {
        if (denominator <= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(numerator)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(denominator), 1, RoundingMode.HALF_UP);
    }

    private BigDecimal rate(BigDecimal numerator, BigDecimal denominator) {
        if (denominator == null || denominator.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return numerator
                .multiply(BigDecimal.valueOf(100))
                .divide(denominator, 1, RoundingMode.HALF_UP);
    }

    private double averageInt(List<CoHoiBanHang> items, Function<CoHoiBanHang, Integer> extractor) {
        if (items.isEmpty()) {
            return 0D;
        }
        double total = 0D;
        int count = 0;
        for (CoHoiBanHang item : items) {
            Integer value = extractor.apply(item);
            if (value == null) {
                continue;
            }
            total += value;
            count++;
        }
        return count == 0 ? 0D : total / count;
    }

    private long countActivity(List<HoatDong> items, String type) {
        return items.stream()
                .filter(item -> item.getLoaiHoatDong() != null && item.getLoaiHoatDong().name().equalsIgnoreCase(type))
                .count();
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private <T> List<T> safeList(List<T> items) {
        return items != null ? items : List.of();
    }

    private <T> List<T> safeLoad(String sourceName, java.util.function.Supplier<List<T>> loader) {
        try {
            return safeList(loader.get());
        } catch (RuntimeException ex) {
            return List.of();
        }
    }
}
