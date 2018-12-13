package com.xavient.service.service;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xavient.service.data.Role;
import com.xavient.service.data.User;
import com.xavient.service.data.UserRepository;
import com.xavient.service.model.NewUser;
import com.xavient.service.model.Registration;

@Service
public class UserService {
    public static final String USER_ROLE = "user";
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private Registration registration;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService, Registration registration) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.registration = registration;
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public String save(String email, String username, String password) {
        if (repository.findByEmail(email).isPresent()) {
            throw new UsernameTakenException("Username is already taken");
        }
        Role role = new Role(email, USER_ROLE);
        User user = repository.saveAndFlush(new User(email, username, passwordEncoder.encode(password), true, Collections.singletonList(role)));
        return tokenService.encode(user);
    }

    @PostConstruct
    public void register() {
        registration.newUserRequest().subscribe(message -> {
            NewUser payload = (NewUser) message.getPayload();
            save(payload.getEmail(), payload.getUsername(), payload.getPassword());
        });
    }
}
