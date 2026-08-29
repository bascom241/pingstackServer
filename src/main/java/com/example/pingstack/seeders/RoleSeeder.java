package com.example.pingstack.seeders;

import com.example.pingstack.auth.model.Role;
import com.example.pingstack.auth.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoleSeeder implements DataSeeder{

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seed() {

            if (roleRepository.count() == 0) {
                roleRepository.save(new Role("user"));  // Create constructors without ID
                roleRepository.save(new Role("admin"));
                System.out.println("Role Data Seeded");
            }


    }

    @Override
    public int getOrder() {
        return 2;
    }
}
