package com.crm.application.hopdong.handler;

import com.crm.application.common.IRequestHandler;
import com.crm.application.hopdong.command.UpdateHopDongCommand;
import com.crm.domain.entities.HopDong;
import com.crm.domain.repositories.HopDongRepo;
import com.crm.domain.valueobjects.MaHopDong;
import com.crm.domain.valueobjects.TrangThaiHopDong;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UpdateHopDongHandler implements IRequestHandler<UpdateHopDongCommand, HopDong> {
    private final HopDongRepo hopDongRepo;

    public UpdateHopDongHandler(HopDongRepo hopDongRepo) {
        this.hopDongRepo = hopDongRepo;
    }

    @Override
    public HopDong handle(UpdateHopDongCommand command) {
        HopDong existing = hopDongRepo.findById(command.getId())
                .orElseThrow(() -> new NoSuchElementException("Hop dong khong ton tai: " + command.getId()));

        if (command.getMaHopDong() != null && !command.getMaHopDong().isBlank()) {
            existing.changeMaHopDong(new MaHopDong(command.getMaHopDong()));
        }

        existing.updateDetails(
                command.getKhachHangId(),
                command.getNgayKy(),
                command.getThoiHan(),
                TrangThaiHopDong.from(command.getTrangThai())
        );

        return hopDongRepo.save(existing);
    }
}
