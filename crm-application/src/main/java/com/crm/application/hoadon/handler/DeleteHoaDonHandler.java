package com.crm.application.hoadon.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoadon.command.DeleteHoaDonCommand;
import com.crm.domain.repositories.HoaDonRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteHoaDonHandler implements IRequestHandler<DeleteHoaDonCommand, Boolean> {
    private final HoaDonRepo hoaDonRepo;

    public DeleteHoaDonHandler(HoaDonRepo hoaDonRepo) {
        this.hoaDonRepo = hoaDonRepo;
    }

    @Override
    public Boolean handle(DeleteHoaDonCommand command) {
        hoaDonRepo.deleteById(command.getId());
        return true;
    }
}
