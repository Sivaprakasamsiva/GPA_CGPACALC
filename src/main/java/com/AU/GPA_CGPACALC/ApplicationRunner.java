package com.AU.GPA_CGPACALC;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🎉 GPA Calculator Application Started Successfully!");
        System.out.println("📍 API Available at: http://localhost:8081");
        System.out.println("🔗 Test endpoint: http://localhost:8081/api/test");
        System.out.println("👤 Admin login: admin@gpa.com / admin123");
    }
}