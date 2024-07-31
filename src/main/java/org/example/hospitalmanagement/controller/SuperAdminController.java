package org.example.hospitalmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.hospitalmanagement.entity.*;
import org.example.hospitalmanagement.repository.*;
import org.example.hospitalmanagement.service.UserCrudService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class SuperAdminController {
    private final UserCrudService userService;
    private final PatientRepository patientRepository;
    private final AdministratorRepository administratorRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final SpecialityRepository specialityRepository;
    private final RoomRepository roomRepository;

    @GetMapping()
    public String getAllUsers(@RequestParam(required = false) String person, Model model) {
        if (person != null) {
            model.addAttribute("person", person);
            if (person.equals("doctor")) {
                List<Doctor> doctors = getDoctors();
                model.addAttribute("doctors", doctors);
            } else if (person.equals("patient")) {
                List<User> users = getPatients();
                model.addAttribute("users", users);
            } else if (person.equals("administrator")) {
                List<User> users = getAdministrators();
                model.addAttribute("users", users);
            }
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }
        return "userCrud";
    }

    private List<Doctor> getDoctors() {
        List<Doctor> all = doctorRepository.findAll();
        return all;
    }

    private List<User> getAdministrators() {
        List<User> users = new ArrayList<>();
        List<Administrator> all = administratorRepository.findAll();
        for (Administrator patient : all) {
            users.add(patient.getUser());
        }
        return users;
    }

    private List<User> getPatients() {
        List<User> users = new ArrayList<>();
        List<Patient> all = patientRepository.findAll();
        for (Patient patient : all) {
            users.add(patient.getUser());
        }
        return users;
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model, @RequestParam(required = false) String person) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        if (person != null) {
            model.addAttribute("person", person);
        }
        return "new-user";
    }

    @PostMapping("/save")
    @Transactional
    public String createUser(@RequestParam List<Integer> rolesId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String phone, @RequestParam(required = false) String speciality, @RequestParam(required = false) String room, @RequestParam(required = false) String person) {
        if (person != null) {
            if (userRepository.findByPhone(phone).isPresent()) {
                return "redirect:/users";
            }
            if (person.equals("doctor")) {
                Speciality specialitySave = specialityRepository.save(Speciality.builder().name(speciality).build());
                Room roomSave = roomRepository.save(Room.builder().name(room).build());
                User userSave = userRepository.save(User.builder().roles(List.of(roleRepository.findByRole("ROLE_DOCTOR"))).firstName(firstName).lastName(lastName).phone(phone).password(passwordEncoder.encode("123")).build());
                Doctor doctor = Doctor.builder().user(userSave).speciality(specialitySave).room(roomSave).build();
                doctorRepository.save(doctor);
            } else {
                List<Role> roles = roleRepository.findAllByIdIn(rolesId);
                User user1 = User.builder().roles(roles).firstName(firstName).lastName(lastName).phone(phone).password(passwordEncoder.encode("123")).build();
                User user = userRepository.save(user1);
                if (user.getRoles().contains(roleRepository.findByRole("ROLE_ADMIN"))) {
                    administratorRepository.save(Administrator.builder().user(user).build());
                } else if (user.getRoles().contains(roleRepository.findByRole("ROLE_DOCTOR"))) {
                    /// to'liqmas DOCTOR
                    doctorRepository.save(Doctor.builder().user(user).build());
                } else if (user.getRoles().contains(roleRepository.findByRole("ROLE_PATIENT"))) {
                    patientRepository.save(Patient.builder().user(user).build());
                }
            }
            return "redirect:/users?person=" + person;
        } else {
            List<Role> roles = roleRepository.findAllByIdIn(rolesId);
            User user1 = User.builder().roles(roles).firstName(firstName).lastName(lastName).phone(phone).password(passwordEncoder.encode("123")).build();
            User user = userRepository.save(user1);
            if (user.getRoles().contains(roleRepository.findByRole("ROLE_ADMIN"))) {
                administratorRepository.save(Administrator.builder().user(user).build());
            } else if (user.getRoles().contains(roleRepository.findByRole("ROLE_DOCTOR"))) {
                /// to'liqmas DOCTOR
                doctorRepository.save(Doctor.builder().user(user).build());
            } else if (user.getRoles().contains(roleRepository.findByRole("ROLE_PATIENT"))) {
                patientRepository.save(Patient.builder().user(user).build());
            } else if (user.getRoles().contains(roleRepository.findByRole("ROLE_SUPER_ADMIN"))) {

            }
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditUserForm(@PathVariable("id") Integer id, Model model, @RequestParam(required = false) String person) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        if (person != null) {
            model.addAttribute("person", person);
        }
        return "edit-user";
    }

    @PostMapping("/{id}/update")
    @Transactional
    public String updateUser(@PathVariable("id") Integer id, @RequestParam(required = false) String person, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String phone, @RequestParam(required = false) String speciality, @RequestParam(required = false) String room) {
        if (person.equals("doctor")) {
            Speciality specialitySave = specialityRepository.save(Speciality.builder().name(speciality).build());
            Room roomSave = roomRepository.save(Room.builder().name(room).build());
            Doctor doctor = doctorRepository.findByUserId(id);
            doctor.setRoom(roomSave);
            doctor.setSpeciality(specialitySave);
            doctorRepository.save(doctor);
        }
        User user1 = userRepository.findById(id).get();
        User user = User.builder().id(id).firstName(firstName).lastName(lastName).phone(phone).password(user1.getPassword()).roles(user1.getRoles()).build();
        userService.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    @Transactional
    public String deleteUser(@PathVariable("id") Integer id) {
        if (doctorRepository.findFirstByUserId(id).isPresent()) {
            doctorRepository.deleteByUserId(id);
        }
        if (patientRepository.findByUserId(id).isPresent()) {
            patientRepository.deleteByUserId(id);
        }
        if (administratorRepository.findFirstByUserId(id).isPresent()) {
            administratorRepository.deleteByUserId(id);
        }
        userService.deleteUser(id);
        return "redirect:/users";
    }
}