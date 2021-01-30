package org.bausit.admin.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Event;
import org.bausit.admin.services.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Log4j2
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Iterable<Event> search(@RequestParam(required = false) String query) {
        log.info("search keywords: {}", query);

        return eventService.query(query);
    }
}
