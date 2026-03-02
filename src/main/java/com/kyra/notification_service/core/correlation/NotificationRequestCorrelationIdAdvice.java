package com.kyra.notification_service.core.correlation;

import com.kyra.notification_service.api.dto.NotificationRequest;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

@Component
@ControllerAdvice
public class NotificationRequestCorrelationIdAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(
            MethodParameter methodParameter,
            java.lang.reflect.Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return NotificationRequest.class.equals(targetType);
    }

    @Override
    public Object afterBodyRead(
            Object body,
            HttpInputMessage inputMessage,
            MethodParameter parameter,
            java.lang.reflect.Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        if (!(body instanceof NotificationRequest requestBody)) {
            return body;
        }

        HttpHeaders headers = inputMessage.getHeaders();
        String headerCorrelationId = firstNonBlank(
                headers.getFirst(CorrelationIdConstants.CORRELATION_ID_HEADER),
                headers.getFirst(CorrelationIdConstants.REQUEST_ID_HEADER)
        );
        if (StringUtils.hasText(headerCorrelationId)) {
            return body;
        }

        if (!StringUtils.hasText(requestBody.correlationId())) {
            return body;
        }

        String payloadCorrelationId = requestBody.correlationId().trim();
        if (inputMessage instanceof ServletServerHttpRequest servletRequest) {
            servletRequest.getServletRequest()
                    .setAttribute(CorrelationIdConstants.REQUEST_ATTRIBUTE, payloadCorrelationId);
        }
        MDC.put(CorrelationIdConstants.MDC_KEY, payloadCorrelationId);
        return body;
    }

    private String firstNonBlank(String first, String second) {
        if (StringUtils.hasText(first)) {
            return first;
        }
        return second;
    }
}
