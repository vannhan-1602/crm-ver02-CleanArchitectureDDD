package com.crm.presentation.controllers;

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
public class SanPhamController  {
    @Autowired
    private final CreateSanPhamHandler createHandler;
    private final UpdateSanPhamHandler updateHandler;
    private final DeleteSanPhamHandler deleteHandler;
    private final GetAllSanPhamQueryHandler getAllHandler;
    private final GetSanPhamByIdQueryHandler getByIdHandler;
    public SanPhamController(CreateSanPhamHandler createHandler,
                             UpdateSanPhamHandler updateHandler,
                             DeleteSanPhamHandler deleteHandler,
                             GetAllSanPhamQueryHandler getAllHandler,
                             GetSanPhamByIdQueryHandler getByIdHandler) {
        this.createHandler = createHandler;
        this.updateHandler = updateHandler;
        this.deleteHandler = deleteHandler;
        this.getAllHandler = getAllHandler;
        this.getByIdHandler = getByIdHandler;
    }    @GetMapping()
    public ResponseEntity<List<SanPham>> GetAll() {
            return new ResponseEntity<>(getAllHandler.handle(new GetAllSanPhamQuery()), HttpStatus.OK);

    }
    @PostMapping()
    public ResponseEntity<SanPham> Create(@RequestBody CreateSanPhamCommand command) {
        return new ResponseEntity<>(createHandler.handle(command), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SanPham> GetById(@PathVariable Integer  id) {
        return new ResponseEntity<SanPham>(getByIdHandler.handle(new GetSanPhamByIdQuery(id)), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<SanPham> Update(@PathVariable Integer  id, @RequestBody UpdateSanPhamCommand command) {
        command.setId(id);
        return ResponseEntity.ok(updateHandler.handle(command));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> Delete(@PathVariable Integer  id) {
        return ResponseEntity.ok(deleteHandler.handle(new DeleteSanPhamCommand(id)));

    }
}
