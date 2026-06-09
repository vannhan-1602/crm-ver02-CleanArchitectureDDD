package com.crm.application.lead.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.Lead;

public class UpdateLeadCommand implements IRequest<Lead> {

    private Long id;
    private String tenLead;
    private String tenCongTy;
    private String soDienThoai;
    private String email;
    private Integer nhanVienPhuTrachId;

    public UpdateLeadCommand() {}

    public UpdateLeadCommand(String tenLead, String tenCongTy,
                             String soDienThoai, String email,
                             Integer nhanVienPhuTrachId) {
        this.tenLead             = tenLead;
        this.tenCongTy           = tenCongTy;
        this.soDienThoai         = soDienThoai;
        this.email               = email;
        this.nhanVienPhuTrachId  = nhanVienPhuTrachId;
    }

    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }
    public String getTenLead()               { return tenLead; }
    public String getTenCongTy()             { return tenCongTy; }
    public String getSoDienThoai()           { return soDienThoai; }
    public String getEmail()                 { return email; }
    public Integer getNhanVienPhuTrachId()   { return nhanVienPhuTrachId; }
}