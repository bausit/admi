package org.bausit.admin.controllers;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Participant;
import org.bausit.admin.repositories.ParticipantRepository;
import org.bausit.admin.search.ParticipantPredicatesBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@Log4j2
public class ParticipantController {
    private final ParticipantRepository participantRepository;

    @GetMapping
    public ResponseEntity<Iterable<Participant>> search(@RequestParam(required = false) String query) {
        log.info("search keywords: {}", query);
        ParticipantPredicatesBuilder builder = new ParticipantPredicatesBuilder();
        if (StringUtils.hasLength(query)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(query + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build();
        return ResponseEntity.ok(participantRepository.findAll(exp));
    }
}
