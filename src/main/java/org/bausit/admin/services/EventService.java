package org.bausit.admin.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.bausit.admin.exceptions.EntityNotFoundException;
import org.bausit.admin.models.Event;
import org.bausit.admin.models.QEvent;
import org.bausit.admin.models.Team;
import org.bausit.admin.models.TeamMember;
import org.bausit.admin.repositories.EventRepository;
import org.bausit.admin.repositories.TeamMemberRepository;
import org.bausit.admin.search.PredicatesBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantService participantService;
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
        participants.stream()
            .map(id -> participantService.findById(id))
            //don't invite if participant already joined the event
            .filter(participant -> !event.hasParticipant(participant))
            .forEach(participant -> event.getInvitedParticipants().add(participant));

        eventRepository.save(event);
    }

    public Team findTeam(Event event, long teamId) {
        return event.getTeams()
            .stream()
            .filter(team -> team.getId() == teamId)
            .findAny()
            .orElseThrow(() -> new EntityNotFoundException("Unable to find team with id: " + teamId));
    }

    @Transactional
    public void assignTeamMember(Event event, Team team, List<Long> participants) {
        participants.stream()
            .map(id -> participantService.findById(id))
            .forEach(participant -> {
                if(!team.isMember(participant)) {
                    TeamMember member = TeamMember.builder()
                        .participant(participant)
                        .team(team)
                        .build();
                    member = teamMemberRepository.save(member);
                    team.getMembers().add(member);
                }

                event.getInvitedParticipants().remove(participant);
            });
    }
}
