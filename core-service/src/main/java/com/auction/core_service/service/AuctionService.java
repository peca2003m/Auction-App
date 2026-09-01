package com.auction.core_service.service;

import com.auction.core_service.dto.AuctionDto;
import com.auction.core_service.entity.Auction;
import com.auction.core_service.repository.AuctionRepository;
import com.auction.core_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;


    public AuctionDto createAuction(AuctionDto request, UUID sellerId){

        Auction auction = Auction.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startingPrice(request.getStartingPrice())
                .currentPrice(request.getStartingPrice())
                .status("ACTIVE")
                .startsAt(request.getStartsAt())
                .endsAt(request.getEndsAt())
                .sellerId(sellerId)
                .build();


        auctionRepository.save(auction);

        return mapToDto(auction);
    }


    public List<AuctionDto> getAuctions(){

        return auctionRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();


    }

    public AuctionDto getAuctionById(UUID id){

        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found!"));

        return mapToDto(auction);

    }

    private AuctionDto mapToDto(Auction auction){

        return AuctionDto.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .startingPrice(auction.getStartingPrice())
                .currentPrice(auction.getCurrentPrice())
                .status(auction.getStatus())
                .startsAt(auction.getStartsAt())
                .endsAt(auction.getEndsAt())
                .sellerId(auction.getSellerId())
                .winningBidId(auction.getWinningBidId())
                .categoryId(auction.getCategory() != null ? auction.getCategory().getId() : null)
                .lastBidderId(auction.getBids() == null || auction.getBids().isEmpty() ? null :
                        auction.getBids().get(auction.getBids().size() - 1).getBidderId())
                .build();

    }


}
