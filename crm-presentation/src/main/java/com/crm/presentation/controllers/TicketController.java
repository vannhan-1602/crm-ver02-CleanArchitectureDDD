package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.ticket.command.CreateTicketCommand;
import com.crm.application.ticket.command.DeleteTicketCommand;
import com.crm.application.ticket.command.UpdateTicketCommand;
import com.crm.application.ticket.query.GetAllTicketQuery;
import com.crm.application.ticket.query.GetTicketByIdQuery;
import com.crm.domain.entities.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tickets")
public class TicketController {

    private final Mediator mediator;

    public TicketController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAll(
            @RequestParam(required = false) Long khachHangId,
            @RequestParam(required = false) String trangThai) {
        return ResponseEntity.ok(mediator.send(new GetAllTicketQuery(khachHangId, trangThai)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mediator.send(new GetTicketByIdQuery(id)));
    }

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody CreateTicketCommand command) {
        return new ResponseEntity<>(mediator.send(command), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(@PathVariable Long id,
                                         @RequestBody UpdateTicketCommand command) {
        command.setId(id);
        return ResponseEntity.ok(mediator.send(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(mediator.send(new DeleteTicketCommand(id)));
    }
}
