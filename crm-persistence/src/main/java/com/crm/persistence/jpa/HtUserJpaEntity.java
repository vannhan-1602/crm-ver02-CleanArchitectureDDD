package com.crm.persistence.jpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HT_User")
public class HtUserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NhanSu_Id", insertable = false, updatable = false)
    private ThongTinNhanSuJpaEntity thongTinNhanSu;

    @Column(name = "NhanSu_Id")
    private Integer nhanSuId;

    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Role_Id")
    private Integer roleId;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    public HtUserJpaEntity() {}

    public Integer getId()                              { return id; }
    public Integer getNhanSuId()                        { return nhanSuId; }
    public ThongTinNhanSuJpaEntity getThongTinNhanSu()  { return thongTinNhanSu; }
    public String getUsername()                         { return username; }
    public String getPassword()                         { return password; }
    public Integer getRoleId()                          { return roleId; }
    public String getTrangThai()                        { return trangThai; }
    public LocalDateTime getCreatedAt()                 { return createdAt; }
    public LocalDateTime getUpdatedAt()                 { return updatedAt; }

    public void setId(Integer id)                       { this.id = id; }
    public void setNhanSuId(Integer nhanSuId)           { this.nhanSuId = nhanSuId; }
    public void setUsername(String username)            { this.username = username; }
    public void setPassword(String password)            { this.password = password; }
    public void setRoleId(Integer roleId)               { this.roleId = roleId; }
    public void setTrangThai(String trangThai)          { this.trangThai = trangThai; }
    public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)   { this.updatedAt = updatedAt; }
}
