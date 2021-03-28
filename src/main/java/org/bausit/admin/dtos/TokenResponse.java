package org.bausit.admin.dtos;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TokenResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String name;
    private String email;
    private List<String> roles;
}
