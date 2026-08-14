package com.auction.core_service.controller;

import com.auction.core_service.dto.BidDto;
import com.auction.core_service.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping("/{auctionId}/bids")
    public BidDto placeBid(@PathVariable UUID auctionId, @RequestBody BidDto request, JwtAuthenticationToken token){

        UUID bidderId = UUID.fromString(token.getName());
        return bidService.placeBid(auctionId, request.getAmount(), bidderId);

    }


}
