package com.crm.application.lead.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.lead.command.DeleteLeadCommand;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.LeadRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class DeleteLeadHandler implements IRequestHandler<DeleteLeadCommand, Boolean> {

    private final LeadRepo leadRepo;

    public DeleteLeadHandler(LeadRepo leadRepo) {
        this.leadRepo = leadRepo;
    }

    @Override
    public Boolean handle(DeleteLeadCommand command) {
        Lead lead = leadRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Lead khong ton tai: " + command.getId()));


        lead.xoa();


        leadRepo.softDeleteById(command.getId());
        return true;
    }
}