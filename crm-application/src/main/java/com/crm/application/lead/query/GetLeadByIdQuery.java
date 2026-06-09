package com.crm.application.lead.query;

import com.crm.application.common.IRequest;
import com.crm.domain.entities.Lead;


public class GetLeadByIdQuery implements IRequest<Lead> {

    private Long id;

    public GetLeadByIdQuery() {}

    public GetLeadByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}