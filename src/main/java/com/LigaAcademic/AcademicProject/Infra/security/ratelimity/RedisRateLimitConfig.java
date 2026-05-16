package com.LigaAcademic.AcademicProject.Infra.security.ratelimity;

import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RedisRateLimitConfig {


    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private String redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Bean
    public RedisClient redisClient() {
        String uri = redisPassword.isBlank()
                ? "redis://" + redisHost + ":" + redisPort
                : "redis://:" + redisPassword + "@" + redisHost + ":" + redisPort;
        return RedisClient.create(uri);
    }

    @Bean
    public LettuceBasedProxyManager<byte[]> proxyManager(RedisClient redisClient) {

        return LettuceBasedProxyManager.builderFor(redisClient)
                .withExpirationStrategy(
                        io.github.bucket4j.distributed.ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(15))
                )
                .build();
    }
}