package com.berryweb.security.config;

import com.berryweb.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FeignClientConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.application.name}")
    private String serviceName;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 서비스간 통신을 위한 JWT 토큰 추가
                String serviceToken = jwtTokenProvider.createServiceToken(serviceName);
                template.header("Authorization", "Bearer " + serviceToken);

                // 서비스 식별을 위한 헤더 추가
                template.header("X-Service-Name", serviceName);
                template.header("Content-Type", "application/json");

                log.debug("Feign request: {} {}", template.method(), template.url());
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Retryer retryer() {
        // 재시도 설정: 최대 3번, 1초 간격으로 시작해서 최대 3초까지
        return new Retryer.Default(1000, 3000, 3);
    }

}
