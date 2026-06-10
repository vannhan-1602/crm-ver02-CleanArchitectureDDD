package com.crm.presentation.controllers;

import com.crm.application.baogia.command.BaoGiaChiTietCommand;
import com.crm.application.baogia.command.CreateBaoGiaCommand;
import com.crm.application.baogia.command.DeleteBaoGiaCommand;
import com.crm.application.baogia.command.UpdateBaoGiaCommand;
import com.crm.application.baogia.query.GetAllBaoGiaQuery;
import com.crm.application.baogia.query.GetBaoGiaByIdQuery;
import com.crm.application.common.Mediator;
import com.crm.domain.entities.BaoGia;
import com.crm.domain.entities.BaoGiaChiTiet;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/bao-gia")
public class BaoGiaController {
    private final Mediator mediator;

    public BaoGiaController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaoGiaResponse create(@RequestBody BaoGiaRequest request) {
        return BaoGiaResponse.from(mediator.send(new CreateBaoGiaCommand(
                request.getMaBaoGia(),
                request.getKhachHangId(),
                request.getNhanVienId(),
                request.getTrangThai(),
                request.getChiTiets()
        )));
    }

    @GetMapping
    public List<BaoGiaResponse> getAll() {
        return mediator.send(new GetAllBaoGiaQuery())
                .stream()
                .map(BaoGiaResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public BaoGiaResponse getById(@PathVariable Long id) {
        return BaoGiaResponse.from(mediator.send(new GetBaoGiaByIdQuery(id)));
    }

    @PutMapping("/{id}")
    public BaoGiaResponse update(@PathVariable Long id, @RequestBody BaoGiaRequest request) {
        UpdateBaoGiaCommand command = new UpdateBaoGiaCommand(
                request.getMaBaoGia(),
                request.getKhachHangId(),
                request.getNhanVienId(),
                request.getTrangThai(),
                request.getChiTiets()
        );
        command.setId(id);
        return BaoGiaResponse.from(mediator.send(command));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeleteBaoGiaCommand(id));
    }

    static class BaoGiaRequest {
        private String maBaoGia;
        private Long khachHangId;
        private Integer nhanVienId;
        private String trangThai;
        private List<BaoGiaChiTietCommand> chiTiets;

        public String getMaBaoGia() {
            return maBaoGia;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public Integer getNhanVienId() {
            return nhanVienId;
        }

        public String getTrangThai() {
            return trangThai;
        }

        public List<BaoGiaChiTietCommand> getChiTiets() {
            return chiTiets;
        }
    }

    static class BaoGiaResponse {
        private Long id;
        private String maBaoGia;
        private Long khachHangId;
        private Integer nhanVienId;
        private Double tongTien;
        private String trangThai;
        private List<ChiTietResponse> chiTiets;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static BaoGiaResponse from(BaoGia baoGia) {
            BaoGiaResponse response = new BaoGiaResponse();
            response.id = baoGia.getId();
            response.maBaoGia = baoGia.getMaBaoGia();
            response.khachHangId = baoGia.getKhachHangId();
            response.nhanVienId = baoGia.getNhanVienId();
            response.tongTien = baoGia.getTongTien();
            response.trangThai = baoGia.getTrangThai() != null ? baoGia.getTrangThai().name() : null;
            response.chiTiets = baoGia.getChiTiets().stream()
                    .map(ChiTietResponse::from)
                    .toList();
            response.createdAt = baoGia.getCreatedAt();
            response.updatedAt = baoGia.getUpdatedAt();
            return response;
        }

        public Long getId() {
            return id;
        }

        public String getMaBaoGia() {
            return maBaoGia;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public Integer getNhanVienId() {
            return nhanVienId;
        }

        public Double getTongTien() {
            return tongTien;
        }

        public String getTrangThai() {
            return trangThai;
        }

        public List<ChiTietResponse> getChiTiets() {
            return chiTiets;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }

    static class ChiTietResponse {
        private Long id;
        private Integer sanPhamId;
        private Integer soLuong;
        private Double donGia;

        public static ChiTietResponse from(BaoGiaChiTiet chiTiet) {
            ChiTietResponse response = new ChiTietResponse();
            response.id = chiTiet.getId();
            response.sanPhamId = chiTiet.getSanPhamId();
            response.soLuong = chiTiet.getSoLuong();
            response.donGia = chiTiet.getDonGia();
            return response;
        }

        public Long getId() {
            return id;
        }

        public Integer getSanPhamId() {
            return sanPhamId;
        }

        public Integer getSoLuong() {
            return soLuong;
        }

        public Double getDonGia() {
            return donGia;
        }
    }
}
