package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Integer> {
}