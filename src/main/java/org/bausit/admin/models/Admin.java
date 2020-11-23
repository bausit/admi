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
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String username;
    private String password;

    @ManyToMany
    @JoinTable(name = "admin_permission",
        inverseJoinColumns = @JoinColumn(name="permission_id", referencedColumnName = "id"),
        joinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id")
    )
    private List<Permission> permissions;
}
