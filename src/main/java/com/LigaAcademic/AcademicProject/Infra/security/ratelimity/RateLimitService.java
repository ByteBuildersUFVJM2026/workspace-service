package com.LigaAcademic.AcademicProject.Infra.security.ratelimity;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
class RateLimitService {

    private final LettuceBasedProxyManager<byte[]> proxyManager;

    public RateLimitService(LettuceBasedProxyManager<byte[]> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public boolean allowRequest(String clientIp) {
        byte[] redisKey = ("rate_limit:" + clientIp).getBytes();

        BucketProxy bucket = proxyManager.builder().build(redisKey, this::getConfig);

        return bucket.tryConsume(1);
    }

    private BucketConfiguration getConfig() {

        Refill refill = Refill.intervally(5, Duration.ofMinutes(15));
        Bandwidth limit = Bandwidth.classic(5, refill);

        return BucketConfiguration.builder()
                .addLimit(limit)
                .build();
    }
}