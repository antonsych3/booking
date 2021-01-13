package com.spring.jpaHibernate.repositories;

import com.spring.jpaHibernate.entities.Roles;
import com.spring.jpaHibernate.entities.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);
    List<Users> findUsersByRolesContains(Roles role);
    List<Users> findUsersByRolesContains(Roles role, Pageable pageable);
    int countAllByRolesContains(Roles role);
    Users findUsersById(Long id);


}
