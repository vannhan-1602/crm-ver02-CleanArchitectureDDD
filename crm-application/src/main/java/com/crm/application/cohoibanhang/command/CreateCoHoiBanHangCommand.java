    package com.crm.application.cohoibanhang.command;

    import com.crm.application.common.IRequest;
    import com.crm.domain.entities.CoHoiBanHang;

    import java.util.Date;

    public class CreateCoHoiBanHangCommand implements IRequest<CoHoiBanHang> {

        private String tenThuongVu;
        private String giaiDoan;
        private Integer khachHangId;
        private Integer leadId;
        private int tyLeThanhCong;
        private double doanhThuKyVong;
        private String ghiChu;
        private Date ngayDuKien;
        private Integer nhanVienPhuTrachId;

        public CreateCoHoiBanHangCommand() {}

        public CreateCoHoiBanHangCommand(String tenThuongVu, String giaiDoan,
                                         Integer khachHangId, Integer leadId,
                                         int tyLeThanhCong, double doanhThuKyVong,
                                         String ghiChu, Date ngayDuKien,
                                         Integer nhanVienPhuTrachId) {
            this.tenThuongVu       = tenThuongVu;
            this.giaiDoan          = giaiDoan;
            this.khachHangId       = khachHangId;
            this.leadId            = leadId;
            this.tyLeThanhCong     = tyLeThanhCong;
            this.doanhThuKyVong    = doanhThuKyVong;
            this.ghiChu            = ghiChu;
            this.ngayDuKien        = ngayDuKien;
            this.nhanVienPhuTrachId = nhanVienPhuTrachId;
        }

        public String getTenThuongVu()           { return tenThuongVu; }
        public String getGiaiDoan()              { return giaiDoan; }
        public Integer getKhachHangId()          { return khachHangId; }
        public Integer getLeadId()               { return leadId; }
        public int getTyLeThanhCong()            { return tyLeThanhCong; }
        public double getDoanhThuKyVong()        { return doanhThuKyVong; }
        public String getGhiChu()               { return ghiChu; }
        public Date getNgayDuKien()             { return ngayDuKien; }
        public Integer getNhanVienPhuTrachId()  { return nhanVienPhuTrachId; }

        public void setTenThuongVu(String tenThuongVu) {
            this.tenThuongVu = tenThuongVu;
        }

        public void setGiaiDoan(String giaiDoan) {
            this.giaiDoan = giaiDoan;
        }

        public void setKhachHangId(Integer khachHangId) {
            this.khachHangId = khachHangId;
        }

        public void setLeadId(Integer leadId) {
            this.leadId = leadId;
        }

        public void setTyLeThanhCong(int tyLeThanhCong) {
            this.tyLeThanhCong = tyLeThanhCong;
        }

        public void setDoanhThuKyVong(double doanhThuKyVong) {
            this.doanhThuKyVong = doanhThuKyVong;
        }

        public void setGhiChu(String ghiChu) {
            this.ghiChu = ghiChu;
        }

        public void setNgayDuKien(Date ngayDuKien) {
            this.ngayDuKien = ngayDuKien;
        }

        public void setNhanVienPhuTrachId(Integer nhanVienPhuTrachId) {
            this.nhanVienPhuTrachId = nhanVienPhuTrachId;
        }
    }