//package com.ecommerce.project.config;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig
//{
//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
//    {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // http.formLogin(withDefaults());
//        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
//        http.csrf(csrf -> csrf.disable());
//        // http.formLogin(withDefaults());
//        http.httpBasic(withDefaults()); // spring should use basic authentication with default settings
//        return http.build();
//    }
//
//    /*
//     * InMemory
//     *
//     * @Bean UserDetailsService userDetailsService() { UserDetails user1 =
//     * User.withUsername("user1").password("{noop}password1").roles("USER").build();
//     * UserDetails admin =
//     * User.withUsername("admin").password("{noop}test").roles("ADMIN").build();
//     * return new InMemoryUserDetailsManager(user1, admin); }
//     */
//
//    // storing the credentials into the database
//    @Bean
//    public UserDetailsService userDetailsService()
//    {
//        UserDetails user1 = User.builder().username("user1").password(passwordEncoder().encode("password1"))
//                .roles("USER").build();
//
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("test")).roles("ADMIN")
//                .build();
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        userDetailsManager.createUser(user1);
//        userDetailsManager.createUser(admin);
//        return userDetailsManager;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder()
//    {
//        return new BCryptPasswordEncoder();
//    }
//
//}
