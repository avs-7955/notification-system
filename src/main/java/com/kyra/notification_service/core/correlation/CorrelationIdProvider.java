package com.kyra.notification_service.core.correlation;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Component
public class CorrelationIdProvider {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdProvider.class);

    public String getOrCreate() {
        String fromMdc = MDC.get(CorrelationIdConstants.MDC_KEY);
        if (StringUtils.hasText(fromMdc)) {
            return fromMdc;
        }

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            Object attribute = request.getAttribute(CorrelationIdConstants.REQUEST_ATTRIBUTE);
            if (attribute instanceof String value && StringUtils.hasText(value)) {
                return value;
            }
        }

        String fallbackId = UUID.randomUUID().toString();
        log.warn("MDC/Correlation Attribute missing in Controller. Generating fallback ID: {}", fallbackId);
        return fallbackId;
    }
}
