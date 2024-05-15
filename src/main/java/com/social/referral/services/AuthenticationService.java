package com.social.referral.services;

import com.social.referral.config.JwtUtil;
import com.social.referral.dto.AuthenticationRequest;
import com.social.referral.dto.AuthenticationResponse;
import com.social.referral.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
    final UserDetails user = userRepository.getUserByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("The requested user is not found "));


            return AuthenticationResponse.builder().accessToken(jwtUtil.generateToken(user)).user(user.getUsername()).build();


    }
}
