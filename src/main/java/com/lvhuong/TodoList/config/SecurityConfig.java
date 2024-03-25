package com.lvhuong.TodoList.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.function.Function;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager() {
        UserDetails userDetails1 = createNewUser("admin1", "secret1");

        return new InMemoryUserDetailsManager(userDetails1);
    }

    private UserDetails createNewUser(String username, String password) {
        Function<String, String> passwordEncoder = input -> passwordEncoder().encode(input);

        return User.builder()
                .passwordEncoder(passwordEncoder)
                .username(username)
                .password(password)
                .roles("USER", "ADMIN")
                .build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        DefaultSecurityFilterChain filterChain = http.authorizeHttpRequests(
            auth -> {
                auth.requestMatchers("/").permitAll();
                auth.requestMatchers("/home").permitAll();
                auth.requestMatchers("/profile").authenticated();
                auth.requestMatchers("/todo-list").authenticated();
                auth.requestMatchers("/admin").hasRole("ADMIN");
                auth.requestMatchers("/login").permitAll();
                auth.requestMatchers("/logout").permitAll();
                auth.requestMatchers("/css/**").permitAll();
                auth.requestMatchers("/js/**").permitAll();

            })
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> {
                form.loginPage("/login")
                    .loginProcessingUrl("/api/auth/login")
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/login?error=true")
                    .failureHandler(authenticationFailureHandler());

            })
            .logout(logout -> {
                logout.logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/home");
            })
            .build();


        return filterChain;
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return null;
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return null;
    }
}
