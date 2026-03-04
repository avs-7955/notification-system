package com.kyra.notification_service.core.timing;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.kyra.notification_service.core.timing.TimingConstants.START_TIME_NANOS_ATTRIBUTE;

public class RequestTimingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        long startNanos = System.nanoTime();
        request.setAttribute(START_TIME_NANOS_ATTRIBUTE, startNanos);
        try {
            filterChain.doFilter(request, response);
        } finally {
            Object value = request.getAttribute(START_TIME_NANOS_ATTRIBUTE);
            long startedAt = value instanceof Long castValue ? castValue : startNanos;
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
            logger.info("Request processed in " + elapsedMs + " ms");
        }
    }
}
