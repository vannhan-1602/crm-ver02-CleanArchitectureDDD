package com.crm.application.lead.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.lead.query.GetAllLeadsQuery;
import com.crm.domain.entities.Lead;
import com.crm.domain.repositories.LeadRepo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GetAllLeadsQueryHandler implements IRequestHandler<GetAllLeadsQuery, List<Lead>> {

    private final LeadRepo leadRepo;

    public GetAllLeadsQueryHandler(LeadRepo leadRepo) {
        this.leadRepo = leadRepo;
    }

    @Override
    public List<Lead> handle(GetAllLeadsQuery query) {
        return leadRepo.findAll();
    }
}