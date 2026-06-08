package com.crm.presentation.controllers;

import com.crm.application.hopdong.command.CreateHopDongCommand;
import com.crm.application.hopdong.command.DeleteHopDongCommand;
import com.crm.application.hopdong.command.UpdateHopDongCommand;
import com.crm.application.hopdong.handler.CreateHopDongHandler;
import com.crm.application.hopdong.handler.DeleteHopDongHandler;
import com.crm.application.hopdong.handler.GetAllHopDongQueryHandler;
import com.crm.application.hopdong.handler.GetHopDongByIdQueryHandler;
import com.crm.application.hopdong.handler.UpdateHopDongHandler;
import com.crm.application.hopdong.query.GetAllHopDongQuery;
import com.crm.application.hopdong.query.GetHopDongByIdQuery;
import com.crm.domain.entities.HopDong;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/hop-dong")
public class HopDongController {
    private final CreateHopDongHandler createHopDongHandler;
    private final UpdateHopDongHandler updateHopDongHandler;
    private final DeleteHopDongHandler deleteHopDongHandler;
    private final GetHopDongByIdQueryHandler getHopDongByIdQueryHandler;
    private final GetAllHopDongQueryHandler getAllHopDongQueryHandler;

    public HopDongController(CreateHopDongHandler createHopDongHandler,
                              UpdateHopDongHandler updateHopDongHandler,
                              DeleteHopDongHandler deleteHopDongHandler,
                              GetHopDongByIdQueryHandler getHopDongByIdQueryHandler,
                              GetAllHopDongQueryHandler getAllHopDongQueryHandler) {
        this.createHopDongHandler = createHopDongHandler;
        this.updateHopDongHandler = updateHopDongHandler;
        this.deleteHopDongHandler = deleteHopDongHandler;
        this.getHopDongByIdQueryHandler = getHopDongByIdQueryHandler;
        this.getAllHopDongQueryHandler = getAllHopDongQueryHandler;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HopDongResponse create(@RequestBody CreateHopDongRequest request) {
        HopDong hopDong = createHopDongHandler.handle(new CreateHopDongCommand(
                request.getMaHopDong(),
                request.getKhachHangId(),
                request.getNgayKy(),
                request.getThoiHan(),
                request.getTrangThai()
        ));
        return HopDongResponse.from(hopDong);
    }

    @GetMapping
    public List<HopDongResponse> getAll() {
        return getAllHopDongQueryHandler.handle(new GetAllHopDongQuery())
                .stream()
                .map(HopDongResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public HopDongResponse getById(@PathVariable Long id) {
        return getHopDongByIdQueryHandler.handle(new GetHopDongByIdQuery(id))
                .map(HopDongResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Hợp đồng không tồn tại: " + id));
    }

    @PutMapping("/{id}")
    public HopDongResponse update(@PathVariable Long id, @RequestBody UpdateHopDongRequest request) {
        HopDong hopDong = updateHopDongHandler.handle(id, new UpdateHopDongCommand(
                request.getMaHopDong(),
                request.getKhachHangId(),
                request.getNgayKy(),
                request.getThoiHan(),
                request.getTrangThai()
        ));
        return HopDongResponse.from(hopDong);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteHopDongHandler.handle(new DeleteHopDongCommand(id));
    }

    static class CreateHopDongRequest {
        private String maHopDong;
        private Long khachHangId;
        private java.time.LocalDate ngayKy;
        private Integer thoiHan;
        private String trangThai;

        public String getMaHopDong() {
            return maHopDong;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public java.time.LocalDate getNgayKy() {
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
        private java.time.LocalDate ngayKy;
        private Integer thoiHan;
        private String trangThai;

        public String getMaHopDong() {
            return maHopDong;
        }

        public Long getKhachHangId() {
            return khachHangId;
        }

        public java.time.LocalDate getNgayKy() {
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
        private java.time.LocalDate ngayKy;
        private Integer thoiHan;
        private String trangThai;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;

        public static HopDongResponse from(HopDong hopDong) {
            HopDongResponse response = new HopDongResponse();
            response.id = hopDong.getId();
            response.maHopDong = hopDong.getMaHopDong().getValue();
            response.khachHangId = hopDong.getKhachHangId();
            response.ngayKy = hopDong.getNgayKy();
            response.thoiHan = hopDong.getThoiHan();
            response.trangThai = hopDong.getTrangThai().name();
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

        public java.time.LocalDate getNgayKy() {
            return ngayKy;
        }

        public Integer getThoiHan() {
            return thoiHan;
        }

        public String getTrangThai() {
            return trangThai;
        }

        public java.time.LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public java.time.LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
