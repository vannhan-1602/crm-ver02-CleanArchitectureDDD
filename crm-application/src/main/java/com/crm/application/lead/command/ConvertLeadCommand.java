package com.crm.application.lead.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.KhachHang;


public class ConvertLeadCommand implements IRequest<KhachHang> {


    private Long leadId;

    public ConvertLeadCommand() {}

    public ConvertLeadCommand(Long leadId) {
        this.leadId = leadId;
    }

    public Long getLeadId() { return leadId; }
}