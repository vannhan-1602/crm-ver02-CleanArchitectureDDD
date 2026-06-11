package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.khachhang.command.CreateKhachHangCommand;
import com.crm.application.khachhang.command.DeleteKhachHangCommand;
import com.crm.application.khachhang.command.UpdateKhachHangCommand;
import com.crm.application.khachhang.query.GetAllKhachHangQuery;
import com.crm.application.khachhang.query.GetKhachHangByIdQuery;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.NhanVienRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/khach-hang")
public class KhachHangController {

    private final Mediator mediator;
    private final NhanVienRepository nhanVienRepository;

    public KhachHangController(Mediator mediator,
                               NhanVienRepository nhanVienRepository) {
        this.mediator            = mediator;
        this.nhanVienRepository  = nhanVienRepository;
    }

   
    @GetMapping
    public List<KhachHangResponse> getAll(
            @RequestParam(required = false) Integer loai) {
        List<KhachHang> list = mediator.send(new GetAllKhachHangQuery(loai));
        return list.stream()
                .map(kh -> KhachHangResponse.from(kh, nhanVienRepository))
                .toList();
    }

    @GetMapping("/{id}")
    public KhachHangResponse getById(@PathVariable Long id) {
        return KhachHangResponse.from(
                mediator.send(new GetKhachHangByIdQuery(id)),
                nhanVienRepository);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KhachHangResponse create(@RequestBody CreateKhachHangRequest request) {
        KhachHang kh = mediator.send(new CreateKhachHangCommand(
                request.getTenKhachHang(),
                request.getEmail(),
                request.getSoDienThoai(),
                request.getLoaiKhachHangId(),
                request.getTinhTrangId(),
                request.getMaSoThue(),
                request.getNhanVienPhuTrachId()
        ));
        return KhachHangResponse.from(kh, nhanVienRepository);
    }

    @PutMapping("/{id}")
    public KhachHangResponse update(@PathVariable Long id,
                                    @RequestBody UpdateKhachHangRequest request) {
        UpdateKhachHangCommand command = new UpdateKhachHangCommand(
                request.getTenKhachHang(),
                request.getEmail(),
                request.getSoDienThoai(),
                request.getLoaiKhachHangId(),
                request.getTinhTrangId(),
                request.getMaSoThue(),
                request.getNhanVienPhuTrachId()
        );
        command.setId(id);
        return KhachHangResponse.from(mediator.send(command), nhanVienRepository);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeleteKhachHangCommand(id));
    }

    static class CreateKhachHangRequest {
        private String tenKhachHang;
        private String email;
        private String soDienThoai;
        private Integer loaiKhachHangId;
        private Integer tinhTrangId;
        private String maSoThue;
        private Integer nhanVienPhuTrachId;

        public String getTenKhachHang()         { return tenKhachHang; }
        public String getEmail()                { return email; }
        public String getSoDienThoai()          { return soDienThoai; }
        public Integer getLoaiKhachHangId()     { return loaiKhachHangId; }
        public Integer getTinhTrangId()         { return tinhTrangId; }
        public String getMaSoThue()             { return maSoThue; }
        public Integer getNhanVienPhuTrachId()  { return nhanVienPhuTrachId; }
    }

    static class UpdateKhachHangRequest {
        private String tenKhachHang;
        private String email;
        private String soDienThoai;
        private Integer loaiKhachHangId;
        private Integer tinhTrangId;
        private String maSoThue;
        private Integer nhanVienPhuTrachId;

        public String getTenKhachHang()         { return tenKhachHang; }
        public String getEmail()                { return email; }
        public String getSoDienThoai()          { return soDienThoai; }
        public Integer getLoaiKhachHangId()     { return loaiKhachHangId; }
        public Integer getTinhTrangId()         { return tinhTrangId; }
        public String getMaSoThue()             { return maSoThue; }
        public Integer getNhanVienPhuTrachId()  { return nhanVienPhuTrachId; }
    }

    static class KhachHangResponse {
        private Long id;
        private String maKhachHang;
        private String tenKhachHang;
        private String email;
        private String soDienThoai;
        private Integer loaiKhachHangId;
        private Integer tinhTrangId;
        private String maSoThue;
        private Integer nhanVienPhuTrachId;
        private String tenNhanVienPhuTrach;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static KhachHangResponse from(KhachHang kh,
                                             NhanVienRepository nhanVienRepository) {
            KhachHangResponse r = new KhachHangResponse();
            r.id                   = kh.getId();
            r.maKhachHang          = kh.getMaKhachHang();
            r.tenKhachHang         = kh.getTenKhachHang();
            r.email                = kh.getEmail();
            r.soDienThoai          = kh.getSoDienThoai();
            r.loaiKhachHangId      = kh.getLoaiKhachHangId();
            r.tinhTrangId          = kh.getTinhTrangId();
            r.maSoThue             = kh.getMaSoThue();
            r.nhanVienPhuTrachId   = kh.getNhanVienPhuTrachId();
            r.tenNhanVienPhuTrach  = kh.getNhanVienPhuTrachId() != null
                    ? nhanVienRepository.findHoTenById(kh.getNhanVienPhuTrachId()).orElse(null)
                    : null;
            r.createdAt            = kh.getCreatedAt();
            r.updatedAt            = kh.getUpdatedAt();
            return r;
        }

        public Long getId()                     { return id; }
        public String getMaKhachHang()          { return maKhachHang; }
        public String getTenKhachHang()         { return tenKhachHang; }
        public String getEmail()                { return email; }
        public String getSoDienThoai()          { return soDienThoai; }
        public Integer getLoaiKhachHangId()     { return loaiKhachHangId; }
        public Integer getTinhTrangId()         { return tinhTrangId; }
        public String getMaSoThue()             { return maSoThue; }
        public Integer getNhanVienPhuTrachId()  { return nhanVienPhuTrachId; }
        public String getTenNhanVienPhuTrach()  { return tenNhanVienPhuTrach; }
        public LocalDateTime getCreatedAt()     { return createdAt; }
        public LocalDateTime getUpdatedAt()     { return updatedAt; }
    }
}