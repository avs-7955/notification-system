package com.kyra.notification_service.core.correlation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String correlationId = resolveFromHeaders(request);
        if (!StringUtils.hasText(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        request.setAttribute(CorrelationIdConstants.REQUEST_ATTRIBUTE, correlationId);
        response.setHeader(CorrelationIdConstants.CORRELATION_ID_HEADER, correlationId);
        MDC.put(CorrelationIdConstants.MDC_KEY, correlationId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String resolveFromHeaders(HttpServletRequest request) {
        String correlationId = request.getHeader(CorrelationIdConstants.CORRELATION_ID_HEADER);
        if (StringUtils.hasText(correlationId)) {
            return correlationId;
        }
        return request.getHeader(CorrelationIdConstants.REQUEST_ID_HEADER);
    }
}
