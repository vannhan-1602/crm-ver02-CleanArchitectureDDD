package com.crm.application.hoatdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoatdong.command.DeleteHoatDongCommand;
import com.crm.domain.repositories.HoatDongRepo;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class DeleteHoatDongHandler implements IRequestHandler<DeleteHoatDongCommand, Boolean> {

    private final HoatDongRepo hoatDongRepo;

    public DeleteHoatDongHandler(HoatDongRepo hoatDongRepo) {
        this.hoatDongRepo = hoatDongRepo;
    }

    @Override
    public Boolean handle(DeleteHoatDongCommand command) {
        hoatDongRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "HoatDong khong ton tai: " + command.getId()));

        hoatDongRepo.deleteById(command.getId());
        return true;
    }
}