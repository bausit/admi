package org.bausit.admin.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Checkin {
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name="participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    private Instant checkinDate;
    private Instant checkoutDate;

    public Checkin initViewMode() {
        this.event = null;
        this.participant = participant.initViewMode();
        return this;
    }
}
