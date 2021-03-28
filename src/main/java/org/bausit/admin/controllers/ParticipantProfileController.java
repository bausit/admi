package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.dtos.UpdatePasswordRequest;
import org.bausit.admin.models.Participant;
import org.bausit.admin.services.ParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ParticipantProfileController {
    private final PasswordEncoder passwordEncoder;
    private final ParticipantService participantService;

    @PostMapping("/password")
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequest request,
                               Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Participant participant = user.getParticipant();

        if(!passwordEncoder.matches(request.getCurrentPassword(), participant.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong current password");
        }
        if(!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New passwords don't match");
        }

        log.info("updating password");
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        participant.setPassword(encodedPassword);
        participantService.save(participant);
    }
}
