package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String rolePatient);

    List<Role> findAllByIdIn(List<Integer> rolesId);
}