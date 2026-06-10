package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.phieuchi.command.CreatePhieuChiCommand;
import com.crm.application.phieuchi.command.DeletePhieuChiCommand;
import com.crm.application.phieuchi.command.UpdatePhieuChiCommand;
import com.crm.application.phieuchi.query.GetAllPhieuChiQuery;
import com.crm.application.phieuchi.query.GetPhieuChiByIdQuery;
import com.crm.domain.entities.PhieuChi;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/phieu-chi")
public class PhieuChiController {
    private final Mediator mediator;

    public PhieuChiController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhieuChiResponse create(@RequestBody CreatePhieuChiRequest request) {
        PhieuChi phieuChi = mediator.send(new CreatePhieuChiCommand(
                request.getMaPhieuChi(),
                request.getKhachHangId(),
                request.getHoaDonId(),
                request.getSoTien(),
                request.getNguoiLapId()
        ));
        return PhieuChiResponse.from(phieuChi);
    }

    @GetMapping
    public List<PhieuChiResponse> getAll() {
        List<PhieuChi> phieuChis = mediator.send(new GetAllPhieuChiQuery());
        return phieuChis.stream()
                .map(PhieuChiResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public PhieuChiResponse getById(@PathVariable Long id) {
        return PhieuChiResponse.from(mediator.send(new GetPhieuChiByIdQuery(id)));
    }

    @PutMapping("/{id}")
    public PhieuChiResponse update(@PathVariable Long id, @RequestBody UpdatePhieuChiRequest request) {
        UpdatePhieuChiCommand command = new UpdatePhieuChiCommand(
                request.getMaPhieuChi(),
                request.getKhachHangId(),
                request.getHoaDonId(),
                request.getSoTien(),
                request.getNguoiLapId()
        );
        command.setId(id);
        return PhieuChiResponse.from(mediator.send(command));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeletePhieuChiCommand(id));
    }

    static class CreatePhieuChiRequest {
        private String maPhieuChi;
        private Long khachHangId;
        private Long hoaDonId;
        private BigDecimal soTien;
        private Integer nguoiLapId;

        public String getMaPhieuChi() {
            return maPhieuChi;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public Long getHoaDonId() {
            return hoaDonId;
        }

        public BigDecimal getSoTien() {
            return soTien;
        }

        public Integer getNguoiLapId() {
            return nguoiLapId;
        }
    }

    static class UpdatePhieuChiRequest {
        private String maPhieuChi;
        private Long khachHangId;
        private Long hoaDonId;
        private BigDecimal soTien;
        private Integer nguoiLapId;

        public String getMaPhieuChi() {
            return maPhieuChi;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public Long getHoaDonId() {
            return hoaDonId;
        }

        public BigDecimal getSoTien() {
            return soTien;
        }

        public Integer getNguoiLapId() {
            return nguoiLapId;
        }
    }

    static class PhieuChiResponse {
        private Long id;
        private String maPhieuChi;
        private Long khachHangId;
        private Long hoaDonId;
        private BigDecimal soTien;
        private Integer nguoiLapId;
        private LocalDateTime ngayTao;
        private LocalDateTime updatedAt;

        public static PhieuChiResponse from(PhieuChi phieuChi) {
            PhieuChiResponse response = new PhieuChiResponse();
            response.id = phieuChi.getId();
            response.maPhieuChi = phieuChi.getMaPhieuChi().getValue();
            response.khachHangId = phieuChi.getKhachHangId();
            response.hoaDonId = phieuChi.getHoaDonId();
            response.soTien = phieuChi.getSoTien();
            response.nguoiLapId = phieuChi.getNguoiLapId();
            response.ngayTao = phieuChi.getNgayTao();
            response.updatedAt = phieuChi.getUpdatedAt();
            return response;
        }

        public Long getId() {
            return id;
        }

        public String getMaPhieuChi() {
            return maPhieuChi;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public Long getHoaDonId() {
            return hoaDonId;
        }

        public BigDecimal getSoTien() {
            return soTien;
        }

        public Integer getNguoiLapId() {
            return nguoiLapId;
        }

        public LocalDateTime getNgayTao() {
            return ngayTao;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
