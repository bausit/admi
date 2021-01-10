package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Participant;
import org.bausit.admin.services.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@Log4j2
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<Iterable<Participant>> search(@RequestParam(required = false) String query) {
        log.info("search keywords: {}", query);

        return ResponseEntity.ok(participantService.query(query));
    }
}
