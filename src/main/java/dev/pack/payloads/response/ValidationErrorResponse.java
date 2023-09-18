package dev.pack.payloads.response;

import lombok.*;
import java.util.List;

@Data
@Builder
public class ValidationErrorResponse {
    private Integer statusCode;
    private List<String> message;
}
