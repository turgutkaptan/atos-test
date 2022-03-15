package com.atos.interview.usercontrol.service;

import com.atos.interview.usercontrol.model.User;
import com.atos.interview.usercontrol.repository.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String username) {
        return userRepository.findByUsername(username).stream().filter(user -> user.getUsername().equals(username)).findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

}
