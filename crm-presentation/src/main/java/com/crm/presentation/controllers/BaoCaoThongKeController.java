package com.crm.presentation.controllers;

import com.crm.application.baocao.dto.BaoCaoTongHopResponse;
import com.crm.application.baocao.handler.GetBaoCaoTongHopQueryHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/bao-cao-thong-ke")
public class BaoCaoThongKeController {
    private final GetBaoCaoTongHopQueryHandler handler;

    public BaoCaoThongKeController(GetBaoCaoTongHopQueryHandler handler) {
        this.handler = handler;
    }

    @GetMapping
    public BaoCaoTongHopResponse getReport() {
        return handler.handle(new com.crm.application.baocao.query.GetBaoCaoTongHopQuery());
    }
}
