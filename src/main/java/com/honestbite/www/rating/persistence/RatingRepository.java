package com.honestbite.www.rating.persistence;

import com.honestbite.www.rating.model.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
}
