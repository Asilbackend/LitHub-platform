package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("SELECT p FROM Patient p " +
            "JOIN p.user u " +
            "WHERE lower(u.phone) LIKE lower(concat('%', :search, '%')) " +
            "   OR lower(u.firstName) LIKE lower(concat('%', :search, '%')) " +
            "   OR lower(u.lastName) LIKE lower(concat('%', :search, '%')) " +
            "GROUP BY p order by p.id desc")
    List<Patient> findPatients(String search);

    Optional<Patient> findByUserId(Integer id);

    void deleteByUserId(Integer id);
}