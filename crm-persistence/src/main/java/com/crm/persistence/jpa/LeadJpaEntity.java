package com.crm.persistence.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "KH_Lead")
public class LeadJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "TenLead", nullable = false, length = 150)
    private String tenLead;

    @Column(name = "TenCongTy", length = 255)
    private String tenCongTy;

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "Email", length = 150)
    private String email;


    @Column(name = "TinhTrang", length = 50)
    private String tinhTrang;

    @Column(name = "NhanVienPhuTrach_Id")
    private Integer nhanVienPhuTrachId;


    @Column(name = "IsDeleted")
    private boolean isDeleted;

    @Column(name = "CreatedAt", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public LeadJpaEntity() {}

   

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getTenLead()                   { return tenLead; }
    public void setTenLead(String tenLead)       { this.tenLead = tenLead; }

    public String getTenCongTy()                 { return tenCongTy; }
    public void setTenCongTy(String tenCongTy)   { this.tenCongTy = tenCongTy; }

    public String getSoDienThoai()               { return soDienThoai; }
    public void setSoDienThoai(String v)         { this.soDienThoai = v; }

    public String getEmail()                     { return email; }
    public void setEmail(String email)           { this.email = email; }

    public String getTinhTrang()                 { return tinhTrang; }
    public void setTinhTrang(String tinhTrang)   { this.tinhTrang = tinhTrang; }

    public Integer getNhanVienPhuTrachId()             { return nhanVienPhuTrachId; }
    public void setNhanVienPhuTrachId(Integer v)       { this.nhanVienPhuTrachId = v; }

    public boolean isDeleted()                   { return isDeleted; }
    public void setDeleted(boolean deleted)      { isDeleted = deleted; }

    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime v)    { this.createdAt = v; }

    public LocalDateTime getUpdatedAt()          { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v)    { this.updatedAt = v; }
}