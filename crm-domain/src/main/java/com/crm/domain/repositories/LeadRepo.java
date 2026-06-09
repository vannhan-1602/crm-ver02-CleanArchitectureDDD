package com.crm.domain.repositories;

import com.crm.domain.entities.Lead;

import java.util.List;
import java.util.Optional;


public interface LeadRepo {

    Lead save(Lead lead);

    Optional<Lead> findById(Long id);


    List<Lead> findAll();


    List<Lead> findByNhanVienPhuTrachId(Integer nhanVienId);

 
    void softDeleteById(Long id);
}