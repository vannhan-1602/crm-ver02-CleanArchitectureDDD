package com.crm.application.loaiticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaiticket.command.DeleteLoaiTicketCommand;
import com.crm.domain.repositories.LoaiTicketRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeleteLoaiTicketHandler implements IRequestHandler<DeleteLoaiTicketCommand, Boolean> {
    private final LoaiTicketRepo repo;

    public DeleteLoaiTicketHandler(LoaiTicketRepo repo) { this.repo = repo; }

    @Override
    public Boolean handle(DeleteLoaiTicketCommand command) {
        repo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("LoaiTicket khong ton tai: " + command.getId()));
        repo.deleteById(command.getId());
        return true;
    }
}
