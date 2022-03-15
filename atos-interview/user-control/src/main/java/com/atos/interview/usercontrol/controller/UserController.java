package com.atos.interview.usercontrol.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.atos.interview.usercontrol.model.User;
import com.atos.interview.usercontrol.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.validation.FieldError;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get a single user
    @GetMapping("/{username}")
    public Object getUser(@PathVariable String username) {
        User user = new User();
        user.setUsername(username);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> errors = validator.validateProperty(user, "username");
        if (errors.isEmpty()) {
            user = userService.getUser(username);
            if (user == null) {
                return new ResponseEntity<String>("Username: " + username + " not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(errors.stream().findFirst().get().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    // Add a user
    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        Date birthdate = user.getBirthdate();
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");                           
        int d1 = Integer.parseInt(formatter.format(birthdate));                            
        int d2 = Integer.parseInt(formatter.format(new Date()));                          
        int age = (d2 - d1) / 10000; 
        if (age < 18) {
            return new ResponseEntity<String>("The user must be an adult to be able to register",
                    HttpStatus.BAD_REQUEST);
        }
        userService.addUser(user);
        log.info("User is registered: " + user);
        return new ResponseEntity<String>("User registered", HttpStatus.OK);
    }

    // Handle input validation errors => Bad requests
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
