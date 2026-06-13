package com.crm.application.ticketphanhoi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticketphanhoi.command.DeleteTicketPhanHoiCommand;
import com.crm.domain.repositories.TicketPhanHoiRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeleteTicketPhanHoiHandler implements IRequestHandler<DeleteTicketPhanHoiCommand, Boolean> {
    private final TicketPhanHoiRepo repo;

    public DeleteTicketPhanHoiHandler(TicketPhanHoiRepo repo) { this.repo = repo; }

    @Override
    public Boolean handle(DeleteTicketPhanHoiCommand command) {
        repo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("TicketPhanHoi khong ton tai: " + command.getId()));
        repo.deleteById(command.getId());
        return true;
    }
}
