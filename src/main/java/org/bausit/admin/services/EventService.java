package org.bausit.admin.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.exceptions.EntityNotFoundException;
import org.bausit.admin.models.*;
import org.bausit.admin.repositories.EventRepository;
import org.bausit.admin.repositories.TeamMemberRepository;
import org.bausit.admin.repositories.TeamRepository;
import org.bausit.admin.search.PredicatesBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

@Service
@RequiredArgsConstructor
@Log4j2
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantService participantService;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public Iterable<Event> query(String query) {
        PredicatesBuilder builder = new PredicatesBuilder(Event.class);
        QEvent event = QEvent.event;

        if (StringUtils.hasLength(query)) {
            Matcher matcher = PredicatesBuilder.PATTERN.matcher(query + ",");
            while (matcher.find()) {
                Object value = matcher.group(3);
                String name = matcher.group(1);
                builder.with(name, matcher.group(2), value);
            }
        }
        BooleanExpression exp = builder.build();
        return eventRepository.findAll(exp);
    }

    public Event findById(long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new EntityNotFoundException("Unable to find events with id: " + eventId));
    }

    @Transactional
    public void invite(Event event, List<Long> participants) {
        Team team = event.getDefaultTeam();
        Team defaultTeam = team.getId() == 0 ?
            teamRepository.save(team) :
            team;

        participants.stream()
            .map(id -> participantService.findById(id))
            //don't invite if participant already joined the event
            .filter(participant -> !event.hasParticipant(participant))
            .forEach(participant -> defaultTeam.addMember(participant)
                .ifPresent(teamMember -> teamMemberRepository.save(teamMember))
            );
    }

    public Team findTeam(Event event, long teamId) {
        return event.getTeams()
            .stream()
            .filter(team -> team.getId() == teamId)
            .findAny()
            .orElseThrow(() -> new EntityNotFoundException("Unable to find team with id: " + teamId));
    }

    @Transactional
    public Optional<TeamMember> removeTeamMember(Event event, Team team, Long participantId) {
        return team.getMember(participantId)
            .map(teamMember -> {
                teamMember.setTeam(event.getDefaultTeam());
                teamMemberRepository.save(teamMember);
                return teamMember;
            });
    }

    @Transactional
    public void assignTeamMember(Event event, Team team, List<Long> participants) {
        participants.stream()
            .map(id -> event.getDefaultTeam().getMember(id))
            .forEach(teamMemberOptional -> {
                teamMemberOptional.ifPresent(teamMember -> {
                    teamMember.setTeam(team);
                    teamMemberRepository.save(teamMember);
                });
            });
    }
}
