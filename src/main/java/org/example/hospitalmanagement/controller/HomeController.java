package org.example.hospitalmanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.hospitalmanagement.entity.Admission;
import org.example.hospitalmanagement.entity.Procedure;
import org.example.hospitalmanagement.entity.User;
import org.example.hospitalmanagement.repository.AdmissionRepository;
import org.example.hospitalmanagement.repository.PatientRepository;
import org.example.hospitalmanagement.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final AdmissionRepository admissionRepository;

    @GetMapping("/")
    public String goHome() {
        return "redirect:/loginSuccessUrl";
    }

    @GetMapping("/loginSuccessUrl")
    public String loginSuccess() {
        System.out.println("role: ------------------------------------------------------------------------------------------------------ ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        System.out.println("role: " + role);
        return switch (role) {
            case "ROLE_ADMIN" -> "redirect:/admin/office";
            case "ROLE_PATIENT" -> "redirect:/patient/office";
            case "ROLE_DOCTOR" -> "redirect:/doctor/office";
            case "ROLE_SUPER_ADMIN" -> "redirect:/users";
            default -> "redirect:/";
        };
    }

    @GetMapping("/home/patient/info")
    public String showPatientInfo(Model model, @RequestParam Integer admissionId) {
        Admission admission = admissionRepository.findById(admissionId).get();
        model.addAttribute("admission", admission);
        model.addAttribute("sum", admission.getProcedures().stream().mapToInt(Procedure::getPrice).sum());
        return "showPatientInfo";
    }

    @PostMapping("/procedures")
    public String saveProcedures(@RequestBody List<Procedure> procedures, Model model) {
        System.out.println("keldi");
        System.out.println("proc:" + procedures);
        // Save the procedures to the database or perform any other necessary operations
        saveProceduresToDatabase(procedures);

        // Pass the procedures to the Thymeleaf template
        model.addAttribute("procedures", procedures);

        // Redirect to a success page or return the template name
        return "home";
    }

    private void saveProceduresToDatabase(List<Procedure> procedures) {
        // Implement the logic to save the procedures to the database
        // This is just a placeholder for the actual implementation
        System.out.println("Saving procedures to the database: " + procedures);
    }
}
