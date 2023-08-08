package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.dtos.TokenRequest;
import org.bausit.admin.dtos.TokenResponse;
import org.bausit.admin.services.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    //generate jwt tokens to be used as authorization header in future requests
    @PostMapping
    public TokenResponse authenticateUser(@RequestBody TokenRequest tokenRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(), tokenRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenService.generateToken((SecurityUser) authentication.getPrincipal());
    }

    @GetMapping("/key")
    public String getKey() {
        return tokenService.getPublicKey();
    }
}
