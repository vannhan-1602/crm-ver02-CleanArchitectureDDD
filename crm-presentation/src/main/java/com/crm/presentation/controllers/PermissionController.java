package com.crm.presentation.controllers;

import com.crm.application.auth.PermissionCatalog;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/permissions")
public class PermissionController {
    @GetMapping("/modules")
    public List<PermissionCatalog.ModuleInfo> getModules() {
        return PermissionCatalog.MODULES;
    }
}
