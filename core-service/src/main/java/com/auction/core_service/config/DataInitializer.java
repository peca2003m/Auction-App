package com.auction.core_service.config;

import com.auction.core_service.entity.Category;
import com.auction.core_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category(null, "Electronics", "electronics"),
                    new Category(null, "Vehicles", "vehicles"),
                    new Category(null, "Real Estate", "real-estate"),
                    new Category(null, "Clothing", "clothing"),
                    new Category(null, "Sports", "sports"),
                    new Category(null, "Home & Garden", "home-garden"),
                    new Category(null, "Books", "books"),
                    new Category(null, "Music & Instruments", "music-instruments"),
                    new Category(null, "Art & Collectibles", "art-collectibles"),
                    new Category(null, "Other", "other")
            );
            categoryRepository.saveAll(categories);
        }
    }
}