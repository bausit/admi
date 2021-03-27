package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.dtos.TokenRequest;
import org.bausit.admin.dtos.TokenResponse;
import org.bausit.admin.services.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    //generate jwt tokens to be used as authorization header in future requests
    @PostMapping
    public TokenResponse authenticateUser(@RequestBody TokenRequest tokenRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(), tokenRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(authentication);

        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return TokenResponse.builder()
            .token(jwt)
            .id(userDetails.getParticipant().getId())
            .name(userDetails.getParticipant().getEnglishName())
            .email(userDetails.getUsername())
            .roles(roles)
            .build();
    }
}
