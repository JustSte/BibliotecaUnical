package com.Stefan.BibliotecaUnical.DTO.UserDTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String name;
    private String surname;
    private int studentID;
}
