package com.crm.application.ticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticket.command.DeleteTicketCommand;
import com.crm.domain.entities.Ticket;
import com.crm.domain.repositories.TicketRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeleteTicketHandler implements IRequestHandler<DeleteTicketCommand, Boolean> {
    private final TicketRepo repo;

    public DeleteTicketHandler(TicketRepo repo) { this.repo = repo; }

    @Override
    public Boolean handle(DeleteTicketCommand command) {
        Ticket ticket = repo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Ticket khong ton tai: " + command.getId()));
        ticket.xoaMem();
        repo.save(ticket);
        return true;
    }
}
