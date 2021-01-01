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
public class FunctionMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private Function function;

    private Instant checkinTime;
    private Instant checkoutTime;

    @Column(columnDefinition="TEXT")
    private String note;
}
