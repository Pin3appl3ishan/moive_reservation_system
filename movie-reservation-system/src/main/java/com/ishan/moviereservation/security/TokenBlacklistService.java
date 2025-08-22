package com.ishan.moviereservation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenBlacklistService {

    private final ConcurrentMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TokenBlacklistService() {
        // Clean up expired tokens every hour
        scheduler.scheduleAtFixedRate(this::cleanupExpiredTokens, 1, 1, TimeUnit.HOURS);
    }

    /**
     * Add a token to the blacklist
     * @param token The JWT token to blacklist
     */
    public void blacklistToken(String token) {
        try {
            // Extract expiration time from token
            Long expirationTime = extractExpirationTime(token);
            if (expirationTime != null) {
                blacklistedTokens.put(token, expirationTime);
                log.info("Token blacklisted successfully");
            }
        } catch (Exception e) {
            log.error("Error blacklisting token: {}", e.getMessage());
        }
    }

    /**
     * Check if a token is blacklisted
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    /**
     * Clean up expired tokens from the blacklist
     */
    private void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
        int initialSize = blacklistedTokens.size();
        
        blacklistedTokens.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue() < currentTime;
            if (expired) {
                log.debug("Removing expired token from blacklist");
            }
            return expired;
        });
        
        int finalSize = blacklistedTokens.size();
        if (initialSize != finalSize) {
            log.info("Cleaned up {} expired tokens from blacklist", initialSize - finalSize);
        }
    }

    /**
     * Extract expiration time from JWT token
     * @param token The JWT token
     * @return Expiration time in seconds, or null if extraction fails
     */
    private Long extractExpirationTime(String token) {
        try {
            // This is a simplified extraction - in production, you might want to use a proper JWT library
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                // Decode the payload (second part)
                String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                // Parse JSON to get exp field
                if (payload.contains("\"exp\":")) {
                    String expValue = payload.split("\"exp\":")[1].split(",")[0];
                    return Long.parseLong(expValue);
                }
            }
        } catch (Exception e) {
            log.error("Error extracting expiration time from token: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Get the current size of the blacklist (for monitoring)
     * @return Number of blacklisted tokens
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }
}
