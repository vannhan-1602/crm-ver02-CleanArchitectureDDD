package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.sanpham.command.AddHinhAnhToSanPhamCommand;
import com.crm.application.sanpham.command.CreateSanPhamCommand;
import com.crm.application.sanpham.command.DeleteSanPhamCommand;
import com.crm.application.sanpham.command.UpdateSanPhamCommand;
import com.crm.application.sanpham.query.GetAllSanPhamQuery;
import com.crm.application.sanpham.query.GetSanPhamByIdQuery;
import com.crm.application.sanphamhinhanh.command.DeleteSanPhamHinhAnhCommand;
import com.crm.domain.entities.SanPham;
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
import java.util.UUID;

@RestController
@RequestMapping("api/sanpham")
public class SanPhamController {

    private final Mediator mediator;
    private static final String UPLOAD_DIR = "uploads/products/";

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

    // Upload file ảnh
    @PostMapping(value = "/{id}/hinhanh", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SanPham> uploadHinhAnh(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isMain", defaultValue = "0") Integer isMain) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        String url = "/" + UPLOAD_DIR + fileName;
        AddHinhAnhToSanPhamCommand command = new AddHinhAnhToSanPhamCommand(id, url, isMain);
        return new ResponseEntity<>(mediator.send(command), HttpStatus.CREATED);
    }

    // Xóa ảnh theo id
    @DeleteMapping("/{sanPhamId}/hinhanh/{hinhAnhId}")
    public ResponseEntity<Void> deleteHinhAnh(
            @PathVariable Integer sanPhamId,
            @PathVariable Integer hinhAnhId) {
        mediator.send(new DeleteSanPhamHinhAnhCommand(hinhAnhId));
        return ResponseEntity.noContent().build();
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