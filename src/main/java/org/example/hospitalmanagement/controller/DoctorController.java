package org.example.hospitalmanagement.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.hospitalmanagement.db.DB;
import org.example.hospitalmanagement.entity.Admission;
import org.example.hospitalmanagement.entity.Doctor;
import org.example.hospitalmanagement.entity.Procedure;
import org.example.hospitalmanagement.entity.User;
import org.example.hospitalmanagement.repository.AdmissionRepository;
import org.example.hospitalmanagement.repository.DoctorRepository;
import org.example.hospitalmanagement.repository.ProcedureRepository;
import org.example.hospitalmanagement.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final AdmissionRepository admissionRepository;
    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final ProcedureRepository procedureRepository;

    @GetMapping("/office")
    public String goDoctor(Model model, Authentication authentication, @RequestParam(required = false) Integer admissionId, HttpSession session) {
        if (admissionId != null) {
            Admission admission = admissionRepository.findById(admissionId).get();
            session.setAttribute("admission", admission);
            model.addAttribute("currentAdmission", admission);
        }
        User authUser = userService.getAuthUser(authentication);
        if (authUser != null) {
            Doctor doctor = doctorRepository.findByUserId(authUser.getId());
            List<Admission> admissionList = admissionRepository.findAllByStatusAndDoctorIdOrderByOrderCount(DB.QABULDA, doctor.getId());
            /*admissionList.sort(Comparator.comparingInt(Admission::getOrderCount));*/
            if (!admissionList.isEmpty()) {
                model.addAttribute("admissions", admissionList);
            }
        }
        return "doctor";
    }

    @PostMapping("/procedure")
    public String saveProcedures(@RequestBody List<Procedure> procedures, Authentication authentication, HttpSession session) {
        System.out.println(procedures + "<-pro----------------------------keldi");
        Object object = session.getAttribute("admission");
        if (object != null) {
            List<Procedure> procedureList = procedureRepository.saveAll(procedures);
            Admission admission = (Admission) object;
            System.out.println("admission topildi");
            admission.setProcedures(procedureList);
            Duration between = Duration.between(admission.getArrivedDateTime(), admission.getLocalDateTime());
            if (between.getSeconds() >= 0) {
                admission.setStatus(DB.VAQTIDA_KELGAN);
            } else {
                admission.setStatus(DB.KECHIKIB_KELGAN);
            }
            admissionRepository.save(admission);
            minusOrderCountOfAdmission(authentication, admission);
        }
        session.removeAttribute("admission");
        return "redirect:/doctor/office";
    }

    private void minusOrderCountOfAdmission(Authentication authentication, Admission admission) {
        List<Admission> admissionList = admissionRepository.findAllByStatusAndDoctorIdOrderByOrderCount(DB.QABULDA, userService.getAuthUser(authentication).getId());
        for (Admission admission1 : admissionList) {
            if (admission1.getOrderCount() > admission.getOrderCount()) {
                admission1.setOrderCount(admission1.getOrderCount() - 1);
            }
        }
        admissionRepository.saveAll(admissionList);
    }
}
