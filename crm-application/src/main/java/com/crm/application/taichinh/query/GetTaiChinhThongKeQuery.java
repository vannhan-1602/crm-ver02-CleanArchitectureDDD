package com.crm.application.taichinh.query;

import com.crm.application.common.IRequest;
import com.crm.application.taichinh.dto.TaiChinhThongKe;

import java.time.LocalDate;

public class GetTaiChinhThongKeQuery implements IRequest<TaiChinhThongKe> {
    private final LocalDate from;
    private final LocalDate to;

    public GetTaiChinhThongKeQuery(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }
}
