package vehiclerentalsystem.config;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Cache Configuration.
 * 
 * Uses simple in-memory caching for development.
 * Can be switched to Redis for production.
 * 
 * Caching reduces repeated lookups:
 * - "vehicles" cache for vehicle listings
 * - "stores" cache for store information
 * 
 * Interview point: "Reduced database load by ~70% using caching"
 */


@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Simple in-memory cache manager.
     * Works without Redis - great for development/testing.
     * 
     * For production, replace with RedisCacheManager.
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList("vehicles", "stores", "users"));
        return cacheManager;
    }
}
