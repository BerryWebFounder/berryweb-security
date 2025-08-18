package com.berryweb.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockReservationRequestDto {

    private Long productId;
    private Integer warehouseId;
    private Integer quantity;
    private Long orderId;

}
