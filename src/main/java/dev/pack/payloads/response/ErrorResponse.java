package dev.pack.payloads.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.pack.utils.CustomDateSerializer;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Integer statusCode;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date timestamps;

    private String message;

}
