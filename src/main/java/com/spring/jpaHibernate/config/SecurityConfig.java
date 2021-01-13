package com.spring.jpaHibernate.config;

import com.spring.jpaHibernate.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedPage("/accessdenied");

        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/").permitAll(); //or hasRole("ROLE_ADMIN")

        http.formLogin()
                .loginProcessingUrl("/auth").permitAll() //or authenticated
                .loginPage("/login").permitAll()
                .usernameParameter("my_email")
                .passwordParameter("my_password")
                .successForwardUrl("/")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error");

        http.logout().permitAll()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/login").permitAll();

        http.csrf().disable(); //for safety

    }

    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
