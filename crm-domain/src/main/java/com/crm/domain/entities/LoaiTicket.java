package com.crm.domain.entities;

public class LoaiTicket {

    private Short id;
    private String tenLoai;
    private String moTa;
    private Boolean isActive;

    public LoaiTicket() {}

    public LoaiTicket(Short id, String tenLoai, String moTa, Boolean isActive) {
        if (tenLoai == null || tenLoai.isBlank()) {
            throw new IllegalArgumentException("TenLoai khong duoc de trong");
        }
        this.id = id;
        this.tenLoai = tenLoai.trim();
        this.moTa = moTa;
        this.isActive = isActive != null ? isActive : true;
    }

    public void capNhat(String tenLoai, String moTa, Boolean isActive) {
        if (tenLoai != null && !tenLoai.isBlank()) {
            this.tenLoai = tenLoai.trim();
        }
        if (moTa != null) {
            this.moTa = moTa;
        }
        if (isActive != null) {
            this.isActive = isActive;
        }
    }

    public Short getId() { return id; }
    public String getTenLoai() { return tenLoai; }
    public String getMoTa() { return moTa; }
    public Boolean getIsActive() { return isActive; }
}
