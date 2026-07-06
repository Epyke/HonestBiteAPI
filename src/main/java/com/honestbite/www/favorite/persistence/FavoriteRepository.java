package com.honestbite.www.favorite.persistence;

import com.honestbite.www.favorite.model.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findByUserId(Long userId);
    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);
    Optional<FavoriteEntity> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
