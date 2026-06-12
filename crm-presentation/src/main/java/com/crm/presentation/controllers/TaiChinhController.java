package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.taichinh.dto.TaiChinhThongKe;
import com.crm.application.taichinh.query.GetTaiChinhThongKeQuery;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tai-chinh")
public class TaiChinhController {
    private final Mediator mediator;

    public TaiChinhController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/thong-ke")
    public TaiChinhThongKe thongKe(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return mediator.send(new GetTaiChinhThongKeQuery(from, to));
    }
}
