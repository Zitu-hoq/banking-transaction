package com.zituhoq.banking_transaction.in.log.filters;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CorrelationIdFilterTests {

    @Mock
    private jakarta.servlet.http.HttpServletRequest request;

    @Mock
    private jakarta.servlet.http.HttpServletResponse response;

    private CorrelationIdFilter filter;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        filter = new CorrelationIdFilter();
    }

    @AfterEach
    void tearDown() throws Exception {
        MDC.clear();
        mocks.close();
    }

    @Test
    void shouldUseExistingCorrelationId() throws IOException, jakarta.servlet.ServletException {
        String existingId = "test-correlation-id-123";
        when(request.getHeader("X-Correlation-Id")).thenReturn(existingId);

        final String[] capturedId = new String[1];
        FilterChain captureChain = (req, res) -> capturedId[0] = MDC.get("correlationId");

        filter.doFilter(request, response, captureChain);

        assertEquals(existingId, capturedId[0]);
    }

    @Test
    void shouldGenerateCorrelationIdWhenHeaderMissing() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        final String[] capturedId = new String[1];
        FilterChain captureChain = (req, res) -> capturedId[0] = MDC.get("correlationId");

        filter.doFilter(request, response, captureChain);

        assertNotNull(capturedId[0]);
        assertFalse(capturedId[0].isEmpty());
    }

    @Test
    void shouldGenerateCorrelationIdWhenHeaderEmpty() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("X-Correlation-Id")).thenReturn("");

        final String[] capturedId = new String[1];
        FilterChain captureChain = (req, res) -> capturedId[0] = MDC.get("correlationId");

        filter.doFilter(request, response, captureChain);

        assertNotNull(capturedId[0]);
        assertFalse(capturedId[0].isEmpty());
    }

    @Test
    void shouldClearMDCAfterFilter() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("X-Correlation-Id")).thenReturn("test-id");

        filter.doFilter(request, response, (req, res) -> {});

        assertNull(MDC.get("correlationId"));
    }

    @Test
    void shouldClearMDCWhenFilterChainThrowsException() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("X-Correlation-Id")).thenReturn("test-id");
        FilterChain throwingChain = (req, res) -> { throw new RuntimeException("Test exception"); };

        assertThrows(RuntimeException.class, () -> filter.doFilter(request, response, throwingChain));

        assertNull(MDC.get("correlationId"));
    }

    @Test
    void shouldGenerateUniqueCorrelationIds() throws IOException, jakarta.servlet.ServletException {
        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        final String[] firstId = new String[1];
        filter.doFilter(request, response, (req, res) -> firstId[0] = MDC.get("correlationId"));

        final String[] secondId = new String[1];
        filter.doFilter(request, response, (req, res) -> secondId[0] = MDC.get("correlationId"));

        assertNotEquals(firstId[0], secondId[0]);
    }
}
