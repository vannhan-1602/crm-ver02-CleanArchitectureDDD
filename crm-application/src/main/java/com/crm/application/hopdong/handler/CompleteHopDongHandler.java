package com.crm.application.hopdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hopdong.command.CompleteHopDongCommand;
import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CompleteHopDongHandler implements IRequestHandler<CompleteHopDongCommand, HopDong> {
    private final HopDongRepo hopDongRepo;

    public CompleteHopDongHandler(HopDongRepo hopDongRepo) {
        this.hopDongRepo = hopDongRepo;
    }

    @Override
    public HopDong handle(CompleteHopDongCommand command) {
        HopDong existing = hopDongRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Hop dong khong ton tai: " + command.getId()));
        existing.complete();
        return hopDongRepo.save(existing);
    }
}
