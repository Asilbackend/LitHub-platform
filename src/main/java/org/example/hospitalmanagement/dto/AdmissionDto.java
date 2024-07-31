package org.example.hospitalmanagement.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.hospitalmanagement.entity.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Data
public class AdmissionDto {
    private Integer id;
    private LocalDateTime localDateTime;
    private LocalDateTime arrivedDateTime;
    private String description;
    private String status;
    private Patient patient;
    private Doctor doctor;
    List<Procedure> procedures;
    private Administrator administrator;
    private Integer orderCount;
    private Integer sum;
}