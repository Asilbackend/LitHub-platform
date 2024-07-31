package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.dto.AdmissionDto;
import org.example.hospitalmanagement.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.ArrayList;
import java.util.List;
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {
    List<Admission> findAllByDoctorId(Integer doctorId);
    @Query("SELECT a.id as id, a.localDateTime as localDateTime, a.arrivedDateTime as arrivedDateTime, a.description as description, a.status as status, a.patient as patient, a.doctor as doctor, a.procedures as procedures, a.administrator as administrator, a.orderCount as orderCount, SUM(p.price) as sum " +
            "FROM Admission a JOIN a.procedures p " +
            "GROUP BY a.id, a.localDateTime, a.arrivedDateTime, a.description, a.status, a.patient, a.doctor, a.procedures, a.administrator, a.orderCount")
    ArrayList<AdmissionDto> findAllDto();
    @Query(value = "select t as admisson,5 as sum from Admission t")
    List<AdmissionDto> getLong();
    List<Admission> findAllByStatus(String qabulda);
    List<Admission> findAllByStatusAndDoctorIdOrderByOrderCount(String qabulda, int i);
    List<Admission> findAllByPatientIdOrderByIdDesc(int i);

}
