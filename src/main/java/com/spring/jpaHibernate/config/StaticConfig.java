package com.spring.jpaHibernate.config;

import com.spring.jpaHibernate.entities.Roles;

public class StaticConfig {
    public static final Roles ROLE_USER = new Roles(1L, "ROLE_USER", "User role");
    public static final Roles ROLE_ADMIN = new Roles(2L, "ROLE_ADMIN", "Admin role");
    public static final Roles ROLE_MODERATOR = new Roles(3L, "ROLE_MODERATOR", "Moderator role");

    public static final int PAGE_SIZE = 4;
}
