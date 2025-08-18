package com.berryweb.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    private Long orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String customerEmail;

}
