package com.kyra.notification_service.core.correlation;

public final class CorrelationIdConstants {

    private CorrelationIdConstants() {
    }

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String MDC_KEY = "correlationId";
    public static final String REQUEST_ATTRIBUTE = "correlationId";
}
