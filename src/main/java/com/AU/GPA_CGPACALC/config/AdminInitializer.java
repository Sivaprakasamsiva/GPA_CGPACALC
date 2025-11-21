package com.AU.GPA_CGPACALC.config;

import com.AU.GPA_CGPACALC.entity.Role;
import com.AU.GPA_CGPACALC.entity.User;
import com.AU.GPA_CGPACALC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")  // ⭐ Only run in development, NOT in Render
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@events.com";
        String adminPassword = "admin123";

        if (userRepository.existsByEmail(adminEmail)) {
            System.out.println("✔ Admin already exists. Skipping creation.");
            return;
        }

        User admin = new User();
        admin.setName("Administrator");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setVerified(true);
        admin.setRegulation(null);
        admin.setDepartment(null);

        userRepository.save(admin);

        System.out.println("✔ Admin user created successfully!");
    }
}
