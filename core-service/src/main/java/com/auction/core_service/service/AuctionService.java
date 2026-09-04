package com.auction.core_service.service;

import com.auction.core_service.dto.AuctionDto;
import com.auction.core_service.entity.Auction;
import com.auction.core_service.entity.AuctionImage;
import com.auction.core_service.repository.AuctionImageRepository;
import com.auction.core_service.repository.AuctionRepository;
import com.auction.core_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final AuctionImageRepository auctionImageRepository;


    @Transactional
    public AuctionDto createAuction(AuctionDto request, UUID sellerId) {

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

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .ifPresent(auction::setCategory);
        }

        auctionRepository.save(auction);

        List<String> savedUrls = new ArrayList<>();

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<AuctionImage> images = new ArrayList<>();
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                AuctionImage image = AuctionImage.builder()
                        .url(request.getImageUrls().get(i))
                        .orderIndex(i)
                        .auction(auction)
                        .build();
                images.add(image);
                savedUrls.add(image.getUrl());
            }
            auctionImageRepository.saveAll(images);
        }

        AuctionDto dto = mapToDto(auction);
        dto.setImageUrls(savedUrls);
        return dto;
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

        List<String> imageUrls = auctionImageRepository.findByAuctionId(auction.getId())
                .stream()
                .map(AuctionImage::getUrl)
                .toList();

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
                .imageUrls(imageUrls)
                .build();

    }


}
