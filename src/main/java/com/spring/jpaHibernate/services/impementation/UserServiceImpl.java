package com.spring.jpaHibernate.services.impementation;

import com.spring.jpaHibernate.config.StaticConfig;
import com.spring.jpaHibernate.entities.Roles;
import com.spring.jpaHibernate.entities.Users;
import com.spring.jpaHibernate.repositories.UserRepository;
import com.spring.jpaHibernate.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableWebSecurity
public class UserServiceImpl implements UserService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users myUser = userRepository.findByEmail(email);

        if (myUser!=null) {
            User securityUser = new User(myUser.getEmail(), myUser.getPassword(), myUser.getRoles());
            return securityUser;
        }else {
           throw new UsernameNotFoundException("");
        }
        //Or
        //throw UsernameNotFoundException("str message")
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users registerUser(Users user) {
        Users checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser==null){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findUsersByRolesContains(StaticConfig.ROLE_USER);
    }

    @Override
    public List<Users> getAllUsersByRole(Roles role) {
        return userRepository.findUsersByRolesContains(role);
    }

    @Override
    public List<Users> getAllUsersPaged(int currentPage, int length) {
        Pageable pageable = PageRequest.of(currentPage, length);
        return userRepository.findUsersByRolesContains(StaticConfig.ROLE_USER, pageable);
    }

    @Override
    public int getUserSize(Roles role) {
        return userRepository.countAllByRolesContains(role);
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findUsersById(id);
    }
}
