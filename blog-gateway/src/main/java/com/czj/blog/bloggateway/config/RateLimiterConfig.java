package com.czj.blog.bloggateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @Author: clownc
 * @Date: 2019-03-28 15:06
 * 路由限流配置
 * Spring Cloud Gateway默认集成了Redis限流，可以对不同服务做不同维度的限流，如：IP限流、用户限流 、接口限流
 * 这里做的是ip限流
 */
@Configuration
public class RateLimiterConfig {
    @Bean(value = "remoteAddrKeyResolver")
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}

