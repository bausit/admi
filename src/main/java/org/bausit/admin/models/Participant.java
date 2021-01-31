package org.bausit.admin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String chineseName;
    private String englishName;
    private Instant issueDate;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String email;

    @JsonIgnore
    private String password;
    private String phoneNumber;
    private int birthYear;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean refuge;
    private String address;
    private String city;
    private String state;
    private String zipcode;

    public enum Type {
        V, B, M
    }

    public enum Gender {
        F, M
    }

    @ManyToMany
    @JoinTable(name = "participant_skill",
        inverseJoinColumns = @JoinColumn(name="skill_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id")
    )
    private List<Skill> skills;

    @Column(columnDefinition="TEXT")
    private String note;

    @ManyToMany
    @JoinTable(name = "participant_permission",
        inverseJoinColumns = @JoinColumn(name="permission_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id")
    )
    private List<Permission> permissions;

    @Column(columnDefinition="TEXT")
    private String preferences;

    public boolean equals(Participant p) {
        return p.getId() == p.getId();
    }
}
