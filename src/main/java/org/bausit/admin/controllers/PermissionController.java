package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.models.Participant;
import org.bausit.admin.services.ParticipantService;
import org.bausit.admin.services.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final ParticipantService participantService;
    private final PermissionService permissionService;

    @GetMapping("/administrators")
    public List<Participant> getAdministrators(@RequestParam(required = false) String query) {
        return StreamSupport.stream(participantService.query(query).spliterator(), false)
            .filter(participant -> participant.getPermissions().size() > 0)
            .collect(Collectors.toList());
    }
}
