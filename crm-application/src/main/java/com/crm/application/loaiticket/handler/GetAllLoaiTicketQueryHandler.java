package com.crm.application.loaiticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaiticket.query.GetAllLoaiTicketQuery;
import com.crm.domain.entities.LoaiTicket;
import com.crm.domain.repositories.LoaiTicketRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllLoaiTicketQueryHandler implements IRequestHandler<GetAllLoaiTicketQuery, List<LoaiTicket>> {
    private final LoaiTicketRepo repo;

    public GetAllLoaiTicketQueryHandler(LoaiTicketRepo repo) { this.repo = repo; }

    @Override
    public List<LoaiTicket> handle(GetAllLoaiTicketQuery query) { return repo.findAll(); }
}
