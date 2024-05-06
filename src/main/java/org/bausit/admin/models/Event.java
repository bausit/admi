package org.bausit.admin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;



import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Log4j2
public class Event {
    public static final String DEFAULT_TEAM_NAME = "DEFAULT";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String name;
    private String location;
    private Instant date;

    @OneToMany(mappedBy = "event")
    private List<Team> teams;

    @OneToMany(mappedBy = "event")
    private List<Checkin> checkins;

    @ManyToMany
    @JoinTable(name = "event_participant",
        inverseJoinColumns = @JoinColumn(name="participant_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    private Set<Participant> invitedParticipants;

    public boolean hasParticipant(Participant participant) {
        if(getInvitedParticipants().contains(participant))
            return true;

        return getTeams().stream()
            .filter(team -> team.hasParticipant(participant))
            .findAny()
            .isPresent();
    }

    @JsonIgnore
    public Set<Participant> getAllParticipants() {
        Set<Participant> allParticipants = new HashSet<>();
        allParticipants.addAll(getInvitedParticipants());
        getTeams().forEach(team -> allParticipants.addAll(team.getParticipants()));
        return allParticipants;
    }

    public void initViewMode() {
        if(getTeams() != null)
            getTeams().forEach(team -> team.initViewMode());

        if(getInvitedParticipants() != null)
            getInvitedParticipants().forEach(participant -> participant.initViewMode());

        if(getCheckins() != null)
            getCheckins().forEach(checkin -> checkin.initViewMode());
    }

    @JsonIgnore
    public Team getDefaultTeam() {
        return getTeams().stream()
            .filter(team -> DEFAULT_TEAM_NAME.equals(team.getName()))
            .findAny()
            .orElseGet(() -> {
                Team team =Team.builder()
                        .name(DEFAULT_TEAM_NAME)
                        .event(this)
                        .members(new HashSet<>())
                        .build();
                getTeams().add(team);
                return team;
            });
    }
}
