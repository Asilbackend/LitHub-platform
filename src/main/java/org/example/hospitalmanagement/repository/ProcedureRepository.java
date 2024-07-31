package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.entity.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {
}