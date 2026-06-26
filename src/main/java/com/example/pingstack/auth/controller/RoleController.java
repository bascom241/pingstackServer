package com.example.pingstack.auth.controller;

import com.example.pingstack.auth.model.Role;
import com.example.pingstack.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/role")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> saveRole (@RequestBody Role role){
        Role savedRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @GetMapping
    public ResponseEntity<List<Role>> getRoles(){
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }
}
