package com.internship.mareshkiFinance.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String firstName;
    private String username;
    private String email;
    private String city;
    private String country;
    private String password;
}
