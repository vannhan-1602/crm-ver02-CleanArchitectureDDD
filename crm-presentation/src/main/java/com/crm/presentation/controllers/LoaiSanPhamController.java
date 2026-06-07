package com.crm.presentation.SanPham;

import com.crm.application.loaisanpham.command.*;
import com.crm.application.loaisanpham.handler.*;
import com.crm.application.loaisanpham.query.*;
import com.crm.domain.entities.LoaiSanPham;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/loaisanpham")
@CrossOrigin(origins = "http://localhost:5173")
public class LoaiSanPhamController {

    private final CreateLoaiSanPhamHandler createHandler;
    private final UpdateLoaiSanPhamHandler updateHandler;
    private final DeleteLoaiSanPhamHandler deleteHandler;
    private final GetAllLoaiSanPhamQueryHandler getAllHandler;
    private final GetLoaiSanPhamByIdQueryHandler getByIdHandler;

    public LoaiSanPhamController(CreateLoaiSanPhamHandler createHandler,
                                 UpdateLoaiSanPhamHandler updateHandler,
                                 DeleteLoaiSanPhamHandler deleteHandler,
                                 GetAllLoaiSanPhamQueryHandler getAllHandler,
                                 GetLoaiSanPhamByIdQueryHandler getByIdHandler) {
        this.createHandler = createHandler;
        this.updateHandler = updateHandler;
        this.deleteHandler = deleteHandler;
        this.getAllHandler = getAllHandler;
        this.getByIdHandler = getByIdHandler;
    }

    @GetMapping
    public ResponseEntity<List<LoaiSanPham>> getAll() {
        return ResponseEntity.ok(getAllHandler.handle(new GetAllLoaiSanPhamQuery()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiSanPham> getById(@PathVariable Integer  id) {
        return ResponseEntity.ok(getByIdHandler.handle(new GetLoaiSanPhamByIdQuery(id)));
    }

    @PostMapping
    public ResponseEntity<LoaiSanPham> create(@RequestBody CreateLoaiSanPhamCommand command) {
        return new ResponseEntity<>(createHandler.handle(command), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiSanPham> update(@PathVariable Integer  id,
                                              @RequestBody UpdateLoaiSanPhamCommand command) {
        command.setId(id);
        return ResponseEntity.ok(updateHandler.handle(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer  id) {
        return ResponseEntity.ok(deleteHandler.handle(new DeleteLoaiSanPhamCommand(id)));
    }
}