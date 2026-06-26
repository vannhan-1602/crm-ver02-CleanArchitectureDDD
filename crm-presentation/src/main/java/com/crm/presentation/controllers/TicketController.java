package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.ticket.command.CreateTicketCommand;
import com.crm.application.ticket.command.DeleteTicketCommand;
import com.crm.application.ticket.command.UpdateTicketCommand;
import com.crm.application.ticket.query.GetAllTicketQuery;
import com.crm.application.ticket.query.GetTicketByIdQuery;
import com.crm.domain.entities.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tickets")
public class TicketController {

    private final Mediator mediator;
    private static final String UPLOAD_DIR = "uploads/tickets/";

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

    @PostMapping(value = "/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadAttachment(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File dinh kem khong duoc de trong"));
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename() == null ? "attachment" : file.getOriginalFilename();
        String safeName = originalName.replaceAll("[\\\\/:*?\"<>|]", "_");
        String fileName = UUID.randomUUID() + "_" + safeName;
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        return ResponseEntity.ok(Map.of(
                "url", "/" + UPLOAD_DIR + fileName,
                "fileName", originalName
        ));
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
