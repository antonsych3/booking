package com.spring.jpaHibernate.repositories;

import com.spring.jpaHibernate.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository <Roles, Long> {
}
