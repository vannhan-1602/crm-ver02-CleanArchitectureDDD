package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.hopdong.command.CreateHopDongCommand;
import com.crm.application.hopdong.command.CompleteHopDongCommand;
import com.crm.application.hopdong.command.DeleteHopDongCommand;
import com.crm.application.hopdong.command.UpdateHopDongCommand;
import com.crm.application.hopdong.query.GetAllHopDongQuery;
import com.crm.application.hopdong.query.GetHopDongByIdQuery;
import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.KhachHangRepo;
import com.crm.domain.entities.KhachHang;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/hop-dong")
public class HopDongController {
    private final Mediator mediator;
    private final KhachHangRepo khachHangRepo;

    public HopDongController(Mediator mediator, KhachHangRepo khachHangRepo) {
        this.mediator = mediator;
        this.khachHangRepo = khachHangRepo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HopDongResponse create(@RequestBody CreateHopDongRequest request) {
        HopDong hopDong = mediator.send(new CreateHopDongCommand(
                request.getMaHopDong(),
                request.getKhachHangId(),
                request.getNgayKy(),
                request.getThoiHan(),
                request.getTrangThai()
        ));
        return HopDongResponse.from(hopDong, khachHangRepo);
    }

    @GetMapping
    public List<HopDongResponse> getAll() {
        List<HopDong> hopDongs = mediator.send(new GetAllHopDongQuery());
        return hopDongs.stream()
                .map(hopDong -> HopDongResponse.from(hopDong, khachHangRepo))
                .toList();
    }

    @GetMapping("/{id}")
    public HopDongResponse getById(@PathVariable Long id) {
        return HopDongResponse.from(mediator.send(new GetHopDongByIdQuery(id)), khachHangRepo);
    }

    @PutMapping("/{id}")
    public HopDongResponse update(@PathVariable Long id, @RequestBody UpdateHopDongRequest request) {
        UpdateHopDongCommand command = new UpdateHopDongCommand(
                request.getMaHopDong(),
                request.getKhachHangId(),
                request.getNgayKy(),
                request.getThoiHan(),
                request.getTrangThai()
        );
        command.setId(id);
        HopDong hopDong = mediator.send(command);
        return HopDongResponse.from(hopDong, khachHangRepo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeleteHopDongCommand(id));
    }

    @PatchMapping("/{id}/complete")
    public HopDongResponse complete(@PathVariable Long id) {
        HopDong hopDong = mediator.send(new CompleteHopDongCommand(id));
        return HopDongResponse.from(hopDong, khachHangRepo);
    }

    static class CreateHopDongRequest {
        private String maHopDong;
        private Long khachHangId;
        private LocalDate ngayKy;
        private Integer thoiHan;
        private String trangThai;

        public String getMaHopDong() {
            return maHopDong;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public LocalDate getNgayKy() {
            return ngayKy;
        }

        public Integer getThoiHan() {
            return thoiHan;
        }

        public String getTrangThai() {
            return trangThai;
        }
    }

    static class UpdateHopDongRequest {
        private String maHopDong;
        private Long khachHangId;
        private LocalDate ngayKy;
        private Integer thoiHan;
        private String trangThai;

        public String getMaHopDong() {
            return maHopDong;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public LocalDate getNgayKy() {
            return ngayKy;
        }

        public Integer getThoiHan() {
            return thoiHan;
        }

        public String getTrangThai() {
            return trangThai;
        }
    }

    static class HopDongResponse {
        private Long id;
        private String maHopDong;
        private Long khachHangId;
        private String tenKhachHang;
        private LocalDate ngayKy;
        private Integer thoiHan;
        private String trangThai;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static HopDongResponse from(HopDong hopDong, KhachHangRepo khachHangRepo) {
            HopDongResponse response = new HopDongResponse();
            response.id = hopDong.getId();
            response.maHopDong = hopDong.getMaHopDong() != null ? hopDong.getMaHopDong().getValue() : null;
            response.khachHangId = hopDong.getKhachHangId();
            response.tenKhachHang = hopDong.getKhachHangId() != null
                    ? khachHangRepo.findByIdIncludingDeleted(hopDong.getKhachHangId())
                    .map(KhachHang::getTenKhachHang)
                    .orElse(null)
                    : null;
            response.ngayKy = hopDong.getNgayKy();
            response.thoiHan = hopDong.getThoiHan();
            response.trangThai = hopDong.getTrangThai() != null ? hopDong.getTrangThai().name() : null;
            response.createdAt = hopDong.getCreatedAt();
            response.updatedAt = hopDong.getUpdatedAt();
            return response;
        }

        public Long getId() {
            return id;
        }

        public String getMaHopDong() {
            return maHopDong;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public String getTenKhachHang() {
            return tenKhachHang;
        }

        public LocalDate getNgayKy() {
            return ngayKy;
        }

        public Integer getThoiHan() {
            return thoiHan;
        }

        public String getTrangThai() {
            return trangThai;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
