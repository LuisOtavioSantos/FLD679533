package com.example.rest.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interceptador para Rate Limiting (Proteção contra DDoS/Bots)
 * Limita a 60 requisições por minuto por endereço IP.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // Limite de requisições
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    // Controle de requisições: IP -> {count, timestamp_inicio}
    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIP(request);
        long currentTime = System.currentTimeMillis();

        requestCounts.compute(clientIp, (ip, info) -> {
            if (info == null || (currentTime - info.startTime) > 60_000) {
                // Primeira requisição ou a janela de 1 minuto expirou
                return new RequestInfo(1, currentTime);
            } else {
                // Incrementa requisições dentro do mesmo minuto
                info.count++;
                return info;
            }
        });

        RequestInfo currentInfo = requestCounts.get(clientIp);
        if (currentInfo.count > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too Many Requests - Rate Limit Exceeded (Max " + MAX_REQUESTS_PER_MINUTE + " per minute)");
            return false; // Bloqueia a requisição
        }

        return true; // Libera a requisição
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private static class RequestInfo {
        int count;
        long startTime;

        RequestInfo(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}
