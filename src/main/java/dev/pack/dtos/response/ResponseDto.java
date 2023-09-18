package dev.pack.dtos.response;

import dev.pack.enums.Major;
import dev.pack.enums.Roles;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

public class ResponseDto {

    @Getter
    @Builder
    public static class UserResponseDto{
        private String name;
        private String email;
        private String username;
        private Roles role;
        private Major major;
        private Date tanggalBergabung;
    }

}
