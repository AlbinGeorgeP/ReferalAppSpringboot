package com.social.referral.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig  {


    private final JtwAuthFilter jtwAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http

                .csrf(csrf->csrf.disable()).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/*/v1/AuthService/**")
                .permitAll()
                .requestMatchers("/actuator")
                .permitAll()
                .requestMatchers("/*/v1/UtilityService/**").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/*/v1/UserService/**").hasAnyAuthority("ADMIN","USER")
                .anyRequest()
                .authenticated())
                .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jtwAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider);
        return http.build();
    }


}
