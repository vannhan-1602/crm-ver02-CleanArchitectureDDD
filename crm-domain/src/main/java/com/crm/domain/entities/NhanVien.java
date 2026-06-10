package com.crm.domain.entities;

public class NhanVien {

    private Integer id;
    private String  hoTen;

    public NhanVien() {}

    public NhanVien(Integer id, String hoTen) {
        this.id    = id;
        this.hoTen = hoTen;
    }

    public Integer getId()    { return id; }
    public String  getHoTen() { return hoTen; }
}
 