package com.atos.interview.usercontrol.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;

import com.atos.interview.usercontrol.model.User;
import com.atos.interview.usercontrol.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

    @InjectMocks
	private UserService userService;

    @Test
    void userNotFound() {
        when(userRepository.findByUsername("turgut")).thenReturn(new ArrayList<User>());
        assertEquals(null, userService.getUser("turgut"));
    }

    @Test
    void userFound() {
        User user = User.builder().username("turgut").build();
        when(userRepository.findByUsername("turgut")).thenReturn(Collections.singletonList(user));
        assertEquals(user, userService.getUser("turgut"));
    }

    @Test
    void userAdded() {
        User user = User.builder().username("turgut").build();
        userService.addUser(user);
		verify(userRepository, times(1)).save(user);
    }
    
}
