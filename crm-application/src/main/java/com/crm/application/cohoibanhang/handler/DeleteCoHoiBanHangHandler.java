package com.crm.application.cohoibanhang.handler;

import com.crm.application.cohoibanhang.command.DeleteCoHoiBanHangCommand;
import com.crm.application.common.IRequestHandler;
import com.crm.domain.entities.CoHoiBanHang;
import com.crm.domain.repositories.CoiHoiBanHangRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeleteCoHoiBanHangHandler
        implements IRequestHandler<DeleteCoHoiBanHangCommand, Boolean> {

    private final CoiHoiBanHangRepo repo;

    public DeleteCoHoiBanHangHandler(CoiHoiBanHangRepo repo) {
        this.repo = repo;
    }

    @Override
    public Boolean handle(DeleteCoHoiBanHangCommand command) {
        CoHoiBanHang existing = repo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Co hoi ban hang khong ton tai: " + command.getId()));
        existing.xoa();
        repo.save(existing);   // soft-delete: lưu lại với isDeleted = 1
        return true;
    }
}