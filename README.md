AxCache
=======

Simple Multi-module cache system.

Install 
-------

```xml

<dependency>
    <groupId>com.axgrid</groupId>
    <artifactId>AxCahce</artifactId>
    <version>1.1</version>
</dependency>

```


Usage
-----

```java


@EnableAxCache
@Configuration
public class MyServiceConfiguration {

    public static final String MY_CACHE ="my-cache";

    // Method name (bean name) must be uniq
    @Bean
    public AxCacheObject getMyCache(@Value("${my.cache.expire:10}") int expire,
                                     @Value("${my.cache.size:10000}") int size)
    {
        return AxCacheObject
                .builder()
                .configuration(new AxCacheObject.CacheObjectConfiguration(MY_CACHE, 
                               AxCacheObject.ExpireType.Write, 
                               expire, 
                               size))
                .build();
    }
}

```
