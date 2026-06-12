package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.khachhang.command.CreateKhachHangCommand;
import com.crm.application.khachhang.command.DeleteKhachHangCommand;
import com.crm.application.khachhang.command.UpdateKhachHangCommand;
import com.crm.application.khachhang.query.GetAllKhachHangQuery;
import com.crm.application.khachhang.query.GetKhachHangByIdQuery;
import com.crm.domain.entities.DiaChi;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.repositories.DiaChiRepo;
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
    private final DiaChiRepo diaChiRepo;

    public KhachHangController(Mediator mediator,
                               NhanVienRepository nhanVienRepository,
                               DiaChiRepo diaChiRepo) {
        this.mediator           = mediator;
        this.nhanVienRepository = nhanVienRepository;
        this.diaChiRepo         = diaChiRepo;
    }


    @GetMapping
    public List<KhachHangResponse> getAll(
            @RequestParam(required = false) Integer loai) {
        List<KhachHang> list = mediator.send(new GetAllKhachHangQuery(loai));
        return list.stream()
                .map(kh -> KhachHangResponse.from(kh, nhanVienRepository, diaChiRepo))
                .toList();
    }

    @GetMapping("/{id}")
    public KhachHangResponse getById(@PathVariable Long id) {
        return KhachHangResponse.from(
                mediator.send(new GetKhachHangByIdQuery(id)),
                nhanVienRepository, diaChiRepo);
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
        return KhachHangResponse.from(kh, nhanVienRepository, diaChiRepo);
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
        return KhachHangResponse.from(mediator.send(command), nhanVienRepository, diaChiRepo);
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
        private List<DiaChiResponse> diaChiList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static KhachHangResponse from(KhachHang kh,
                                             NhanVienRepository nhanVienRepository,
                                             DiaChiRepo diaChiRepo) {
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
            r.diaChiList           = diaChiRepo.findByKhachHangId(kh.getId())
                    .stream()
                    .map(DiaChiResponse::from)
                    .toList();
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
        public List<DiaChiResponse> getDiaChiList() { return diaChiList; }
        public LocalDateTime getCreatedAt()     { return createdAt; }
        public LocalDateTime getUpdatedAt()     { return updatedAt; }
    }

    static class DiaChiResponse {
        private Long id;
        private String diaChiChiTiet;
        private String tinhThanh;
        private String quanHuyen;
        private String phuongXa;
        private String loaiDiaChi;
        private boolean isDefault;

        public static DiaChiResponse from(DiaChi dc) {
            DiaChiResponse r = new DiaChiResponse();
            r.id             = dc.getId();
            r.diaChiChiTiet  = dc.getDiaChiChiTiet();
            r.tinhThanh      = dc.getTinhThanh();
            r.quanHuyen      = dc.getQuanHuyen();
            r.phuongXa       = dc.getPhuongXa();
            r.loaiDiaChi     = dc.getLoaiDiaChi();
            r.isDefault      = dc.isDefault();
            return r;
        }

        public Long getId()              { return id; }
        public String getDiaChiChiTiet() { return diaChiChiTiet; }
        public String getTinhThanh()     { return tinhThanh; }
        public String getQuanHuyen()     { return quanHuyen; }
        public String getPhuongXa()      { return phuongXa; }
        public String getLoaiDiaChi()    { return loaiDiaChi; }
        public boolean isDefault()       { return isDefault; }
    }
}
