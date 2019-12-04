package com.axgrid.cache;

import lombok.*;

import java.util.List;

@Builder
@Data
@ToString()
public class AxCacheObject {

    public enum ExpireType {
        Access,
        Write
    }

    @Singular
    List<CacheObjectConfiguration> configurations;

    @Data
    @AllArgsConstructor
    public static class CacheObjectConfiguration {
        String name;
        ExpireType expireType;
        int secondToExpire;
        int size;
    }

}
