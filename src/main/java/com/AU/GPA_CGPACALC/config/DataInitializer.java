package com.AU.GPA_CGPACALC.config;

import com.AU.GPA_CGPACALC.entity.Department;
import com.AU.GPA_CGPACALC.entity.Regulation;
import com.AU.GPA_CGPACALC.entity.User;
import com.AU.GPA_CGPACALC.entity.Role;
import com.AU.GPA_CGPACALC.repository.DepartmentRepository;
import com.AU.GPA_CGPACALC.repository.RegulationRepository;
import com.AU.GPA_CGPACALC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RegulationRepository regulationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        initializeRegulations();
        initializeDepartments();
        initializeAdminUser();
    }

    private void initializeRegulations() {

        if (regulationRepository.count() == 0) {

            Regulation r2017 = new Regulation();
            r2017.setName("R2017");
            r2017.setYear(2017);
            r2017.setDescription("Anna University Regulation 2017");

            Regulation r2021 = new Regulation();
            r2021.setName("R2021");
            r2021.setYear(2021);
            r2021.setDescription("Anna University Regulation 2021");

            regulationRepository.save(r2017);
            regulationRepository.save(r2021);

            System.out.println("Initialized regulations");
        }
    }

    private void initializeDepartments() {

        if (departmentRepository.count() == 0) {

            Regulation defaultReg = regulationRepository.findByName("R2021")
                    .orElse(regulationRepository.findAll().get(0));

            departmentRepository.save(createDept("Computer Science Engineering",
                    "CSE", "Department of CSE", defaultReg));

            departmentRepository.save(createDept("Information Technology",
                    "IT", "Department of IT", defaultReg));

            departmentRepository.save(createDept("Electronics and Communication Engineering",
                    "ECE", "Department of ECE", defaultReg));

            departmentRepository.save(createDept("Electrical and Electronics Engineering",
                    "EEE", "Department of EEE", defaultReg));

            departmentRepository.save(createDept("Mechanical Engineering",
                    "MECH", "Department of Mechanical Engineering", defaultReg));

            departmentRepository.save(createDept("Civil Engineering",
                    "CIVIL", "Department of Civil Engineering", defaultReg));

            System.out.println("Initialized departments");
        }
    }

    private Department createDept(String name, String code, String desc, Regulation regulation) {
        Department d = new Department();
        d.setName(name);
        d.setCode(code);
        d.setDescription(desc);
        d.setSemesterCount(8);  // default
        d.setRegulation(regulation);
        return d;
    }

    private void initializeAdminUser() {

        if (userRepository.findByEmail("admin@gpa.com").isEmpty()) {

            User admin = new User();
            admin.setName("Administrator");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("adminpassword"));
            admin.setRole(Role.ADMIN);
            admin.setVerified(true);

            userRepository.save(admin);



            System.out.println("Initialized admin: admin@gpa.com / admin123");
        }
    }
}
