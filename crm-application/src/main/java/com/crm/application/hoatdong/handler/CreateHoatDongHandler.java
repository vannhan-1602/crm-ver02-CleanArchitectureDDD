package com.crm.application.hoatdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hoatdong.command.CreateHoatDongCommand;
import com.crm.domain.entities.HoatDong;
import com.crm.domain.repositories.HoatDongRepo;
import com.crm.domain.valueobjects.LoaiHoatDong;
import org.springframework.stereotype.Service;


@Service
public class CreateHoatDongHandler implements IRequestHandler<CreateHoatDongCommand, HoatDong> {

    private final HoatDongRepo hoatDongRepo;

    public CreateHoatDongHandler(HoatDongRepo hoatDongRepo) {
        this.hoatDongRepo = hoatDongRepo;
    }

    @Override
    public HoatDong handle(CreateHoatDongCommand command) {
        HoatDong hoatDong = new HoatDong(
                command.getKhachHangId(),
                command.getLeadId(),
                LoaiHoatDong.from(command.getLoaiHoatDong()),
                command.getNoiDung(),
                command.getThoiGianThucHien(),
                command.getNhanVienId()
        );
        return hoatDongRepo.save(hoatDong);
    }
}