package org.bausit.admin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private int memberNumber;

    private String chineseName;
    private String firstName;
    private String lastName;
    private Instant issueDate;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String email;

    @JsonIgnore
    private String password;
    private String phoneNumber;
    private int birthYear;
    private Instant birthDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean refuge;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String emergencyContact;

    public enum Type {
        V, B, M
    }

    public enum Gender {
        F, M
    }

    public enum Status {
        A, I
    }

    @ManyToMany
    @JoinTable(name = "participant_skill",
        inverseJoinColumns = @JoinColumn(name="skill_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id")
    )
    private List<Skill> skills;

    @Column(columnDefinition="TEXT")
    private String note;

    @Column(columnDefinition="TEXT")
    private String remark;

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

    public String getSkillsAsString() {
        if(getSkills() == null)
            return "";

        return getSkills()
            .stream()
            .map(skill -> skill.getName())
            .collect(Collectors.joining(", "));
    }

    public String getPermissionsAsString() {
        if(getPermissions() == null)
            return "";

        return getPermissions()
            .stream()
            .map(permission -> permission.getName())
            .collect(Collectors.joining(", "));
    }

    public void initViewMode() {
        this.permissions = null;
        this.preferences = null;
        this.note = null;
    }

    public String getEnglishName() {
        return firstName + ' ' + lastName;
    }
}
