package dev.pack.dtos.request;

import dev.pack.enums.Major;
import dev.pack.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserCreateDto{

        @NotEmpty(message = "Name cannot be empty!")
        private String name;

        @NotEmpty(message = "Email cannot be empty!")
        @Email(message = "Email pattern doesn't valid!")
        private String email;

        @NotEmpty(message = "Username cannot be empty!")
        private String username;

        @NotEmpty(message = "Password cannot be empty!")
        private String password;

        @NotNull(message = "Role cannot be null")
        private Roles role;

//        @NotNull(message = "Jurusan cannot be null!")
//        private Major major;
    }

}
