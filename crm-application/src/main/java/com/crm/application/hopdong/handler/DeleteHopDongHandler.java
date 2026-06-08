package com.crm.application.hopdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hopdong.command.DeleteHopDongCommand;
import com.crm.domain.repositories.HopDongRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteHopDongHandler implements IRequestHandler<DeleteHopDongCommand, Boolean> {
    private final HopDongRepo hopDongRepo;

    public DeleteHopDongHandler(HopDongRepo hopDongRepo) {
        this.hopDongRepo = hopDongRepo;
    }

    @Override
    public Boolean handle(DeleteHopDongCommand command) {
        hopDongRepo.deleteById(command.getId());
        return true;
    }
}
