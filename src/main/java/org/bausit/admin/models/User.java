package org.bausit.admin.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="ZYMUser_6")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userRecordId;

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
