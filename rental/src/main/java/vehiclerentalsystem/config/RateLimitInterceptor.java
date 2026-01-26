package vehiclerentalsystem.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Rate Limiting Interceptor using Bucket4j.
 * 
 * Limits API requests to prevent abuse:
 * - 100 requests per minute per IP address
 * - Returns 429 Too Many Requests when limit exceeded
 * 
 * This is a Token Bucket algorithm implementation.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // Store rate limit buckets per IP address
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Rate limit configuration: 100 requests per minute
    private static final int REQUESTS_PER_MINUTE = 100;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String clientIP = getClientIP(request);
        Bucket bucket = buckets.computeIfAbsent(clientIP, this::createNewBucket);

        if (bucket.tryConsume(1)) {
            // Request allowed
            long remainingTokens = bucket.getAvailableTokens();
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(remainingTokens));
            return true;
        } else {
            // Rate limit exceeded
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\": \"Rate limit exceeded. Please try again later.\", " +
                            "\"limit\": " + REQUESTS_PER_MINUTE + ", " +
                            "\"retryAfter\": 60}");
            return false;
        }
    }

    /**
     * Create a new rate limit bucket for a client.
     * Uses Token Bucket algorithm: refill 100 tokens per minute.
     */
    private Bucket createNewBucket(String clientIP) {
        Bandwidth limit = Bandwidth.classic(
                REQUESTS_PER_MINUTE,
                Refill.greedy(REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Extract client IP address from request.
     * Handles proxied requests (X-Forwarded-For header).
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Take the first IP if there are multiple
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Get current rate limit status for an IP.
     */
    public long getRemainingRequests(String clientIP) {
        Bucket bucket = buckets.get(clientIP);
        return bucket != null ? bucket.getAvailableTokens() : REQUESTS_PER_MINUTE;
    }
}
