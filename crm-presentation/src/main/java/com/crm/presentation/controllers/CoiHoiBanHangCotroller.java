package com.crm.presentation.controllers;

import com.crm.application.cohoibanhang.command.CreateCoHoiBanHangCommand;
import com.crm.application.cohoibanhang.command.DeleteCoHoiBanHangCommand;
import com.crm.application.cohoibanhang.command.UpdateCoHoiBanHangCommand;
import com.crm.application.cohoibanhang.query.GetAllCoHoiQuery;
import com.crm.application.cohoibanhang.query.GetCoHoiBanHangByIdQuery;
import com.crm.application.common.Mediator;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.KhachHangRepo;
import com.crm.domain.repositories.LeadRepo;
import com.crm.domain.repositories.NhanVienRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/cohoi")
public class CoiHoiBanHangCotroller {

    private final Mediator mediator;
    private final NhanVienRepository nvrepo;
    private final LeadRepo leadrepo;
    private final KhachHangRepo khrepo;

    public CoiHoiBanHangCotroller(Mediator mediator,
                                  NhanVienRepository nvrepo,
                                  LeadRepo leadrepo,
                                  KhachHangRepo khrepo) {
        this.mediator  = mediator;
        this.nvrepo    = nvrepo;
        this.leadrepo  = leadrepo;
        this.khrepo    = khrepo;
    }

    // ─── Helper: map CoHoiBanHang → CoHoiBanHangResponse ────────────────────
    private CoHoiBanHangResponse toResponse(CoHoiBanHang c) {
        CoHoiBanHangResponse res = new CoHoiBanHangResponse();
        res.setId(c.getId());
        res.setTenThuongVu(c.getTenThuongVu());
        res.setGiaiDoan(c.getGiaiDoan());
        res.setTyLeThanhCong(c.getTyLeThanhCong());
        res.setDoanhThuKyVong(c.getDoanhThuKyVong());
        res.setGhiChu(c.getGhiChu());
        res.setNgayDuKien(c.getNgayDuKien());
        res.setCreatedAt(c.getCreatedAt());
        res.setUpdatedAt(c.getUpdatedAt());

        // Resolve tên KhachHang
        res.setKhachHangId(c.getKhachHang_Id());
        res.setTenKhachHang(
                khrepo.findTenById(c.getKhachHang_Id()).orElse(null)  // hoặc orElse("")
        );

        // Resolve tên Lead
        res.setLeadId(c.getLead_Id());
        res.setTenLead(
                leadrepo.findTenById(c.getLead_Id()).orElse("Không xác định")
        );

        // Resolve tên NhanVien
        res.setNhanVienPhuTrachId(c.getNhanVienPhuTrach_Id());
        res.setTenNhanVienPhuTrach(
                nvrepo.findHoTenById(c.getNhanVienPhuTrach_Id()).orElse("Không xác định")
        );

        return res;
    }

    // ─── Endpoints ───────────────────────────────────────────────────────────

    @GetMapping
    public List<CoHoiBanHangResponse> GetAll() {
        return mediator.<List<CoHoiBanHang>>send(new GetAllCoHoiQuery())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CoHoiBanHangResponse GetById(@PathVariable Integer id) {
        CoHoiBanHang c = mediator.send(new GetCoHoiBanHangByIdQuery(id));
        return toResponse(c);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoHoiBanHangResponse Create(@RequestBody CreateCoHoiBanHangCommand command) {
        CoHoiBanHang c = mediator.send(command);
        return toResponse(c);
    }

    @PutMapping("/{id}")
    public CoHoiBanHangResponse Update(@PathVariable Integer id,
                                       @RequestBody UpdateCoHoiBanHangCommand command) {
        command.setId(id);
        CoHoiBanHang c = mediator.send(command);
        return toResponse(c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean delete(@PathVariable Integer id) {
        return mediator.send(new DeleteCoHoiBanHangCommand(id));
    }

    // ─── DTO ─────────────────────────────────────────────────────────────────

    static class CoHoiBanHangResponse {
        private Integer id;
        private String tenThuongVu;
        private String giaiDoan;
        private Integer khachHangId;
        private String tenKhachHang;
        private Integer leadId;
        private String tenLead;
        private Integer nhanVienPhuTrachId;
        private String tenNhanVienPhuTrach;
        private int tyLeThanhCong;
        private double doanhThuKyVong;
        private String ghiChu;
        private Date ngayDuKien;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CoHoiBanHangResponse() {}

        public Integer getId()                         { return id; }
        public void setId(Integer id)                  { this.id = id; }
        public String getTenThuongVu()                 { return tenThuongVu; }
        public void setTenThuongVu(String v)           { this.tenThuongVu = v; }
        public String getGiaiDoan()                    { return giaiDoan; }
        public void setGiaiDoan(String v)              { this.giaiDoan = v; }
        public Integer getKhachHangId()                { return khachHangId; }
        public void setKhachHangId(Integer v)          { this.khachHangId = v; }
        public String getTenKhachHang()                { return tenKhachHang; }
        public void setTenKhachHang(String v)          { this.tenKhachHang = v; }
        public Integer getLeadId()                     { return leadId; }
        public void setLeadId(Integer v)               { this.leadId = v; }
        public String getTenLead()                     { return tenLead; }
        public void setTenLead(String v)               { this.tenLead = v; }
        public Integer getNhanVienPhuTrachId()         { return nhanVienPhuTrachId; }
        public void setNhanVienPhuTrachId(Integer v)   { this.nhanVienPhuTrachId = v; }
        public String getTenNhanVienPhuTrach()         { return tenNhanVienPhuTrach; }
        public void setTenNhanVienPhuTrach(String v)   { this.tenNhanVienPhuTrach = v; }
        public int getTyLeThanhCong()                  { return tyLeThanhCong; }
        public void setTyLeThanhCong(int v)            { this.tyLeThanhCong = v; }
        public double getDoanhThuKyVong()              { return doanhThuKyVong; }
        public void setDoanhThuKyVong(double v)        { this.doanhThuKyVong = v; }
        public String getGhiChu()                      { return ghiChu; }
        public void setGhiChu(String v)                { this.ghiChu = v; }
        public Date getNgayDuKien()                    { return ngayDuKien; }
        public void setNgayDuKien(Date v)              { this.ngayDuKien = v; }
        public LocalDateTime getCreatedAt()            { return createdAt; }
        public void setCreatedAt(LocalDateTime v)      { this.createdAt = v; }
        public LocalDateTime getUpdatedAt()            { return updatedAt; }
        public void setUpdatedAt(LocalDateTime v)      { this.updatedAt = v; }
    }
}