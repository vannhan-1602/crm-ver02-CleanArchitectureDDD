package com.crm.application.baogia.handler;

import com.crm.application.baogia.command.DeleteBaoGiaCommand;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.repositories.BaoGiaRepo;
import org.springframework.stereotype.Service;

@Service
public class DeleteBaoGiaHandler implements IRequestHandler<DeleteBaoGiaCommand, Void> {
    private final BaoGiaRepo baoGiaRepo;

    public DeleteBaoGiaHandler(BaoGiaRepo baoGiaRepo) {
        this.baoGiaRepo = baoGiaRepo;
    }

    @Override
    public Void handle(DeleteBaoGiaCommand request) {
        baoGiaRepo.deleteById(request.getId());
        return null;
    }
}
