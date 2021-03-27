package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Event;
import org.bausit.admin.models.Team;
import org.bausit.admin.services.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Log4j2
public class EventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable long eventId) {
        Event event = eventService.findById(eventId);
        event.initViewMode();
        return event;
    }

    @GetMapping
    public Iterable<Event> search(@RequestParam(required = false) String query,
                                  @RequestParam(required = false, defaultValue = "false") boolean slim) {
        log.info("search keywords: {}", query);

        Iterable<Event> events = eventService.query(query);
        if(slim) {
            events.forEach(event -> event.setTeams(null)
                .setInvitedParticipants(null)
            );
        }

        return events;
    }

    @PostMapping("/invite/{eventId}")
    public void invite(@PathVariable long eventId, @RequestBody List<Long> participants) {
        Event event = eventService.findById(eventId);
        eventService.invite(event, participants);
    }

    @PostMapping("/assign/{eventId}/{teamId}")
    public void assignTeamMember(@PathVariable long eventId,
                              @PathVariable long teamId,
                              @RequestBody List<Long> participants) {
        Event event = eventService.findById(eventId);
        Team team = eventService.findTeam(event, teamId);
        eventService.assignTeamMember(event, team, participants);
    }
}
