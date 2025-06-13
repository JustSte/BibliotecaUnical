package com.Stefan.BibliotecaUnical.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Users {

    public static enum Role{
        ROLE_ADMIN, ROLE_MANAGER, ROLE_BASE_USER,ROLE_GUEST
    }

    @Id
    @GeneratedValue
    private Long ID;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Email
    private String email;

    @NotBlank
    private int studentID;

/*    @OneToOne(mappedBy = "occupant")
    private Chair chair;*/
    Role role= Role.ROLE_GUEST;

}
