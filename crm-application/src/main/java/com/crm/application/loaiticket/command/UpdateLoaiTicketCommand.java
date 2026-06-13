package com.crm.application.loaiticket.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiTicket;

public class UpdateLoaiTicketCommand implements IRequest<LoaiTicket> {
    private Short id;
    private String tenLoai;
    private String moTa;
    private Boolean isActive;

    public Short getId() { return id; }
    public void setId(Short id) { this.id = id; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}
