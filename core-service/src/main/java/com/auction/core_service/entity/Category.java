package com.auction.core_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

}
