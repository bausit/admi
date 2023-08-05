package org.bausit.admin.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.exceptions.EntityNotFoundException;
import org.bausit.admin.exceptions.InvalidRequestException;
import org.bausit.admin.models.*;
import org.bausit.admin.repositories.CheckinRepository;
import org.bausit.admin.repositories.EventRepository;
import org.bausit.admin.repositories.TeamMemberRepository;
import org.bausit.admin.repositories.TeamRepository;
import org.bausit.admin.search.PredicatesBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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
    private final CheckinRepository checkinRepository;

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

    public Participant checkin(Event event, Participant participant, Instant checkoutDate) {
        //check if participant was invited
        boolean invited = event.getAllParticipants()
            .stream()
            .anyMatch(p -> p.getId() == participant.getId());

        if (!invited) {
            throw new InvalidRequestException("Participant was not invited");
        }

        Checkin checkin = Checkin.builder()
            .participant(participant)
            .event(event)
            .checkinDate(Instant.now())
            .checkoutDate(checkoutDate)
            .build();

        checkinRepository.save(checkin);

        return participant;
    }

    public List<Event> findTodayEvent() {
        Instant start = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).toInstant(ZoneOffset.UTC);
        Instant end = start.plus(Duration.ofDays(2));
        log.info("Find events between {} and {}", start, end);
        return eventRepository.findByDateBetween(start, end);
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

    public void delete(long eventId) {
        Event event = this.findById(eventId);
        log.info("deleting event: {}", event.getName());

        event.getTeams()
            .stream()
            .forEach(team -> {
                team.getMembers().stream()
                    .forEach(teamMember -> teamMemberRepository.delete(teamMember));
                teamRepository.delete(team);
            });
        eventRepository.delete(event);
    }

    public void deleteTeam(long teamId) {
        teamRepository.deleteById(teamId);
    }
}
