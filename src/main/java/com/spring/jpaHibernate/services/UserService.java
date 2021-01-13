package com.spring.jpaHibernate.services;

import com.spring.jpaHibernate.entities.Roles;
import com.spring.jpaHibernate.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {

    Users getUserByEmail(String email);
    Users registerUser(Users user);
    Users updateUser(Users user);
    List<Users> getAllUsers();
    List<Users> getAllUsersByRole(Roles role);
    List<Users> getAllUsersPaged(int currentPage, int length);
    int getUserSize(Roles role);
    Users getUserById(Long id);
}
