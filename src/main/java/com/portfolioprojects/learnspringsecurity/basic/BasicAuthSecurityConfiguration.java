package com.portfolioprojects.learnspringsecurity.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
//@EnableMethodSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class BasicAuthSecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/users").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated());

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.formLogin();
        http.httpBasic();
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        var user = User.withUsername("Robert").password("{noop}password")
//                .roles("USER").build();
//
//        var admin = User.withUsername("admin").password("{noop}password")
//                .roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
        System.out.println(passwordEncoder);
        var user = User.withUsername("Robert")
//                .password("{noop}password")
                .password("password")
                .passwordEncoder(str -> passwordEncoder.encode(str))
                .roles("USER")
                .build();


        var admin = User.withUsername("admin")
//                .password("{noop}password")
                .password("password")
                .passwordEncoder(str -> passwordEncoder.encode(str))
                .roles("ADMIN")
                .build();

        var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.createUser(user);
        jdbcUserDetailsManager.createUser(admin);

        return jdbcUserDetailsManager;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
