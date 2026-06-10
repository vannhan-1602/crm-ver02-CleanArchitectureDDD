
package com.crm.presentation.controllers;

import com.crm.application.common.Mediator;
import com.crm.application.nhanvien.query.GetAllNhanVienQuery;
import com.crm.domain.entities.NhanVien;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/nhan-vien")
public class NhanVienController {

    private final Mediator mediator;

    public NhanVienController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping
    public List<NhanVienResponse> getAll() {
        List<NhanVien> list = mediator.send(new GetAllNhanVienQuery());
        return list.stream()
                .map(NhanVienResponse::from)
                .toList();
    }


    static class NhanVienResponse {
        private Integer id;
        private String  hoTen;

        public static NhanVienResponse from(NhanVien nv) {
            NhanVienResponse r = new NhanVienResponse();
            r.id    = nv.getId();
            r.hoTen = nv.getHoTen();
            return r;
        }

        public Integer getId()    { return id; }
        public String  getHoTen() { return hoTen; }
    }
}