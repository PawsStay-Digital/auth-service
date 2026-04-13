package com.pawsstay.auth_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String address;
    private LocalDate birthday;
    private String photoUrl;
}
