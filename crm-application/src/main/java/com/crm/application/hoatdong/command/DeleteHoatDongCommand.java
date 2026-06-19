package com.crm.application.hoatdong.command;

import com.crm.application.common.IRequest;

public class DeleteHoatDongCommand implements IRequest<Boolean> {

    private Long id;

    public DeleteHoatDongCommand(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
}