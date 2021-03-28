package org.bausit.admin.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

    public enum Type {
        L, A, M
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @Enumerated(EnumType.STRING)
    Type type = Type.M;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private Instant checkinTime;
    private Instant checkoutTime;

    @Column(columnDefinition="TEXT")
    private String note;

    public void initViewMode() {
        this.team = null;
        if(getParticipant() != null)
            getParticipant().initViewMode();
    }
}
