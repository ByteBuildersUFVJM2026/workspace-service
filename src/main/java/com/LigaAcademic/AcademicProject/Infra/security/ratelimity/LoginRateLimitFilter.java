package com.LigaAcademic.AcademicProject.Infra.security.ratelimity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final ObjectMapper mapper = new ObjectMapper();

    public LoginRateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();
        if (!uri.equals("/auth/login") || !request.getMethod().equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = getClientIP(request);

        if (rateLimitService.allowRequest(clientIp)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");

            Map<String,Object> errorDetails = new HashMap<>();
            errorDetails.put("status",429);
            errorDetails.put("error", "Too Many Requests");
            errorDetails.put("message", "Muitas tentativas de login. Por favor, aguarde 15 minutos.");
            errorDetails.put("path", request.getRequestURI());

            response.getWriter().write(mapper.writeValueAsString(errorDetails));
        }
    }


    private String getClientIP(HttpServletRequest request) {

        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {

            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
