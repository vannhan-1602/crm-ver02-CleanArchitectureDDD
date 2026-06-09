package com.crm.application.lead.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.lead.query.GetLeadByIdQuery;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.LeadRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class GetLeadByIdQueryHandler implements IRequestHandler<GetLeadByIdQuery, Lead> {

    private final LeadRepo leadRepo;

    public GetLeadByIdQueryHandler(LeadRepo leadRepo) {
        this.leadRepo = leadRepo;
    }

    @Override
    public Lead handle(GetLeadByIdQuery query) {
        return leadRepo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Lead khong ton tai: " + query.getId()));
    }
}