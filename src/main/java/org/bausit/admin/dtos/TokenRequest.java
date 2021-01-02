package org.bausit.admin.dtos;

import lombok.Value;

@Value
public class TokenRequest {
    private String username;
    private String password;
}
