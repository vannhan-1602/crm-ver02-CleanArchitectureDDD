package com.crm.application.lead.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.lead.command.UpdateLeadCommand;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.LeadRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class UpdateLeadHandler implements IRequestHandler<UpdateLeadCommand, Lead> {

    private final LeadRepo leadRepo;

    public UpdateLeadHandler(LeadRepo leadRepo) {
        this.leadRepo = leadRepo;
    }

    @Override
    public Lead handle(UpdateLeadCommand command) {
        Lead lead = leadRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Lead khong ton tai: " + command.getId()));

        lead.updateThongTin(
                command.getTenLead(),
                command.getTenCongTy(),
                command.getSoDienThoai(),
                command.getEmail(),
                command.getNhanVienPhuTrachId()
        );

        return leadRepo.save(lead);
    }
}