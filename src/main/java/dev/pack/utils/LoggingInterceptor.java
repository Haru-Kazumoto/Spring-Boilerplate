package dev.pack.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(
                "Incoming request [{}] - [{}]",
                request.getMethod(),
                request.getServletPath()
        );
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        int statusCode = response.getStatus();
        String statusMessage = HttpStatus.valueOf(statusCode).getReasonPhrase();

        log.info(
                "Completed request. Method: {}, Path: {}, Response Status: {} - {}",
                request.getMethod(),
                request.getRequestURI(),
                statusCode,
                statusMessage
        );

        if (ex != null) {
            log.error(
                    "An error occurred during request processing. Method: {}, Path: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    ex
            );
        } else {
            if (statusCode >= 400 && statusCode <= 499) {
                log.warn(
                        "Client error during request processing. Method: {}, Path: {}, Response Status: {} - {}",
                        request.getMethod(),
                        request.getRequestURI(),
                        statusCode,
                        statusMessage
                );
            } else if (statusCode >= 500 && statusCode <= 599) {
                log.error("Server error during request processing. Method: {}, Path: {}, Response Status: {} - {}",
                        request.getMethod(),
                        request.getRequestURI(),
                        statusCode,
                        statusMessage
                );
            }
        }
    }
}
