package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.loaisanpham.command.CreateLoaiSanPhamCommand;
import com.crm.application.loaisanpham.command.UpdateLoaiSanPhamCommand;
import com.crm.application.sanpham.command.CreateSanPhamCommand;
import com.crm.application.sanpham.command.DeleteSanPhamCommand;
import com.crm.application.sanpham.command.UpdateSanPhamCommand;
import com.crm.application.sanpham.handler.*;
import com.crm.application.sanpham.query.GetAllSanPhamQuery;
import com.crm.application.sanpham.query.GetSanPhamByIdQuery;
import com.crm.domain.entities.SanPham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/sanpham")
@CrossOrigin(origins = "http://localhost:5173")
public class SanPhamController {

    private final Mediator mediator;

    public SanPhamController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping
    public ResponseEntity<List<SanPham>> getAll() {
        return ResponseEntity.ok(mediator.send(new GetAllSanPhamQuery()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(mediator.send(new GetSanPhamByIdQuery(id)));
    }

    @PostMapping
    public ResponseEntity<SanPham> create(@RequestBody CreateSanPhamCommand command) {
        return new ResponseEntity<>(mediator.send(command), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPham> update(@PathVariable Integer id,
                                          @RequestBody UpdateSanPhamCommand command) {
        command.setId(id);
        return ResponseEntity.ok(mediator.send(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(mediator.send(new DeleteSanPhamCommand(id)));
    }
}