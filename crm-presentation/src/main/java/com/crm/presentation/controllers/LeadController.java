package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.lead.command.*;
import com.crm.application.lead.query.GetAllLeadsQuery;
import com.crm.application.lead.query.GetLeadByIdQuery;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.NhanVienRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/leads")
public class LeadController {

    private final Mediator mediator;
    private final NhanVienRepository nhanVienRepository;

    public LeadController(Mediator mediator,
                          NhanVienRepository nhanVienRepository) {
        this.mediator = mediator;
        this.nhanVienRepository = nhanVienRepository;
    }


    @GetMapping
    public List<LeadResponse> getAll() {
        List<Lead> leads = mediator.send(new GetAllLeadsQuery());
        return leads.stream()
                .map(lead -> LeadResponse.from(lead, nhanVienRepository))
                .toList();
    }


    @GetMapping("/{id}")
    public LeadResponse getById(@PathVariable Long id) {
        return LeadResponse.from(mediator.send(new GetLeadByIdQuery(id)), nhanVienRepository);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeadResponse create(@RequestBody CreateLeadRequest request) {
        Lead lead = mediator.send(new CreateLeadCommand(
                request.getTenLead(),
                request.getTenCongTy(),
                request.getSoDienThoai(),
                request.getEmail(),
                request.getNhanVienPhuTrachId()
        ));
        return LeadResponse.from(lead, nhanVienRepository);
    }


    @PutMapping("/{id}")
    public LeadResponse update(@PathVariable Long id,
                               @RequestBody UpdateLeadRequest request) {
        UpdateLeadCommand command = new UpdateLeadCommand(
                request.getTenLead(),
                request.getTenCongTy(),
                request.getSoDienThoai(),
                request.getEmail(),
                request.getNhanVienPhuTrachId()
        );
        command.setId(id);
        return LeadResponse.from(mediator.send(command), nhanVienRepository);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediator.send(new DeleteLeadCommand(id));
    }


    @PatchMapping("/{id}/status")
    public LeadResponse changeStatus(@PathVariable Long id,
                                     @RequestBody ChangeStatusRequest request) {
        ChangeLeadStatusCommand command = new ChangeLeadStatusCommand(id, request.getTinhTrangMoi());
        return LeadResponse.from(mediator.send(command), nhanVienRepository);
    }


    @PostMapping("/{id}/convert")
    @ResponseStatus(HttpStatus.CREATED)
    public KhachHangResponse convert(@PathVariable Long id) {
        KhachHang khachHang = mediator.send(new ConvertLeadCommand(id));
        return KhachHangResponse.from(khachHang);
    }




    static class CreateLeadRequest {
        private String tenLead;
        private String tenCongTy;
        private String soDienThoai;
        private String email;
        private Integer nhanVienPhuTrachId;

        public String getTenLead()             { return tenLead; }
        public String getTenCongTy()           { return tenCongTy; }
        public String getSoDienThoai()         { return soDienThoai; }
        public String getEmail()               { return email; }
        public Integer getNhanVienPhuTrachId() { return nhanVienPhuTrachId; }
    }

    static class UpdateLeadRequest {
        private String tenLead;
        private String tenCongTy;
        private String soDienThoai;
        private String email;
        private Integer nhanVienPhuTrachId;

        public String getTenLead()             { return tenLead; }
        public String getTenCongTy()           { return tenCongTy; }
        public String getSoDienThoai()         { return soDienThoai; }
        public String getEmail()               { return email; }
        public Integer getNhanVienPhuTrachId() { return nhanVienPhuTrachId; }
    }

    static class ChangeStatusRequest {
        private String tinhTrangMoi;
        public String getTinhTrangMoi() { return tinhTrangMoi; }
    }



    static class LeadResponse {
        private Long id;
        private String tenLead;
        private String tenCongTy;
        private String soDienThoai;
        private String email;
        private String tinhTrang;
        private Integer nhanVienPhuTrachId;
        private String tenNhanVienPhuTrach;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static LeadResponse from(Lead lead, NhanVienRepository nhanVienRepository) {
            LeadResponse r = new LeadResponse();
            r.id                   = lead.getId();
            r.tenLead              = lead.getTenLead();
            r.tenCongTy            = lead.getTenCongTy();
            r.soDienThoai          = lead.getSoDienThoai();
            r.email                = lead.getEmail();
            r.tinhTrang            = lead.getTinhTrang() != null
                    ? lead.getTinhTrang().name() : null;
            r.nhanVienPhuTrachId   = lead.getNhanVienPhuTrachId();
            r.tenNhanVienPhuTrach  = lead.getNhanVienPhuTrachId() != null
                    ? nhanVienRepository.findHoTenById(lead.getNhanVienPhuTrachId()).orElse(null)
                    : null;
            r.createdAt            = lead.getCreatedAt();
            r.updatedAt            = lead.getUpdatedAt();
            return r;
        }

        public Long getId()                     { return id; }
        public String getTenLead()              { return tenLead; }
        public String getTenCongTy()            { return tenCongTy; }
        public String getSoDienThoai()          { return soDienThoai; }
        public String getEmail()                { return email; }
        public String getTinhTrang()            { return tinhTrang; }
        public Integer getNhanVienPhuTrachId()  { return nhanVienPhuTrachId; }
        public String getTenNhanVienPhuTrach()  { return tenNhanVienPhuTrach; } // ← THÊM MỚI
        public LocalDateTime getCreatedAt()     { return createdAt; }
        public LocalDateTime getUpdatedAt()     { return updatedAt; }
    }

    static class KhachHangResponse {
        private Long id;
        private String maKhachHang;
        private String tenKhachHang;
        private String email;
        private String soDienThoai;
        private Integer nhanVienPhuTrachId;
        private LocalDateTime createdAt;

        public static KhachHangResponse from(KhachHang kh) {
            KhachHangResponse r = new KhachHangResponse();
            r.id                 = kh.getId();
            r.maKhachHang        = kh.getMaKhachHang();
            r.tenKhachHang       = kh.getTenKhachHang();
            r.email              = kh.getEmail();
            r.soDienThoai        = kh.getSoDienThoai();
            r.nhanVienPhuTrachId = kh.getNhanVienPhuTrachId();
            r.createdAt          = kh.getCreatedAt();
            return r;
        }

        public Long getId()                    { return id; }
        public String getMaKhachHang()         { return maKhachHang; }
        public String getTenKhachHang()        { return tenKhachHang; }
        public String getEmail()               { return email; }
        public String getSoDienThoai()         { return soDienThoai; }
        public Integer getNhanVienPhuTrachId() { return nhanVienPhuTrachId; }
        public LocalDateTime getCreatedAt()    { return createdAt; }
    }
}