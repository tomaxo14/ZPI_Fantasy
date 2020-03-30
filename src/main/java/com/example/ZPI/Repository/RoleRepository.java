package com.example.ZPI.Repository;

import com.example.ZPI.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends MongoRepository<Role, Integer> {

    @Override
    List<Role> findAll();

}
