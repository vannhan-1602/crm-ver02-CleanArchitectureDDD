package com.crm.application.ticket.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.ticket.command.CreateTicketCommand;
import com.crm.domain.entities.Ticket;
import com.crm.domain.repositories.TicketRepo;
import com.crm.domain.valueobjects.MucDoUuTienTicket;
import com.crm.domain.valueobjects.NguonTiepNhanTicket;
import com.crm.domain.valueobjects.TrangThaiTicket;
import org.springframework.stereotype.Service;

@Service
public class CreateTicketHandler implements IRequestHandler<CreateTicketCommand, Ticket> {
    private final TicketRepo repo;

    public CreateTicketHandler(TicketRepo repo) { this.repo = repo; }

    @Override
    public Ticket handle(CreateTicketCommand command) {
        Ticket ticket = new Ticket(
                command.getMaTicket(),
                command.getTieuDe(),
                command.getMoTa(),
                command.getFileDinhKem(),
                command.getLoaiTicketId(),
                command.getKhachHangId(),
                command.getHopDongId(),
                command.getSanPhamId(),
                MucDoUuTienTicket.from(command.getMucDoUuTien()),
                NguonTiepNhanTicket.from(command.getNguonTiepNhan()),
                TrangThaiTicket.from(command.getTrangThai()),
                command.getNhanVienTiepNhanId(),
                command.getNhanVienXuLyId(),
                command.getNgayHenXuLy(),
                command.getNgayDong(),
                command.getLyDoDong()
        );
        return repo.save(ticket);
    }
}
