package org.bausit.admin.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String name;
    private String location;
    private Instant date;

    @OneToMany(mappedBy = "event")
    private List<Team> teams;

    @ManyToMany
    @JoinTable(name = "event_participant",
        inverseJoinColumns = @JoinColumn(name="participant_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    private List<Participant> invitedParticipants;
}
