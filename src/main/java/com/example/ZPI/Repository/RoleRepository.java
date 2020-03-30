package com.example.ZPI.Repository;

import com.example.ZPI.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Override
    List<Role> findAll();
}
