package com.crm.application.hoatdong.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.HoatDong;

import java.util.List;

public class GetAllHoatDongQuery implements IRequest<List<HoatDong>> {

    private Long khachHangId;
    private Long leadId;

    public GetAllHoatDongQuery() {}

    public GetAllHoatDongQuery(Long khachHangId, Long leadId) {
        this.khachHangId = khachHangId;
        this.leadId      = leadId;
    }

    public Long getKhachHangId() { return khachHangId; }
    public Long getLeadId()      { return leadId; }
}