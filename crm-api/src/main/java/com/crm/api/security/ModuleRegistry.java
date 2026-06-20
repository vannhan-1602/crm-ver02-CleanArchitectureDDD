package com.crm.api.security;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ModuleRegistry {
    private final Map<String, String> pathToModule = new LinkedHashMap<>();

    public ModuleRegistry() {
        pathToModule.put("/api/leads", "LEAD");
        pathToModule.put("/api/khach-hang", "KHACH_HANG");
        pathToModule.put("/api/cohoi", "CO_HOI");
        pathToModule.put("/api/hoat-dong", "HOAT_DONG");
        pathToModule.put("/api/bao-gia", "BAO_GIA");
        pathToModule.put("/api/hop-dong", "HOP_DONG");
        pathToModule.put("/api/hoa-don", "HOA_DON");
        pathToModule.put("/api/phieu-thu", "TAI_CHINH");
        pathToModule.put("/api/phieu-chi", "TAI_CHINH");
        pathToModule.put("/api/tai-chinh", "TAI_CHINH");
        pathToModule.put("/api/bao-cao-thong-ke", "BAO_CAO");
        pathToModule.put("/api/sanpham", "SAN_PHAM");
        pathToModule.put("/api/loaisanpham", "SAN_PHAM");
        pathToModule.put("/api/loai-ticket", "TICKET");
        pathToModule.put("/api/tickets", "TICKET");
        pathToModule.put("/api/ticket-phan-hoi", "TICKET");
        pathToModule.put("/api/nhan-vien", "NHAN_VIEN");
    }

    public Optional<String> findModule(String path) {
        return pathToModule.entrySet().stream()
                .filter(entry -> path.equals(entry.getKey()) || path.startsWith(entry.getKey() + "/"))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public Map<String, String> getPathToModule() {
        return pathToModule;
    }

    public List<ModuleInfo> getModules() {
        return List.of(
                new ModuleInfo("LEAD", "Quản lý Lead", "/api/leads"),
                new ModuleInfo("KHACH_HANG", "Quản lý Khách hàng", "/api/khach-hang"),
                new ModuleInfo("CO_HOI", "Quản lý Cơ hội bán hàng", "/api/cohoi"),
                new ModuleInfo("HOAT_DONG", "Quản lý Hoạt động", "/api/hoat-dong"),
                new ModuleInfo("BAO_GIA", "Quản lý Báo giá", "/api/bao-gia"),
                new ModuleInfo("HOP_DONG", "Quản lý Hợp đồng", "/api/hop-dong"),
                new ModuleInfo("HOA_DON", "Quản lý Hóa đơn", "/api/hoa-don"),
                new ModuleInfo("TAI_CHINH", "Quản lý Tài chính", "/api/tai-chinh"),
                new ModuleInfo("BAO_CAO", "Báo cáo thống kê", "/api/bao-cao-thong-ke"),
                new ModuleInfo("SAN_PHAM", "Quản lý Sản phẩm", "/api/sanpham"),
                new ModuleInfo("TICKET", "Quản lý Ticket", "/api/tickets"),
                new ModuleInfo("NHAN_VIEN", "Quản lý Nhân viên", "/api/nhan-vien")
        );
    }

    public record ModuleInfo(String moduleKey, String name, String path) {
    }
}
