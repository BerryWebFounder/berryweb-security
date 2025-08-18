package com.berryweb.security.client;

import com.berryweb.security.dto.InventoryResponseDto;
import com.berryweb.security.dto.StockReservationRequestDto;
import com.berryweb.security.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "inventory-service",
        url = "${service.inventory.url:http://localhost:8083}",
        path = "/inventory-service/api/inventory",
        configuration = FeignClientConfig.class
)
public interface InventoryServiceClient {

    @GetMapping("/product/{productId}")
    List<InventoryResponseDto> getInventoriesByProduct(@PathVariable("productId") Long productId);

    @GetMapping("/product/{productId}/total-stock")
    Integer getTotalAvailableStock(@PathVariable("productId") Long productId);

    @PostMapping("/reserve")
    InventoryResponseDto reserveStock(@RequestBody StockReservationRequestDto request);

    @PostMapping("/release")
    InventoryResponseDto releaseStock(@RequestParam Long productId,
                                      @RequestParam Integer warehouseId,
                                      @RequestParam Integer quantity,
                                      @RequestParam Long orderId);

}
