package com.crm.application.lead.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.Lead;


public class ChangeLeadStatusCommand implements IRequest<Lead> {

    private Long id;
    
    private String tinhTrangMoi;

    public ChangeLeadStatusCommand() {}

    public ChangeLeadStatusCommand(Long id, String tinhTrangMoi) {
        this.id          = id;
        this.tinhTrangMoi = tinhTrangMoi;
    }

    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }
    public String getTinhTrangMoi()  { return tinhTrangMoi; }
}