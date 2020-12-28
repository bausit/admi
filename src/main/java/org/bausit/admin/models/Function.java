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
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(name = "function_skill",
        inverseJoinColumns = @JoinColumn(name="skill_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "function_id", referencedColumnName = "id")
    )
    //skills that are useful for this function, will be used to look up member candidates
    private List<Skill> skills;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @OneToMany(mappedBy = "function")
    private List<FunctionMember> members;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private Member leader;
}
