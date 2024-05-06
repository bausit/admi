package org.bausit.admin.dtos;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Profile {
    private String chineseName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipcode;
}
