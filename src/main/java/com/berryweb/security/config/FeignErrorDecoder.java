package com.berryweb.security.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Feign error - Method: {}, Status: {}, Reason: {}",
                methodKey, response.status(), response.reason());

        return switch (response.status()) {
            case 400 -> new IllegalArgumentException("잘못된 요청입니다.");
            case 404 -> new RuntimeException("요청한 리소스를 찾을 수 없습니다.");
            case 500 -> new RuntimeException("내부 서버 오류가 발생했습니다.");
            case 503 -> new RuntimeException("서비스를 사용할 수 없습니다.");
            default -> new RuntimeException("알 수 없는 오류가 발생했습니다.");
        };
    }

}
