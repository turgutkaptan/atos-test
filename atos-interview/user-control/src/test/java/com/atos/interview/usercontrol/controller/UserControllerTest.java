package com.atos.interview.usercontrol.controller;

import com.atos.interview.usercontrol.model.User;
import com.atos.interview.usercontrol.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    void userNotFoundGet() throws Exception {
        when(userService.getUser("turgut")).thenReturn(null);
        mockMvc.perform(get("/users/turgut")).andExpect(status().isNotFound()).andExpect(content().string("Username: turgut not found"));
    }

    @Test
    void userFoundGet() throws Exception {
        when(userService.getUser("turgut")).thenReturn(new User());
        mockMvc.perform(get("/users/turgut")).andExpect(status().isOk());
    }

    @Test
    void userMinorPost() throws Exception {
        User user = User.builder().username("turgut").birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("8/8/2011")).country("France").phoneNumber("0123").gender("male").build();
        mockMvc.perform(post("/users").content(new ObjectMapper().writeValueAsString(user))
        .contentType("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("The user must be an adult to be able to register"));
    }

    @Test
    void userNotInFrancePost() throws Exception {
        User user = User.builder().username("turgut").birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("8/8/2001")).country("Italy").phoneNumber("0123").gender("male").build();
        mockMvc.perform(post("/users").content(new ObjectMapper().writeValueAsString(user))
        .contentType("application/json"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("{\"country\":\"User must live in France\"}"));
    }

    @Test
    void userAdultPost() throws Exception {
        User user = User.builder().username("turgut").birthdate(new SimpleDateFormat("dd/MM/yyyy").parse("8/8/2001")).country("France").phoneNumber("0123").gender("male").build();
        mockMvc.perform(post("/users").content(new ObjectMapper().writeValueAsString(user))
        .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().string("User registered"));
    }

}
