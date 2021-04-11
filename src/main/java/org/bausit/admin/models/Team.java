package org.bausit.admin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(name = "team_skill",
        inverseJoinColumns = @JoinColumn(name="skill_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id")
    )
    //skills that are useful for this function, will be used to look up member candidates
    private List<Skill> skills;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore//prevent json recursion
    @EqualsAndHashCode.Exclude
    private Event event;

    @OneToMany(mappedBy = "team")
    @EqualsAndHashCode.Exclude
    private Set<TeamMember> members;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private TeamMember leader;

    public boolean hasParticipant(Participant participant) {
        return members.stream()
            .filter(teamMember -> teamMember.getParticipant().getId() == participant.getId())
            .findAny()
            .isPresent();
    }

    @JsonIgnore
    public Set<Participant> getParticipants() {
        return members.stream()
            .map(TeamMember::getParticipant)
            .collect(Collectors.toSet());
    }

    public Optional<TeamMember> addMember(Participant participant) {
        if(!hasParticipant(participant)) {
            TeamMember member = TeamMember.builder()
                .participant(participant)
                .type(TeamMember.Type.M)
                .team(this)
                .build();
            getMembers().add(member);
            return Optional.of(member);
        }

        return Optional.empty();
    }

    public Optional<TeamMember> getMember(long participantId) {
        return getMembers().stream()
            .filter(teamMember -> teamMember.getParticipant().getId() == participantId)
            .findAny();
    }

    public void initViewMode() {
        if(getMembers() != null)
            this.getMembers().forEach(member -> member.initViewMode());
        if(getLeader() != null)
            this.getLeader().initViewMode();
    }

    public boolean isDefault() {
        return Event.DEFAULT_TEAM_NAME.equals(getName());
    }
}
