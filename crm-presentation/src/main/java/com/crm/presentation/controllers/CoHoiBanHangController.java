package com.crm.presentation.controllers;

import com.crm.application.cohoibanhang.command.CreateCoHoiBanHangCommand;
import com.crm.application.cohoibanhang.command.DeleteCoHoiBanHangCommand;
import com.crm.application.cohoibanhang.command.UpdateCoHoiBanHangCommand;
import com.crm.application.cohoibanhang.query.GetAllCoHoiQuery;
import com.crm.application.cohoibanhang.query.GetCoHoiBanHangByIdQuery;
import com.crm.application.common.Mediator;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.NhanVienRepository;
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
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/cohoi")
public class CoHoiBanHangController {

    private final Mediator mediator;
    private final NhanVienRepository nhanVienRepository;

    public CoHoiBanHangController(Mediator mediator,
                                  NhanVienRepository nhanVienRepository) {
        this.mediator = mediator;
        this.nhanVienRepository = nhanVienRepository;
    }

    @GetMapping
    public List<CoHoiBanHangResponse> getAll() {
        List<CoHoiBanHang> coHois = mediator.send(new GetAllCoHoiQuery());
        return coHois.stream()
                .map(coHoi -> CoHoiBanHangResponse.from(coHoi, nhanVienRepository))
                .toList();
    }

    @GetMapping("/{id}")
    public CoHoiBanHangResponse getById(@PathVariable Integer id) {
        return CoHoiBanHangResponse.from(
                mediator.send(new GetCoHoiBanHangByIdQuery(id)),
                nhanVienRepository
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoHoiBanHangResponse create(@RequestBody CoHoiBanHangRequest request) {
        CoHoiBanHang coHoi = mediator.send(new CreateCoHoiBanHangCommand(
                request.getTenThuongVu(),
                request.getGiaiDoan(),
                request.getKhachHangId(),
                request.getLeadId(),
                request.getTyLeThanhCong(),
                request.getDoanhThuKyVong(),
                request.getGhiChu(),
                request.getNgayDuKien(),
                request.getNhanVienPhuTrachId()
        ));
        return CoHoiBanHangResponse.from(coHoi, nhanVienRepository);
    }

    @PutMapping("/{id}")
    public CoHoiBanHangResponse update(@PathVariable Integer id,
                                       @RequestBody CoHoiBanHangRequest request) {
        UpdateCoHoiBanHangCommand command = new UpdateCoHoiBanHangCommand(
                request.getTenThuongVu(),
                request.getGiaiDoan(),
                request.getKhachHangId(),
                request.getLeadId(),
                request.getTyLeThanhCong(),
                request.getDoanhThuKyVong(),
                request.getGhiChu(),
                request.getNgayDuKien(),
                request.getNhanVienPhuTrachId()
        );
        command.setId(id);
        return CoHoiBanHangResponse.from(mediator.send(command), nhanVienRepository);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        mediator.send(new DeleteCoHoiBanHangCommand(id));
    }

    static class CoHoiBanHangRequest {
        private String tenThuongVu;
        private String giaiDoan;
        private Integer khachHangId;
        private Integer khachHang_Id;
        private Integer leadId;
        private Integer lead_Id;
        private Integer tyLeThanhCong;
        private Double doanhThuKyVong;
        private String ghiChu;
        private Date ngayDuKien;
        private Integer nhanVienPhuTrachId;
        private Integer nhanVienPhuTrach_Id;

        public String getTenThuongVu() {
            return tenThuongVu;
        }

        public void setTenThuongVu(String tenThuongVu) {
            this.tenThuongVu = tenThuongVu;
        }

        public String getGiaiDoan() {
            return giaiDoan;
        }

        public void setGiaiDoan(String giaiDoan) {
            this.giaiDoan = giaiDoan;
        }

        public Integer getKhachHangId() {
            return khachHangId != null ? khachHangId : khachHang_Id;
        }

        public void setKhachHangId(Integer khachHangId) {
            this.khachHangId = khachHangId;
        }

        public void setKhachHang_Id(Integer khachHang_Id) {
            this.khachHang_Id = khachHang_Id;
        }

        public Integer getLeadId() {
            return leadId != null ? leadId : lead_Id;
        }

        public void setLeadId(Integer leadId) {
            this.leadId = leadId;
        }

        public void setLead_Id(Integer lead_Id) {
            this.lead_Id = lead_Id;
        }

        public int getTyLeThanhCong() {
            return tyLeThanhCong != null ? tyLeThanhCong : 0;
        }

        public void setTyLeThanhCong(Integer tyLeThanhCong) {
            this.tyLeThanhCong = tyLeThanhCong;
        }

        public double getDoanhThuKyVong() {
            return doanhThuKyVong != null ? doanhThuKyVong : 0;
        }

        public void setDoanhThuKyVong(Double doanhThuKyVong) {
            this.doanhThuKyVong = doanhThuKyVong;
        }

        public String getGhiChu() {
            return ghiChu;
        }

        public void setGhiChu(String ghiChu) {
            this.ghiChu = ghiChu;
        }

        public Date getNgayDuKien() {
            return ngayDuKien;
        }

        public void setNgayDuKien(Date ngayDuKien) {
            this.ngayDuKien = ngayDuKien;
        }

        public Integer getNhanVienPhuTrachId() {
            return nhanVienPhuTrachId != null ? nhanVienPhuTrachId : nhanVienPhuTrach_Id;
        }

        public void setNhanVienPhuTrachId(Integer nhanVienPhuTrachId) {
            this.nhanVienPhuTrachId = nhanVienPhuTrachId;
        }

        public void setNhanVienPhuTrach_Id(Integer nhanVienPhuTrach_Id) {
            this.nhanVienPhuTrach_Id = nhanVienPhuTrach_Id;
        }
    }

    static class CoHoiBanHangResponse {
        private Integer id;
        private String tenThuongVu;
        private String giaiDoan;
        private Integer khachHangId;
        private Integer khachHang_Id;
        private Integer leadId;
        private Integer lead_Id;
        private int tyLeThanhCong;
        private double doanhThuKyVong;
        private String ghiChu;
        private Date ngayDuKien;
        private Integer nhanVienPhuTrachId;
        private Integer nhanVienPhuTrach_Id;
        private String tenNhanVienPhuTrach;
        private int isDeleted;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static CoHoiBanHangResponse from(CoHoiBanHang coHoi,
                                                NhanVienRepository nhanVienRepository) {
            CoHoiBanHangResponse response = new CoHoiBanHangResponse();
            response.id = coHoi.getId();
            response.tenThuongVu = coHoi.getTenThuongVu();
            response.giaiDoan = coHoi.getGiaiDoan();
            response.khachHangId = coHoi.getKhachHang_Id();
            response.khachHang_Id = coHoi.getKhachHang_Id();
            response.leadId = coHoi.getLead_Id();
            response.lead_Id = coHoi.getLead_Id();
            response.tyLeThanhCong = coHoi.getTyLeThanhCong();
            response.doanhThuKyVong = coHoi.getDoanhThuKyVong();
            response.ghiChu = coHoi.getGhiChu();
            response.ngayDuKien = coHoi.getNgayDuKien();
            response.nhanVienPhuTrachId = coHoi.getNhanVienPhuTrach_Id();
            response.nhanVienPhuTrach_Id = coHoi.getNhanVienPhuTrach_Id();
            response.tenNhanVienPhuTrach = coHoi.getNhanVienPhuTrach_Id() != null
                    ? nhanVienRepository.findHoTenById(coHoi.getNhanVienPhuTrach_Id()).orElse(null)
                    : null;
            response.isDeleted = coHoi.getIsDeleted();
            response.createdAt = coHoi.getCreatedAt();
            response.updatedAt = coHoi.getUpdatedAt();
            return response;
        }

        public Integer getId() {
            return id;
        }

        public String getTenThuongVu() {
            return tenThuongVu;
        }

        public String getGiaiDoan() {
            return giaiDoan;
        }

        public Integer getKhachHangId() {
            return khachHangId;
        }

        public Integer getKhachHang_Id() {
            return khachHang_Id;
        }

        public Integer getLeadId() {
            return leadId;
        }

        public Integer getLead_Id() {
            return lead_Id;
        }

        public int getTyLeThanhCong() {
            return tyLeThanhCong;
        }

        public double getDoanhThuKyVong() {
            return doanhThuKyVong;
        }

        public String getGhiChu() {
            return ghiChu;
        }

        public Date getNgayDuKien() {
            return ngayDuKien;
        }

        public Integer getNhanVienPhuTrachId() {
            return nhanVienPhuTrachId;
        }

        public Integer getNhanVienPhuTrach_Id() {
            return nhanVienPhuTrach_Id;
        }

        public String getTenNhanVienPhuTrach() {
            return tenNhanVienPhuTrach;
        }

        public int getIsDeleted() {
            return isDeleted;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
