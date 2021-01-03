package org.bausit.admin.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    private Event event;

    @OneToMany(mappedBy = "team")
    private List<TeamMember> members;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private Participant leader;
}
