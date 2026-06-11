package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.ticketphanhoi.command.CreateTicketPhanHoiCommand;
import com.crm.application.ticketphanhoi.command.DeleteTicketPhanHoiCommand;
import com.crm.application.ticketphanhoi.query.GetAllTicketPhanHoiQuery;
import com.crm.application.ticketphanhoi.query.GetTicketPhanHoiByIdQuery;
import com.crm.domain.entities.TicketPhanHoi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ticket-phan-hoi")
public class TicketPhanHoiController {

    private final Mediator mediator;

    public TicketPhanHoiController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping
    public ResponseEntity<List<TicketPhanHoi>> getAll(@RequestParam(required = false) Long ticketId) {
        return ResponseEntity.ok(mediator.send(new GetAllTicketPhanHoiQuery(ticketId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketPhanHoi> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mediator.send(new GetTicketPhanHoiByIdQuery(id)));
    }

    @PostMapping
    public ResponseEntity<TicketPhanHoi> create(@RequestBody CreateTicketPhanHoiCommand command) {
        return new ResponseEntity<>(mediator.send(command), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(mediator.send(new DeleteTicketPhanHoiCommand(id)));
    }
}
