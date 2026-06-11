package com.crm.application.loaiticket.command;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.LoaiTicket;

public class CreateLoaiTicketCommand implements IRequest<LoaiTicket> {
    private String tenLoai;
    private String moTa;
    private Boolean isActive;

    public CreateLoaiTicketCommand() {}

    public CreateLoaiTicketCommand(String tenLoai, String moTa, Boolean isActive) {
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.isActive = isActive;
    }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}
