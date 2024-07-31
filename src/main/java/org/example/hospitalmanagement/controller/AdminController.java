package org.example.hospitalmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospitalmanagement.db.DB;
import org.example.hospitalmanagement.entity.*;
import org.example.hospitalmanagement.repository.*;
import org.example.hospitalmanagement.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdmissionRepository admissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final UserService userService;

    @GetMapping("/office")
    public String goAdminOffice(Model model, @RequestParam(required = false) String searchText, @RequestParam(required = false) Integer patientId, @RequestParam(required = false) Integer doctorId) {
        if (searchText != null) {
            model.addAttribute("search", searchText);
        } else {
            searchText = "";
        }
        List<Patient> patientList = getPatients(searchText);
        System.out.println(patientId);
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId).get();
            model.addAttribute("currentPatient", patient);
        }
        List<Doctor> doctorList = doctorRepository.findAll();
        List<Admission> admissionList = admissionRepository.findAll(Sort.by("localDateTime").descending());
        model.addAttribute("patients", patientList);
        model.addAttribute("doctors", doctorList);
        model.addAttribute("admissions", admissionList);
        return "adminOffice";
    }

    @GetMapping("/add/patient")
    public String addPatient(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "addPatient";
    }

    @PostMapping("/add/patient")
    @Transactional
    public String savePatient(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String rePassword
    ) {
        if (rePassword.equals(password)) {
            Role role = roleRepository.findByRole("ROLE_PATIENT");
            if (userRepository.findByPhone(phone).isEmpty()) {
                User user = userRepository.save(User.builder().roles(List.of(role)).phone(phone).firstName(firstName).lastName(lastName).password(password).build());
                Patient patient = patientRepository.save(Patient.builder().user(user).build());
                return "redirect:/admin/office?patientId=" + patient.getId();
            }
        }
        return "redirect:/admin/add/patient?error=invalid password or previously used phone";
    }

    @PostMapping("/add/admission")
    @Transactional
    public String saveAdmission(
            @RequestParam Integer patientId,
            @RequestParam Integer doctorId,
            @RequestParam(required = false) String description,
            @RequestParam LocalDateTime dateTime,
            Authentication authentication
    ) {
        Administrator administrator = administratorRepository.findByUserId(userService.getAuthUser(authentication).getId());
        Admission admission = Admission.builder()
                .doctor(doctorRepository.findById(doctorId).get())
                .patient(patientRepository.findById(patientId).get())
                .status(DB.KIRITIB_YUBORISH).administrator(administrator)
                .description(description)
                .localDateTime(dateTime)
                .build();
        if (description != null) {
            admission.setDescription(description);
        }
        admissionRepository.save(admission);
        return "redirect:/admin/office?patientId=" + patientId;
    }

    @GetMapping("/add/accept")
    public String acceptPatient(@RequestParam Integer admissionId, @RequestParam(required = false) Integer patientId) {
        Admission admission = admissionRepository.findById(admissionId).get();
        int size = admissionRepository.findAllByStatusAndDoctorIdOrderByOrderCount(DB.QABULDA, admission.getDoctor().getId()).size();
        admission.setStatus(DB.QABULDA);
        admission.setOrderCount(size);
        admission.setArrivedDateTime(LocalDateTime.now());
        admissionRepository.save(admission);
        if (patientId != null) {
            return "redirect:/admin/office?patientId=" + patientId;
        } else {
            return "redirect:/admin/office";
        }
    }

    @GetMapping("/show/patient")
    public String showPatients(Model model, @RequestParam Integer patientId) {
        List<Admission> admissionList = admissionRepository.findAllByPatientIdOrderByIdDesc(patientId);
        model.addAttribute("admissions", admissionList);
        return "patient";
    }

    private List<Patient> getPatients(String searchText) {
        String search;
        search = Objects.requireNonNullElse(searchText, "searchText");
        return patientRepository.findPatients(search);
    }
}
