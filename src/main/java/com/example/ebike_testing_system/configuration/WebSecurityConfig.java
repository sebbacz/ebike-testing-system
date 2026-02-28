package com.example.ebike_testing_system.configuration;

import com.example.ebike_testing_system.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/auth/signup", "/auth/process_signup", "/auth/login",
                                        "/forgot_password", "/reset_password",
                                        "/css/**", "/api/**", "/api/test"
                                ).permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/qr-code/generate/**").permitAll()
                                .requestMatchers("/bike/test-history/**").permitAll()
                                .requestMatchers("/qr-code/data/**").permitAll()
                                .requestMatchers("/technician/**").hasAnyRole("TECHNICIAN", "SUPERADMIN")
                                .requestMatchers("/admin/dashboard").hasAnyRole("SUPERADMIN", "ADMIN")
                                .requestMatchers("/admin/dashboard/**").hasAnyRole("SUPERADMIN", "ADMIN")
                                .requestMatchers("/admin/approve").hasAnyRole("SUPERADMIN")
                                .requestMatchers("/superadmin/dashboard").hasAnyRole("SUPERADMIN")
                                .requestMatchers("/superadmin/**").hasAnyRole("SUPERADMIN")
                                .requestMatchers("/api/**").hasAnyRole("SUPERADMIN", "ADMIN", "TECHNICIAN")
                                .requestMatchers("/qr-code/data/**").permitAll()
                                .requestMatchers("/qr-code/generate/**").permitAll()
                                .requestMatchers("/qr-code/generate-and-email/**").hasAnyRole("SUPERADMIN", "ADMIN") // Restricted access for email functionality
                                .anyRequest().permitAll()
                )

                .formLogin(login ->
                        login.loginPage("/auth/login").usernameParameter("email")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/auth/login?error=true")
                                .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(
                                        (request, response, exception) -> {
                                            if (request.getRequestURI().startsWith("/api")) {
                                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                            } else if (request.getRequestURI().startsWith("/error")) {
                                                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                                            } else {
                                                response.sendRedirect(request.getContextPath() + "/auth/login");
                                            }
                                        })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.sendRedirect(request.getContextPath() + "/auth/access-denied");
                                })
                );

        return http.build();
    }
}