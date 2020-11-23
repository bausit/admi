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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String chineseName;
    private String englishName;
    private Instant issueDate;
    private Type type;
    private String email;
    private String password;
    private String phoneNumber;
    private int birthYear;
    private Gender gender;
    private boolean refuge;
    private String address;
    private String city;
    private String state;
    private String zipcode;

    public enum Type {
        V, M
    }

    public enum Gender {
        F, M
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "member_skill",
        inverseJoinColumns = @JoinColumn(name="skill_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id")
    )
    List<Skill> skills;

    @OneToMany(mappedBy = "member")
    private List<Note> notes;

    @OneToMany(mappedBy = "member")
    private List<ActivityMember> activities;

}
