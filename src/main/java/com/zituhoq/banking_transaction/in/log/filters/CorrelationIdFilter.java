package com.zituhoq.banking_transaction.in.log.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        String correlationId = httpReq.getHeader("X-Correlation-Id");

        if(correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        MDC.put("correlationId", correlationId);
        try{
            chain.doFilter(req,res);
        }finally {
            MDC.clear();
        }
    }
}
