package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.loaisanpham.command.*;
import com.crm.application.loaisanpham.query.*;
import com.crm.domain.entities.LoaiSanPham;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/loaisanpham")
@CrossOrigin(origins = "http://localhost:5173")
public class LoaiSanPhamController {

    private final Mediator mediator;

    public LoaiSanPhamController(Mediator mediator) {
        this.mediator = mediator;
    }
    @GetMapping
    public ResponseEntity<List<LoaiSanPham>> getAll() {
        return ResponseEntity.ok(mediator.send(new GetAllLoaiSanPhamQuery()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiSanPham> getById(@PathVariable Integer  id) {
        return ResponseEntity.ok(mediator.send(new GetLoaiSanPhamByIdQuery(id)));
    }

    @PostMapping
    public ResponseEntity<LoaiSanPham> create(@RequestBody CreateLoaiSanPhamCommand command) {
        return new ResponseEntity<>(mediator.send(command), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiSanPham> update(@PathVariable Integer  id,
                                              @RequestBody UpdateLoaiSanPhamCommand command) {
        command.setId(id);
        return ResponseEntity.ok(mediator.send(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer  id) {
        return ResponseEntity.ok(mediator.send(new DeleteLoaiSanPhamCommand(id)));
    }
}
