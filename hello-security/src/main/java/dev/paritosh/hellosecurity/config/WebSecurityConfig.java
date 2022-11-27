package dev.paritosh.hellosecurity.config;

import dev.paritosh.hellosecurity.handlers.CustomAccessDeniedHandler;
import dev.paritosh.hellosecurity.handlers.CustomAuthenticationFailureHandler;
import dev.paritosh.hellosecurity.handlers.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles(USER_ROLE)
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(USER_ROLE, ADMIN_ROLE)
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/home", "/access-denied").permitAll()
                .antMatchers("/admin").hasAnyRole(ADMIN_ROLE)
                .anyRequest().hasAnyRole(USER_ROLE)
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler())
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .logout()
                .permitAll();
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}