package com.berryweb.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(FeignClient.class)
@Import(FeignClientConfig.class)
public class FeignAutoConfiguration {
}
