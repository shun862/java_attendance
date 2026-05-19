package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

      http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/login_view", "/login_check", "/create_user", "/create_user_success").permitAll()
              .anyRequest().authenticated()
          )
          .formLogin(login -> login
              .loginPage("/login_view")
              .loginProcessingUrl("/login")
              .defaultSuccessUrl("/user/attendance_list", true)
              .usernameParameter("username")
              .passwordParameter("password")
              .permitAll()
          )
          .logout(logout -> logout
              .logoutSuccessUrl("/login_view")
              .permitAll()
          );

      return http.build();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
  }
}
