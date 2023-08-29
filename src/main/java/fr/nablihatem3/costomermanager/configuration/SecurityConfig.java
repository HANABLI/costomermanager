package fr.nablihatem3.costomermanager.configuration;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import fr.nablihatem3.costomermanager.handler.CustomerAccessDeniedHandler;
import fr.nablihatem3.costomermanager.handler.CustomerAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 23/08/2023
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final BCryptPasswordEncoder encoder;
    private final CustomerAccessDeniedHandler customerAccessDeniedHandler;
    private final CustomerAuthenticationEntryPoint customerAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private static final String[] PUBLIC_URLS = {"/user/login/**", "/user/verify/code/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.sessionManagement(management -> management.sessionCreationPolicy(STATELESS));
        http.authorizeHttpRequests().requestMatchers(PUBLIC_URLS).permitAll();
        http.authorizeHttpRequests().requestMatchers(DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER");
        http.authorizeHttpRequests().requestMatchers(DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER");
        http.exceptionHandling().accessDeniedHandler(customerAccessDeniedHandler).authenticationEntryPoint(customerAuthenticationEntryPoint);
        http.authorizeHttpRequests().anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authenticationProvider);
    }
}
