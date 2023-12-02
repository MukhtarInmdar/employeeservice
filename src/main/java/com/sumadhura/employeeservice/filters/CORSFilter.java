package com.sumadhura.employeeservice.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;


public class CORSFilter extends OncePerRequestFilter {
    private final static Logger logger = Logger.getLogger(CORSFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    	logger.info("**** The  control is start of the  dofilterinternal *****");
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
        	logger.trace("Sending Header....");
            // CORS "pre-flight" request
            response.addHeader("Access-Control-Allow-Methods", "GET, POST");
            // response.addHeader("Access-Control-Allow-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
            response.addHeader("Access-Control-Max-Age", "1");
        }
        filterChain.doFilter(request, response);
        logger.info("**** The  control is end of  the  dofilterinternal *****");
    }

}

