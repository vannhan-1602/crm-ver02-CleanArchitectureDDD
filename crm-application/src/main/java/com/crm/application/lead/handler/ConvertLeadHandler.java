package com.crm.application.lead.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.lead.command.ConvertLeadCommand;
import com.crm.domain.entities.KhachHang;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.KhachHangRepo;
import com.crm.domain.repositories.LeadRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ConvertLeadHandler implements IRequestHandler<ConvertLeadCommand, KhachHang> {

    private final LeadRepo leadRepo;
    private final KhachHangRepo khachHangRepo;

    public ConvertLeadHandler(LeadRepo leadRepo, KhachHangRepo khachHangRepo) {
        this.leadRepo      = leadRepo;
        this.khachHangRepo = khachHangRepo;
    }

    @Override
    public KhachHang handle(ConvertLeadCommand command) {

        Lead lead = leadRepo.findById(command.getLeadId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Lead khong ton tai: " + command.getLeadId()));


        lead.danhDauDaConvert();


        String maMoi = generateMaKhachHang();


        KhachHang khachHang = new KhachHang(
                maMoi,
                lead.getTenLead(),
                lead.getEmail(),
                lead.getSoDienThoai(),
                lead.getNhanVienPhuTrachId()
        );

        KhachHang savedKhachHang = khachHangRepo.save(khachHang);
        leadRepo.save(lead);
        return savedKhachHang;
    }


    private String generateMaKhachHang() {
        Optional<String> maxMa = khachHangRepo.findMaxMaKhachHang();

        if (maxMa.isEmpty()) {
            return "KH0001";
        }

        String current = maxMa.get(); // ví dụ: "KH0004"
        try {
            // Bỏ prefix "KH", parse số
            int soHienTai = Integer.parseInt(current.replaceAll("[^0-9]", ""));
            return String.format("KH%04d", soHienTai + 1);
        } catch (NumberFormatException e) {

            return "KH" + System.currentTimeMillis();
        }
    }
}