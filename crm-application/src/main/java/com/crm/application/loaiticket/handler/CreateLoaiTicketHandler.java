package com.crm.application.loaiticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaiticket.command.CreateLoaiTicketCommand;
import com.crm.domain.entities.LoaiTicket;
import com.crm.domain.repositories.LoaiTicketRepo;
import org.springframework.stereotype.Service;

@Service
public class CreateLoaiTicketHandler implements IRequestHandler<CreateLoaiTicketCommand, LoaiTicket> {
    private final LoaiTicketRepo repo;

    public CreateLoaiTicketHandler(LoaiTicketRepo repo) { this.repo = repo; }

    @Override
    public LoaiTicket handle(CreateLoaiTicketCommand command) {
        return repo.save(new LoaiTicket(null, command.getTenLoai(), command.getMoTa(), command.getIsActive()));
    }
}
