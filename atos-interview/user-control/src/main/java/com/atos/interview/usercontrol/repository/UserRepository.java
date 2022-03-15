package com.atos.interview.usercontrol.repository;

import java.util.List;

import com.atos.interview.usercontrol.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUsername(String username);

}
