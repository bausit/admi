package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Event;
import org.bausit.admin.models.Participant;
import org.bausit.admin.models.Team;
import org.bausit.admin.services.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @GetMapping("/{eventId}/participants")
    public Set<Participant> getParticipants(@PathVariable long eventId) {
        Event event = eventService.findById(eventId);
        Set<Participant> participants = event.getAllParticipants();
        participants.forEach(Participant::initViewMode);
        return participants;
    }

    @GetMapping
    public Iterable<Event> search(@RequestParam(required = false) String query) {
        log.info("search keywords: {}", query);

        Iterable<Event> events = eventService.query(query);
        events.forEach(Event::initViewMode);
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
