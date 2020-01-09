package com.jx.common.fifter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.omg.CORBA.Environment;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DispatcherFilter implements Filter {
    private static final Log logger = LogFactory.getLog(DispatcherFilter.class);

    @Override
    public void init(FilterConfig filterConfig){

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String traceId = StringUtils.remove(UUID.randomUUID().toString(), '-');
        MDC.put("traceId", traceId);

        HttpServletRequest httpRequest = new DispatcherRequestWrapper((HttpServletRequest) request);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            StringBuilder log = new StringBuilder("request header:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerVal = httpRequest.getHeader(headerName);
                log.append(headerName).append(":").append(headerVal).append(",");
            }
            log.append(System.getProperty("line.separator")).append("servlet path: ").append(httpRequest.getServletPath());
            logger.info(log);

            httpResponse.setHeader("traceId", traceId);
            chain.doFilter(httpRequest, httpResponse);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {

    }
}
