package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Administrator findByUserId(Integer id);
    Optional<Administrator> findFirstByUserId(Integer id);
    void deleteByUserId(Integer id);
}