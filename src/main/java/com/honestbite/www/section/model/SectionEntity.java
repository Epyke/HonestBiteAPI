package com.honestbite.www.section.model;

import com.honestbite.www.menu.model.MenuEntity;
import com.honestbite.www.restaurant.model.RestaurantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sections")
@Entity
public class SectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
    private List<MenuEntity> menus = new ArrayList<>();
}
