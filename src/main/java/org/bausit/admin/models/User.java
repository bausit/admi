package org.bausit.admin.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ZYMUser_6")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userRecordId")
    private long id;

    @Column(name = "userEmailId")
    private String userEmailId;

    @Column(name = "userPassword")
    private String userPassword;

    @Column(name = "firstNmae")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "dOb__date__")
    private long dOb__date__;

    @Column(name = "defaultAddress")
    private String defaultAddress;

    @Column(name = "sex")
    private int sex;//0 female, 1 male

    @Column(name = "refuge")
    private int refuge;//0 no, 1 yes

    @Column(name = "activited")
    private int activated; // 0,1

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "specialty")
    private String specialty;
}
