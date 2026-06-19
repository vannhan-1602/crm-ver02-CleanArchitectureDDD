package com.crm.api.security;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
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
        pathToModule.put("/api/phieu-thu", "PHIEU_THU");
        pathToModule.put("/api/phieu-chi", "PHIEU_CHI");
        pathToModule.put("/api/tai-chinh", "TAI_CHINH");
        pathToModule.put("/api/bao-cao-thong-ke", "BAO_CAO");
        pathToModule.put("/api/sanpham", "SAN_PHAM");
        pathToModule.put("/api/loaisanpham", "LOAI_SAN_PHAM");
        pathToModule.put("/api/loai-ticket", "LOAI_TICKET");
        pathToModule.put("/api/tickets", "TICKET");
        pathToModule.put("/api/ticket-phan-hoi", "TICKET_PHAN_HOI");
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
}
