package com.crm.application.loaiticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaiticket.query.GetLoaiTicketByIdQuery;
import com.crm.domain.entities.LoaiTicket;
import com.crm.domain.repositories.LoaiTicketRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GetLoaiTicketByIdQueryHandler implements IRequestHandler<GetLoaiTicketByIdQuery, LoaiTicket> {
    private final LoaiTicketRepo repo;

    public GetLoaiTicketByIdQueryHandler(LoaiTicketRepo repo) { this.repo = repo; }

    @Override
    public LoaiTicket handle(GetLoaiTicketByIdQuery query) {
        return repo.findById(query.getId())
                .orElseThrow(() -> new NoSuchElementException("LoaiTicket khong ton tai: " + query.getId()));
    }
}
