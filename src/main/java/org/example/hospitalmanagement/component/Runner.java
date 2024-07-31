package org.example.hospitalmanagement.component;

import lombok.RequiredArgsConstructor;
import org.example.hospitalmanagement.entity.*;
import org.example.hospitalmanagement.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String myProperty;
    private final RoleRepository roleRepository;
    private final SpecialityRepository specialityRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final RoomRepository roomRepository;
    private final PatientRepository patientRepository;
    private final AdministratorRepository administratorRepository;
    private final ProcedureRepository procedureRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (myProperty.equals("create")) {
            Role roleAdmin = roleRepository.save(new Role("ROLE_ADMIN"));
            Role rolePatient = roleRepository.save(new Role("ROLE_PATIENT"));
            Role roleDoctor = roleRepository.save(new Role("ROLE_DOCTOR"));
            Role superAdmin = roleRepository.save(new Role("ROLE_SUPER_ADMIN"));

            List<Speciality> specialities = new ArrayList<>();
            Speciality speciality1 = new Speciality("LOR");
            specialities.add(speciality1);
            Speciality speciality2 = new Speciality("Pulmonolog");
            specialities.add(speciality2);
            Speciality speciality3 = new Speciality("Kardiolog");
            specialities.add(speciality3);
            Speciality speciality4 = new Speciality("Jarroh");
            specialities.add(speciality4);
            Speciality speciality5 = new Speciality("Stamotolog");
            specialities.add(speciality5);
            specialityRepository.saveAll(specialities);


            Room room1 = roomRepository.save(Room.builder().name("1-xona").build());
            Room room2 = roomRepository.save(Room.builder().name("2-xona").build());
            Room room3 = roomRepository.save(Room.builder().name("3-xona").build());
            Room room4 = roomRepository.save(Room.builder().name("4-xona").build());
            Room room5 = roomRepository.save(Room.builder().name("5-xona").build());
            Room room6 = roomRepository.save(Room.builder().name("6-xona").build());
            Room room7 = roomRepository.save(Room.builder().name("7-xona").build());
            Room room8 = roomRepository.save(Room.builder().name("8-xona").build());
            Room room9 = roomRepository.save(Room.builder().name("9-xona").build());
            Room room10 = roomRepository.save(Room.builder().name("10-xona").build());

            Doctor doctor1 = Doctor.builder().speciality(speciality1).room(room1)
                    .user(userRepository.save(User.builder().phone("123").firstName("Anvar").lastName("Ochilov")
                            .password(passwordEncoder.encode("123")).roles(List.of(roleDoctor)).build())).build();
            Doctor doctor2 = Doctor.builder().speciality(speciality1).room(room2)
                    .user(userRepository.save(User.builder().phone("1234").firstName("Qosim").lastName("Hamdamov")
                            .password(passwordEncoder.encode("123")).roles(List.of(roleDoctor)).build())).build();
            Doctor doctor3 = Doctor.builder().speciality(speciality2).room(room3)
                    .user(userRepository.save(User.builder().phone("1235").firstName("Sanjar").lastName("Madiyorov")
                            .password(passwordEncoder.encode("123")).roles(List.of(roleDoctor)).build())).build();
            Doctor doctor4 = Doctor.builder().speciality(speciality3).room(room4)
                    .user(userRepository.save(User.builder().phone("1236").firstName("Akbar").lastName("Soliyev")
                            .password(passwordEncoder.encode("123")).roles(List.of(roleDoctor)).build())).build();
            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);
            doctorRepository.save(doctor3);
            doctorRepository.save(doctor4);

            Patient patient1 = patientRepository.save(Patient.builder().user(userRepository.save(User.builder().phone("1221").firstName("Ahror").lastName("Olimov")
                    .password(passwordEncoder.encode("123")).roles(List.of(rolePatient)).build())).build());
            Patient patient2 = patientRepository.save(Patient.builder().user(userRepository.save(User.builder().phone("909209103").firstName("Sayfillo").lastName("Jumayev")
                    .password(passwordEncoder.encode("123")).roles(List.of(rolePatient)).build())).build());
            Patient patient3 = patientRepository.save(Patient.builder().user(userRepository.save(User.builder().phone("336396013").firstName("Hasan").lastName("Qobilov")
                    .password(passwordEncoder.encode("123")).roles(List.of(rolePatient)).build())).build());
            Patient patient4 = patientRepository.save(Patient.builder().user(userRepository.save(User.builder().phone("930458263").firstName("Jahon").lastName("Sattorov")
                    .password(passwordEncoder.encode("123")).roles(List.of(rolePatient)).build())).build());


            Administrator administrator1 = administratorRepository.save(Administrator.builder().user(
                    userRepository.save(User.builder().phone("100").firstName("Salohiddin").lastName("Ochilov")
                            .password(passwordEncoder.encode("123")).roles(List.of(roleAdmin)).build())
            ).build());
            Administrator administrator2 = administratorRepository.save(Administrator.builder().user(
                    userRepository.save(User.builder().phone("101").firstName("Humoyun").lastName("Ochilov")
                            .password(passwordEncoder.encode("123")).roles(List.of(roleAdmin)).build())
            ).build());
            Procedure procedure1 = procedureRepository.save(Procedure.builder().price(300000).description("3-darajali kasallik").build());
            Procedure procedure2 = procedureRepository.save(Procedure.builder().price(300000).description("2-darajali kasallik").build());
            Procedure procedure3 = procedureRepository.save(Procedure.builder().price(300000).description("1-darajali kasallik").build());

            userRepository.save(User.builder().phone("1").firstName("Asilbek").lastName("O'ktamov").password(passwordEncoder.encode("1")).roles(List.of(superAdmin)).build());
        }
    }
}