package com.crm.application.hoatdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoatdong.command.UpdateHoatDongCommand;
import com.crm.domain.entities.HoatDong;
import com.crm.domain.repositories.HoatDongRepo;
import com.crm.domain.valueobjects.LoaiHoatDong;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class UpdateHoatDongHandler implements IRequestHandler<UpdateHoatDongCommand, HoatDong> {

    private final HoatDongRepo hoatDongRepo;

    public UpdateHoatDongHandler(HoatDongRepo hoatDongRepo) {
        this.hoatDongRepo = hoatDongRepo;
    }

    @Override
    public HoatDong handle(UpdateHoatDongCommand command) {
        HoatDong hoatDong = hoatDongRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "HoatDong khong ton tai: " + command.getId()));

        hoatDong.capNhat(
                command.getLoaiHoatDong() != null
                        ? LoaiHoatDong.from(command.getLoaiHoatDong()) : null,
                command.getNoiDung(),
                command.getThoiGianThucHien(),
                command.getNhanVienId()
        );

        return hoatDongRepo.save(hoatDong);
    }
}