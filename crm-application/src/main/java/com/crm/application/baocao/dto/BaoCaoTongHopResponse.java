package com.crm.application.baocao.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BaoCaoTongHopResponse(
        LocalDateTime generatedAt,
        OverviewSection finance,
        OverviewSection pipeline,
        OverviewSection operation,
        List<MonthlySummary> monthlySummaries,
        List<BreakdownItem> opportunityStages,
        List<BreakdownItem> quoteStatuses
) {
    public record OverviewSection(String title, List<MetricCard> metrics) {
    }

    public record MetricCard(String label, BigDecimal value, String format, String tone, String hint) {
    }

    public record MonthlySummary(
            String month,
            BigDecimal invoiceValue,
            BigDecimal cashReceived,
            BigDecimal expenseValue,
            BigDecimal netCashFlow,
            BigDecimal leadCount,
            BigDecimal contractCount,
            BigDecimal activityCount,
            BigDecimal quoteCount,
            BigDecimal opportunityValue
    ) {
    }

    public record BreakdownItem(String label, BigDecimal count, BigDecimal amount, BigDecimal rate) {
    }
}
