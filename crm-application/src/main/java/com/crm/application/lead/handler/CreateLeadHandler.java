package com.crm.application.lead.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.lead.command.CreateLeadCommand;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.LeadRepo;
import org.springframework.stereotype.Service;


@Service
public class CreateLeadHandler implements IRequestHandler<CreateLeadCommand, Lead> {

    private final LeadRepo leadRepo;

    public CreateLeadHandler(LeadRepo leadRepo) {
        this.leadRepo = leadRepo;
    }

    @Override
    public Lead handle(CreateLeadCommand command) {
        Lead lead = new Lead(
                command.getTenLead(),
                command.getTenCongTy(),
                command.getSoDienThoai(),
                command.getEmail(),
                command.getNhanVienPhuTrachId()
        );
        return leadRepo.save(lead);
    }
}