package dev.pack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof BadCredentialsException) {
            handleBadCredentialsException(request, response, (BadCredentialsException) authException);
        } else {
            handleDefaultException(request, response, authException);
        }
    }

    private void handleBadCredentialsException(HttpServletRequest request,
                                               HttpServletResponse response,
                                               BadCredentialsException ex) throws IOException {
        handleException(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
    }

    private void handleDefaultException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException ex) throws IOException {
        handleException(request, response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }

    private void handleException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 int httpStatus,
                                 String message) throws IOException {
        log.error("Unauthorized error: {}", message);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus);

        UnauthorizedError result = UnauthorizedError.builder()
                .status(httpStatus)
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(message)
                .path(request.getServletPath())
                .build();

        objectMapper.writeValue(response.getOutputStream(), result);
    }

    @Data
    @Builder
    private static class UnauthorizedError {
        private int status;
        private String error;
        private String message;
        private String path;
    }
}
