package com.auction.core_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AuctionStatusUpdate {
    private String status;
    private BigDecimal currentPrice;
}