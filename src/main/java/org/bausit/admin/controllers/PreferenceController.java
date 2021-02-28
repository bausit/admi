package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.services.ParticipantService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
@Log4j2
public class PreferenceController {
    private final ParticipantService participantService;

    @PostMapping("/{section}")
    public boolean savePreference(@PathVariable String section,
                                  @RequestBody Map preference,
                                  Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        log.info("saving {} preference: {}", section, preference);
        return participantService.updatePreference(user.getParticipant(), section, preference);
    }

    @GetMapping("/{section}")
    public Map getPreference(@PathVariable String section,
                             Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        log.info("getting {} preference", section);
        return participantService.getPreference(user.getParticipant(), section);
    }
}
