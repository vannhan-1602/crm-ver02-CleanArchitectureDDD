package com.crm.application.hopdong.handler;

import com.crm.application.hopdong.command.CreateHopDongCommand;
import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import com.crm.domain.valueobjects.MaHopDong;
import com.crm.domain.valueobjects.TrangThaiHopDong;
import org.springframework.stereotype.Service;

@Service
public class CreateHopDongHandler {
    private final HopDongRepo hopDongRepo;

    public CreateHopDongHandler(HopDongRepo hopDongRepo) {
        this.hopDongRepo = hopDongRepo;
    }

    public HopDong handle(CreateHopDongCommand command) {
        HopDong hopDong = new HopDong(
                new MaHopDong(command.getMaHopDong()),
                command.getKhachHangId(),
                command.getNgayKy(),
                command.getThoiHan(),
                TrangThaiHopDong.from(command.getTrangThai())
        );
        return hopDongRepo.save(hopDong);
    }
}
