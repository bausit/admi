package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.models.Participant;
import org.bausit.admin.services.ParticipantService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@Log4j2
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public Iterable<Participant> search(@RequestParam(required = false) String query) {
        log.info("search keywords: {}", query);

        return participantService.query(query);
    }

    @PostMapping("/preferences/{section}")
    public boolean savePreference(@PathVariable String section,
                                  @RequestBody Map preference,
                                  Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        log.info("saving {} preference: {}", section, preference);
        return participantService.updatePreference(user.getParticipant(), section, preference);
    }

    @GetMapping("/preferences/{section}")
    public Map getPreference(@PathVariable String section,
                             Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        log.info("getting {} preference", section);
        return participantService.getPreference(user.getParticipant(), section);
    }
}
