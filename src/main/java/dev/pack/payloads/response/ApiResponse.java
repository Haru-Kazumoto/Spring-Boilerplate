package dev.pack.payloads.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.pack.utils.CustomDateSerializer;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

    private Integer statusCode;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date timestamps;

    private Object payloads;

}
