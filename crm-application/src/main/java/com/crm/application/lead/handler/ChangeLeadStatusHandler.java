package com.crm.application.lead.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.lead.command.ChangeLeadStatusCommand;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.LeadRepo;
import com.crm.domain.valueobjects.TinhTrangLead;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class ChangeLeadStatusHandler implements IRequestHandler<ChangeLeadStatusCommand, Lead> {

    private final LeadRepo leadRepo;

    public ChangeLeadStatusHandler(LeadRepo leadRepo) {
        this.leadRepo = leadRepo;
    }

    @Override
    public Lead handle(ChangeLeadStatusCommand command) {
        Lead lead = leadRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Lead khong ton tai: " + command.getId()));

        TinhTrangLead trangThaiMoi = TinhTrangLead.from(command.getTinhTrangMoi());

        
        lead.chuyenTrangThai(trangThaiMoi);

        return leadRepo.save(lead);
    }
}