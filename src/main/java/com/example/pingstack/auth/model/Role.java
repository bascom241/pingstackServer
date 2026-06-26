package com.example.pingstack.auth.model;

import jakarta.persistence.*;


import java.util.UUID;
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @Column(
            nullable = false,
            length = 32
    )
    private String roleName;

    public Role() {
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Role(UUID roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
