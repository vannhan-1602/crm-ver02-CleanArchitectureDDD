package com.crm.application.ticketphanhoi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticketphanhoi.command.CreateTicketPhanHoiCommand;
import com.crm.domain.entities.Ticket;
import com.crm.domain.entities.TicketPhanHoi;
import com.crm.domain.repositories.TicketPhanHoiRepo;
import com.crm.domain.repositories.TicketRepo;
import com.crm.domain.valueobjects.LoaiPhanHoiTicket;
import com.crm.domain.valueobjects.TrangThaiTicket;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CreateTicketPhanHoiHandler implements IRequestHandler<CreateTicketPhanHoiCommand, TicketPhanHoi> {
    private final TicketPhanHoiRepo phanHoiRepo;
    private final TicketRepo ticketRepo;

    public CreateTicketPhanHoiHandler(TicketPhanHoiRepo phanHoiRepo, TicketRepo ticketRepo) {
        this.phanHoiRepo = phanHoiRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public TicketPhanHoi handle(CreateTicketPhanHoiCommand command) {
        Ticket ticket = ticketRepo.findById(command.getTicketId())
                .orElseThrow(() -> new NoSuchElementException("Ticket khong ton tai: " + command.getTicketId()));
        TrangThaiTicket trangThaiSau = command.getTrangThaiSau() != null
                ? TrangThaiTicket.from(command.getTrangThaiSau())
                : null;
        TicketPhanHoi phanHoi = new TicketPhanHoi(
                command.getTicketId(),
                command.getNguoiPhanHoiId(),
                LoaiPhanHoiTicket.from(command.getLoaiPhanHoi()),
                command.getNoiDung(),
                command.getFileDinhKem(),
                ticket.getTrangThai(),
                trangThaiSau
        );
        TicketPhanHoi saved = phanHoiRepo.save(phanHoi);
        if (trangThaiSau != null) {
            ticket.doiTrangThai(trangThaiSau, null, command.getNoiDung());
            ticketRepo.save(ticket);
        }
        return saved;
    }
}
