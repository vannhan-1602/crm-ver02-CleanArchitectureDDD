package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.loaiticket.command.CreateLoaiTicketCommand;
import com.crm.application.loaiticket.command.DeleteLoaiTicketCommand;
import com.crm.application.loaiticket.command.UpdateLoaiTicketCommand;
import com.crm.application.loaiticket.query.GetAllLoaiTicketQuery;
import com.crm.application.loaiticket.query.GetLoaiTicketByIdQuery;
import com.crm.domain.entities.LoaiTicket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/loai-ticket")
public class LoaiTicketController {

    private final Mediator mediator;

    public LoaiTicketController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping
    public ResponseEntity<List<LoaiTicket>> getAll() {
        return ResponseEntity.ok(mediator.send(new GetAllLoaiTicketQuery()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiTicket> getById(@PathVariable Short id) {
        return ResponseEntity.ok(mediator.send(new GetLoaiTicketByIdQuery(id)));
    }

    @PostMapping
    public ResponseEntity<LoaiTicket> create(@RequestBody CreateLoaiTicketCommand command) {
        return new ResponseEntity<>(mediator.send(command), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiTicket> update(@PathVariable Short id,
                                             @RequestBody UpdateLoaiTicketCommand command) {
        command.setId(id);
        return ResponseEntity.ok(mediator.send(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Short id) {
        return ResponseEntity.ok(mediator.send(new DeleteLoaiTicketCommand(id)));
    }
}
