package com.karthik.ai_chatbot_api.filter;

import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.classic(10,
                Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Only rate limit chat endpoint
        if (!request.getRequestURI().contains("/api/v1/chat")) {
            chain.doFilter(request, response);
            return;
        }

        String key = request.getRemoteAddr();
        String auth = request.getHeader("Authorization");
        if (auth != null) key = auth; // per-user limit if authenticated

        Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("""
                {"status":429,"error":"Too many requests",
                 "details":"Max 10 requests per minute"}
                """);
        }
    }
}
