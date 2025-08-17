package com.berryweb.security.client;

import com.berryweb.security.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${service.payment.url:http://localhost:8082}",
        path = "/payment-service/api/payments",
        configuration = FeignClientConfig.class
)
public interface PaymentServiceClient {

    @PostMapping
    PaymentResponseDto createPayment(@RequestBody PaymentRequestDto request);

    @GetMapping("/order/{orderId}")
    PaymentResponseDto getPaymentByOrderId(@PathVariable("orderId") Long orderId);

    @PostMapping("/{paymentId}/process")
    PaymentResponseDto processPayment(@PathVariable("paymentId") Long paymentId,
                                      @RequestBody String gatewayResponse);

}
