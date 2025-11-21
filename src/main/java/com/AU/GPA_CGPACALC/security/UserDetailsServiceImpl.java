package com.AU.GPA_CGPACALC.security;

import com.AU.GPA_CGPACALC.entity.User;
import com.AU.GPA_CGPACALC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // MOST IMPORTANT FIX 🔥
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(user.getRole().name());  // "ADMIN" or "STUDENT"

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(authority)
        );
    }
}
