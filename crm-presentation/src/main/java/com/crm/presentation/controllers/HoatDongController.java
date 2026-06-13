package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.hoatdong.command.CreateHoatDongCommand;
import com.crm.application.hoatdong.command.DeleteHoatDongCommand;
import com.crm.application.hoatdong.command.UpdateHoatDongCommand;
import com.crm.application.hoatdong.query.GetAllHoatDongQuery;
import com.crm.application.hoatdong.query.GetHoatDongByIdQuery;
import com.crm.domain.entities.HoatDong;
import com.crm.domain.repositories.NhanVienRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/hoat-dong")
public class HoatDongController {

    private final Mediator mediator;
    private final NhanVienRepository nhanVienRepository;

    public HoatDongController(Mediator mediator,
                              NhanVienRepository nhanVienRepository) {
        this.mediator           = mediator;
        this.nhanVienRepository = nhanVienRepository;
    }

   
    @GetMapping
    public List<HoatDongResponse> getAll(
            @RequestParam(required = false) Long khachHangId,
            @RequestParam(required = false) Long leadId) {
        List<HoatDong> list = mediator.send(new GetAllHoatDongQuery(khachHangId, leadId));
        return list.stream()
                .map(hd -> HoatDongResponse.from(hd, nhanVienRepository))
                .toList();
    }

    @GetMapping("/{id}")
    public HoatDongResponse getById(@PathVariable Long id) {
        return HoatDongResponse.from(
                mediator.send(new GetHoatDongByIdQuery(id)),
                nhanVienRepository);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HoatDongResponse create(@RequestBody CreateHoatDongRequest request) {
        HoatDong hd = mediator.send(new CreateHoatDongCommand(
                request.getKhachHangId(),
                request.getLeadId(),
                request.getLoaiHoatDong(),
                request.getNoiDung(),
                request.getThoiGianThucHien(),
                request.getNhanVienId()
        ));
        return HoatDongResponse.from(hd, nhanVienRepository);
    }

    @PutMapping("/{id}")
    public HoatDongResponse update(@PathVariable Long id,
                                   @RequestBody UpdateHoatDongRequest request) {
        UpdateHoatDongCommand command = new UpdateHoatDongCommand(
                request.getLoaiHoatDong(),
                request.getNoiDung(),
                request.getThoiGianThucHien(),
                request.getNhanVienId()
        );
        command.setId(id);
        return HoatDongResponse.from(mediator.send(command), nhanVienRepository);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeleteHoatDongCommand(id));
    }



    static class CreateHoatDongRequest {
        private Long khachHangId;
        private Long leadId;
        private String loaiHoatDong;
        private String noiDung;
        private LocalDateTime thoiGianThucHien;
        private Integer nhanVienId;

        public Long getKhachHangId()               { return khachHangId; }
        public Long getLeadId()                    { return leadId; }
        public String getLoaiHoatDong()            { return loaiHoatDong; }
        public String getNoiDung()                 { return noiDung; }
        public LocalDateTime getThoiGianThucHien() { return thoiGianThucHien; }
        public Integer getNhanVienId()             { return nhanVienId; }
    }

    static class UpdateHoatDongRequest {
        private String loaiHoatDong;
        private String noiDung;
        private LocalDateTime thoiGianThucHien;
        private Integer nhanVienId;

        public String getLoaiHoatDong()            { return loaiHoatDong; }
        public String getNoiDung()                 { return noiDung; }
        public LocalDateTime getThoiGianThucHien() { return thoiGianThucHien; }
        public Integer getNhanVienId()             { return nhanVienId; }
    }

    static class HoatDongResponse {
        private Long id;
        private Long khachHangId;
        private Long leadId;
        private String loaiHoatDong;
        private String noiDung;
        private LocalDateTime thoiGianThucHien;
        private Integer nhanVienId;
        private String tenNhanVien;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static HoatDongResponse from(HoatDong hd,
                                            NhanVienRepository nhanVienRepository) {
            HoatDongResponse r = new HoatDongResponse();
            r.id                 = hd.getId();
            r.khachHangId        = hd.getKhachHangId();
            r.leadId             = hd.getLeadId();
            r.loaiHoatDong       = hd.getLoaiHoatDong() != null
                    ? hd.getLoaiHoatDong().name() : null;
            r.noiDung            = hd.getNoiDung();
            r.thoiGianThucHien   = hd.getThoiGianThucHien();
            r.nhanVienId         = hd.getNhanVienId();
            r.tenNhanVien        = hd.getNhanVienId() != null
                    ? nhanVienRepository.findHoTenById(hd.getNhanVienId()).orElse(null)
                    : null;
            r.createdAt          = hd.getCreatedAt();
            r.updatedAt          = hd.getUpdatedAt();
            return r;
        }

        public Long getId()                           { return id; }
        public Long getKhachHangId()                  { return khachHangId; }
        public Long getLeadId()                       { return leadId; }
        public String getLoaiHoatDong()               { return loaiHoatDong; }
        public String getNoiDung()                    { return noiDung; }
        public LocalDateTime getThoiGianThucHien()    { return thoiGianThucHien; }
        public Integer getNhanVienId()                { return nhanVienId; }
        public String getTenNhanVien()                { return tenNhanVien; }
        public LocalDateTime getCreatedAt()           { return createdAt; }
        public LocalDateTime getUpdatedAt()           { return updatedAt; }
    }
}