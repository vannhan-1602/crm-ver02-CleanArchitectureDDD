package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.hoadon.command.CreateHoaDonCommand;
import com.crm.application.hoadon.command.DeleteHoaDonCommand;
import com.crm.application.hoadon.command.UpdateHoaDonCommand;
import com.crm.application.hoadon.query.GetAllHoaDonQuery;
import com.crm.application.hoadon.query.GetHoaDonByIdQuery;
import com.crm.domain.entities.HoaDon;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.KhachHangRepo;
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
@RequestMapping("/api/hoa-don")
public class HoaDonController {
    private final Mediator mediator;
    private final KhachHangRepo khachHangRepo;

    public HoaDonController(Mediator mediator, KhachHangRepo khachHangRepo) {
        this.mediator = mediator;
        this.khachHangRepo = khachHangRepo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HoaDonResponse create(@RequestBody CreateHoaDonRequest request) {
        HoaDon hoaDon = mediator.send(new CreateHoaDonCommand(
                request.getMaHoaDon(),
                request.getHopDongId(),
                request.getKhachHangId(),
                request.getTongTien(),
                request.getSoTienDaThu(),
                request.getTrangThaiThanhToan()
        ));
        return HoaDonResponse.from(hoaDon, khachHangRepo);
    }

    @GetMapping
    public List<HoaDonResponse> getAll() {
        List<HoaDon> hoaDons = mediator.send(new GetAllHoaDonQuery());
        return hoaDons.stream()
                .map(hoaDon -> HoaDonResponse.from(hoaDon, khachHangRepo))
                .toList();
    }

    @GetMapping("/{id}")
    public HoaDonResponse getById(@PathVariable Long id) {
        return HoaDonResponse.from(mediator.send(new GetHoaDonByIdQuery(id)), khachHangRepo);
    }

    @PutMapping("/{id}")
    public HoaDonResponse update(@PathVariable Long id, @RequestBody UpdateHoaDonRequest request) {
        UpdateHoaDonCommand command = new UpdateHoaDonCommand(
                request.getMaHoaDon(),
                request.getHopDongId(),
                request.getKhachHangId(),
                request.getTongTien(),
                request.getTrangThaiThanhToan()
        );
        command.setId(id);
        return HoaDonResponse.from(mediator.send(command), khachHangRepo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeleteHoaDonCommand(id));
    }

    static class CreateHoaDonRequest {
        private String maHoaDon;
        private Long hopDongId;
        private Long khachHangId;
        private BigDecimal tongTien;
        private BigDecimal soTienDaThu;
        private String trangThaiThanhToan;

        public String getMaHoaDon() {
            return maHoaDon;
        }

        public Long getHopDongId() {
            return hopDongId;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public BigDecimal getTongTien() {
            return tongTien;
        }

        public BigDecimal getSoTienDaThu() {
            return soTienDaThu;
        }

        public String getTrangThaiThanhToan() {
            return trangThaiThanhToan;
        }
    }

    static class UpdateHoaDonRequest {
        private String maHoaDon;
        private Long hopDongId;
        private Long khachHangId;
        private BigDecimal tongTien;
        private String trangThaiThanhToan;

        public String getMaHoaDon() {
            return maHoaDon;
        }

        public Long getHopDongId() {
            return hopDongId;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public BigDecimal getTongTien() {
            return tongTien;
        }

        public String getTrangThaiThanhToan() {
            return trangThaiThanhToan;
        }
    }

    static class HoaDonResponse {
        private Long id;
        private String maHoaDon;
        private Long hopDongId;
        private Long khachHangId;
        private String tenKhachHang;
        private BigDecimal tongTien;
        private BigDecimal soTienDaThu;
        private String trangThaiThanhToan;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static HoaDonResponse from(HoaDon hoaDon, KhachHangRepo khachHangRepo) {
            HoaDonResponse response = new HoaDonResponse();
            response.id = hoaDon.getId();
            response.maHoaDon = hoaDon.getMaHoaDon().getValue();
            response.hopDongId = hoaDon.getHopDongId();
            response.khachHangId = hoaDon.getKhachHangId();
            response.tenKhachHang = hoaDon.getKhachHangId() != null
                    ? khachHangRepo.findByIdIncludingDeleted(hoaDon.getKhachHangId())
                    .map(KhachHang::getTenKhachHang)
                    .orElse(null)
                    : null;
            response.tongTien = hoaDon.getTongTien();
            response.soTienDaThu = hoaDon.getSoTienDaThu();
            response.trangThaiThanhToan = hoaDon.getTrangThaiThanhToan().name();
            response.createdAt = hoaDon.getCreatedAt();
            response.updatedAt = hoaDon.getUpdatedAt();
            return response;
        }

        public Long getId() {
            return id;
        }

        public String getMaHoaDon() {
            return maHoaDon;
        }

        public Long getHopDongId() {
            return hopDongId;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public String getTenKhachHang() {
            return tenKhachHang;
        }

        public BigDecimal getTongTien() {
            return tongTien;
        }

        public BigDecimal getSoTienDaThu() {
            return soTienDaThu;
        }

        public String getTrangThaiThanhToan() {
            return trangThaiThanhToan;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
