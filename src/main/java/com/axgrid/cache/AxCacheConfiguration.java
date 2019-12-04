package com.axgrid.cache;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
@Slf4j
public class AxCacheConfiguration {

    @Bean
    public CacheManager infoCacheManager(@Autowired(required = false) Map<String, AxCacheObject> axCacheObjects) {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(axCacheObjects.values().stream()
                .map(AxCacheObject::getConfigurations)
                .flatMap(Collection::stream)
                .map(item -> {
                    switch (item.getExpireType()) {
                        default:
                        case Access:
                            return buildAccessExpireCache(item);
                        case Write:
                            return buildWriteExpireCache(item);
                    }
                }).collect(Collectors.toList()));
        log.debug("Cache manager created");
        return manager;
    }

    private CaffeineCache buildAccessExpireCache(AxCacheObject.CacheObjectConfiguration cacheObject) {
        log.info("Create Access-Expire cache {}", cacheObject);
        return new CaffeineCache(cacheObject.getName(), Caffeine.newBuilder()
                .expireAfterAccess(cacheObject.getSecondToExpire(), TimeUnit.SECONDS)
                .maximumSize(cacheObject.getSize())
                .ticker(Ticker.systemTicker())
                .build());
    }

    private CaffeineCache buildWriteExpireCache(AxCacheObject.CacheObjectConfiguration cacheObject) {
        log.info("Create Write-Expire cache {}", cacheObject);
        return new CaffeineCache(cacheObject.getName(), Caffeine.newBuilder()
                .expireAfterWrite(cacheObject.getSecondToExpire(), TimeUnit.SECONDS)
                .maximumSize(cacheObject.getSize())
                .ticker(Ticker.systemTicker())
                .build());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper(new JsonFactory());
    }

}
