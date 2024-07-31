package org.example.hospitalmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospitalmanagement.entity.Patient;
import org.example.hospitalmanagement.entity.User;
import org.example.hospitalmanagement.repository.PatientRepository;
import org.example.hospitalmanagement.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final UserService userService;
    private final PatientRepository patientRepository;

    @GetMapping("/office")
    public String office(@RequestParam(required = false) Integer patientId, Authentication authentication) {
        User authUser = userService.getAuthUser(authentication);
        Optional<Patient> patientOptional = patientRepository.findByUserId(authUser.getId());
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            return "redirect:/admin/show/patient?patientId=" + patient.getId();
        } else if (patientId != null) {
            return "redirect:/admin/show/patient?patientId=" + patientId;
        }
        return "patient";
    }
}
