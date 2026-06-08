package com.crm.application.hopdong.command;

public class DeleteHopDongCommand {
    private Long id;
    public DeleteHopDongCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
