package com.crm.application.phieuchi.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.phieuchi.command.DeletePhieuChiCommand;
import com.crm.domain.repositories.PhieuChiRepo;
import org.springframework.stereotype.Service;

@Service
public class DeletePhieuChiHandler implements IRequestHandler<DeletePhieuChiCommand, Boolean> {
    private final PhieuChiRepo phieuChiRepo;

    public DeletePhieuChiHandler(PhieuChiRepo phieuChiRepo) {
        this.phieuChiRepo = phieuChiRepo;
    }

    @Override
    public Boolean handle(DeletePhieuChiCommand command) {
        phieuChiRepo.deleteById(command.getId());
        return true;
    }
}
