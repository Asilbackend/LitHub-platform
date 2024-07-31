package org.example.hospitalmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.hospitalmanagement.entity.abs.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder

public class Admission extends BaseEntity {
    private LocalDateTime localDateTime;
    private LocalDateTime arrivedDateTime;
    private String description;
    private String status;
    @ManyToOne
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    @ManyToMany
    List<Procedure> procedures;
    @ManyToOne
    private Administrator administrator;
    private Integer orderCount;
}
