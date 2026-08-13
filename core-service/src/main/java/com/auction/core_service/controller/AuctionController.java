package com.auction.core_service.controller;


import com.auction.core_service.dto.AuctionDto;
import com.auction.core_service.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;


    @PostMapping
    public AuctionDto createAuction(@RequestBody AuctionDto request, JwtAuthenticationToken token){

        UUID sellerId = UUID.fromString(token.getName());
        return auctionService.createAuction(request, sellerId);

    }

    @GetMapping
    public List<AuctionDto> getAuctions(){
        return auctionService.getAuctions();
    }


    @GetMapping("/{id}")
    public AuctionDto getAuctionById(@PathVariable UUID id){
        return auctionService.getAuctionById(id);
    }


}
