package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;


@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails user = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        var paths = List.of("/", "/static/**", "/*.png", "/favicon.ico", "/manifest.json", "/actuator/**");
        var openEndpoints = paths.stream()
                .map(AntPathRequestMatcher::new)
                .toList().toArray(new RequestMatcher[paths.size()]);

        http
            .securityContext( securityContext -> securityContext.requireExplicitSave(false))
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers(openEndpoints).permitAll()
                    .anyRequest().authenticated()
            )
//            .sessionManagement(session -> session
//                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//            )
            .httpBasic()
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .accessDeniedHandler((req, resp, e) -> e.printStackTrace() )
                .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
