package com.mmg.app.controller;

import com.mmg.app.dto.AuthenticationRequest;
import com.mmg.app.dto.AuthenticationResponse;
import com.mmg.app.model.User;
import com.mmg.app.repository.UserRepository;
import com.mmg.app.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth") // Ensure this matches your route
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final String jwt = jwtUtil.generateToken(authenticationRequest.getUsername());
        User user = userRepository.findByUsername(authenticationRequest.getUsername());
                //.orElseThrow(() -> new Exception("User not found"));

        return new AuthenticationResponse(jwt, user.getId(), user.getUsername());
    }
}

