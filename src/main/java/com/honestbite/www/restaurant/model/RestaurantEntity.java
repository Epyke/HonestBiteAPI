package com.honestbite.www.restaurant.model;

import com.honestbite.www.adress.model.AdressEntity;
import com.honestbite.www.category.model.CategoryEntity;
import com.honestbite.www.menu.model.MenuEntity;
import com.honestbite.www.ophour.model.OpHourEntity;
import com.honestbite.www.rating.model.RatingEntity;
import com.honestbite.www.request.model.RequestEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "restaurants")
@Entity
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description;
    String phoneNumber;

    Double avgPrice;

    String mapsUrl;
    String cover;
    String logo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_id")
    private AdressEntity address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<OpHourEntity> operatingHours = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RatingEntity> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<RequestEntity> requests = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<MenuEntity> menus = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cat_restaurants",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();
}
