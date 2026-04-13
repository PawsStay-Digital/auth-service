package com.pawsstay.auth_service.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class OwnerCreateRequest {
    private String email;
    private String name;
}
