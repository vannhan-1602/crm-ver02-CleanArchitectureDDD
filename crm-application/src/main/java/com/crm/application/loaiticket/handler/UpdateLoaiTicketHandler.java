package com.crm.application.loaiticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.loaiticket.command.UpdateLoaiTicketCommand;
import com.crm.domain.entities.LoaiTicket;
import com.crm.domain.repositories.LoaiTicketRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateLoaiTicketHandler implements IRequestHandler<UpdateLoaiTicketCommand, LoaiTicket> {
    private final LoaiTicketRepo repo;

    public UpdateLoaiTicketHandler(LoaiTicketRepo repo) { this.repo = repo; }

    @Override
    public LoaiTicket handle(UpdateLoaiTicketCommand command) {
        LoaiTicket loaiTicket = repo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("LoaiTicket khong ton tai: " + command.getId()));
        loaiTicket.capNhat(command.getTenLoai(), command.getMoTa(), command.getIsActive());
        return repo.save(loaiTicket);
    }
}
