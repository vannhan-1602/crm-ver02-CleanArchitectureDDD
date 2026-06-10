package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.phieuthu.command.CreatePhieuThuCommand;
import com.crm.application.phieuthu.command.DeletePhieuThuCommand;
import com.crm.application.phieuthu.command.UpdatePhieuThuCommand;
import com.crm.application.phieuthu.query.GetAllPhieuThuQuery;
import com.crm.application.phieuthu.query.GetPhieuThuByIdQuery;
import com.crm.domain.entities.PhieuThu;
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
@RequestMapping("/api/phieu-thu")
public class PhieuThuController {
    private final Mediator mediator;

    public PhieuThuController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhieuThuResponse create(@RequestBody CreatePhieuThuRequest request) {
        PhieuThu phieuThu = mediator.send(new CreatePhieuThuCommand(
                request.getMaPhieuThu(),
                request.getKhachHangId(),
                request.getHoaDonId(),
                request.getSoTien(),
                request.getNguoiLapId()
        ));
        return PhieuThuResponse.from(phieuThu);
    }

    @GetMapping
    public List<PhieuThuResponse> getAll() {
        List<PhieuThu> phieuThus = mediator.send(new GetAllPhieuThuQuery());
        return phieuThus.stream()
                .map(PhieuThuResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public PhieuThuResponse getById(@PathVariable Long id) {
        return PhieuThuResponse.from(mediator.send(new GetPhieuThuByIdQuery(id)));
    }

    @PutMapping("/{id}")
    public PhieuThuResponse update(@PathVariable Long id, @RequestBody UpdatePhieuThuRequest request) {
        UpdatePhieuThuCommand command = new UpdatePhieuThuCommand(
                request.getMaPhieuThu(),
                request.getKhachHangId(),
                request.getHoaDonId(),
                request.getSoTien(),
                request.getNguoiLapId()
        );
        command.setId(id);
        return PhieuThuResponse.from(mediator.send(command));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeletePhieuThuCommand(id));
    }

    static class CreatePhieuThuRequest {
        private String maPhieuThu;
        private Long khachHangId;
        private Long hoaDonId;
        private BigDecimal soTien;
        private Integer nguoiLapId;

        public String getMaPhieuThu() {
            return maPhieuThu;
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

    static class UpdatePhieuThuRequest {
        private String maPhieuThu;
        private Long khachHangId;
        private Long hoaDonId;
        private BigDecimal soTien;
        private Integer nguoiLapId;

        public String getMaPhieuThu() {
            return maPhieuThu;
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

    static class PhieuThuResponse {
        private Long id;
        private String maPhieuThu;
        private Long khachHangId;
        private Long hoaDonId;
        private BigDecimal soTien;
        private Integer nguoiLapId;
        private LocalDateTime ngayTao;
        private LocalDateTime updatedAt;

        public static PhieuThuResponse from(PhieuThu phieuThu) {
            PhieuThuResponse response = new PhieuThuResponse();
            response.id = phieuThu.getId();
            response.maPhieuThu = phieuThu.getMaPhieuThu().getValue();
            response.khachHangId = phieuThu.getKhachHangId();
            response.hoaDonId = phieuThu.getHoaDonId();
            response.soTien = phieuThu.getSoTien();
            response.nguoiLapId = phieuThu.getNguoiLapId();
            response.ngayTao = phieuThu.getNgayTao();
            response.updatedAt = phieuThu.getUpdatedAt();
            return response;
        }

        public Long getId() {
            return id;
        }

        public String getMaPhieuThu() {
            return maPhieuThu;
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
