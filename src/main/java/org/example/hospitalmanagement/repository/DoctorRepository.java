package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Doctor findByUserId(Integer id);

    Optional<Doctor> findFirstByUserId(Integer id);

    void deleteByUserId(Integer id);
}