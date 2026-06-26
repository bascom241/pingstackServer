package com.example.pingstack.auth.service;

import com.example.pingstack.auth.model.Role;
import com.example.pingstack.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;


    public Role createRole(Role role){
        Optional<Role> roleExist = roleRepository.findByRoleName(role.getRoleName());

        if(roleExist.isPresent()){
            throw new RuntimeException("Role Exist Already");
        }

        Role roleToSave = new Role();
        roleToSave.setRoleName(role.getRoleName());
        return roleRepository.save(roleToSave);
    }

    public List<Role> getAllRoles (){
        return roleRepository.findAll();
    }
}
