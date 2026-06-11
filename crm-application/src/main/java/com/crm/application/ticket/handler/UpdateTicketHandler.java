package com.crm.application.ticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticket.command.UpdateTicketCommand;
import com.crm.domain.entities.Ticket;
import com.crm.domain.repositories.TicketRepo;
import com.crm.domain.valueobjects.MucDoUuTienTicket;
import com.crm.domain.valueobjects.NguonTiepNhanTicket;
import com.crm.domain.valueobjects.TrangThaiTicket;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateTicketHandler implements IRequestHandler<UpdateTicketCommand, Ticket> {
    private final TicketRepo repo;

    public UpdateTicketHandler(TicketRepo repo) { this.repo = repo; }

    @Override
    public Ticket handle(UpdateTicketCommand command) {
        Ticket ticket = repo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Ticket khong ton tai: " + command.getId()));
        ticket.capNhat(
                command.getTieuDe(),
                command.getMoTa(),
                command.getFileDinhKem(),
                command.getLoaiTicketId(),
                command.getHopDongId(),
                command.getSanPhamId(),
                command.getMucDoUuTien() != null ? MucDoUuTienTicket.from(command.getMucDoUuTien()) : null,
                command.getNguonTiepNhan() != null ? NguonTiepNhanTicket.from(command.getNguonTiepNhan()) : null,
                command.getTrangThai() != null ? TrangThaiTicket.from(command.getTrangThai()) : null,
                command.getNhanVienTiepNhanId(),
                command.getNhanVienXuLyId(),
                command.getNgayHenXuLy(),
                command.getNgayDong(),
                command.getLyDoDong()
        );
        return repo.save(ticket);
    }
}
