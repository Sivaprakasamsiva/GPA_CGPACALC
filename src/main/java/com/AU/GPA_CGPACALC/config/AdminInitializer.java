package com.AU.GPA_CGPACALC.config;

import com.AU.GPA_CGPACALC.entity.Role;
import com.AU.GPA_CGPACALC.entity.User;
import com.AU.GPA_CGPACALC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@events.com";
        String adminPassword = "admin123";

        // Check if admin already exists
        if (userRepository.existsByEmail(adminEmail)) {
            System.out.println("✔ Admin already exists. Skipping creation.");
            return;
        }

        // Create new admin
        User admin = new User();
        admin.setName("Administrator");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setVerified(true);
        admin.setRegulation(null);  // Admin should not require regulation
        admin.setDepartment(null);  // Admin should not require department

        userRepository.save(admin);

        System.out.println("✔ Admin user created successfully!");
        System.out.println("   Email: " + adminEmail);
        System.out.println("   Password: " + adminPassword);
        System.out.println("   Role: ADMIN");
    }
}
