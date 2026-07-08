package com.honestbite.www.days.persistence;

import com.honestbite.www.days.model.DayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DayRepository extends JpaRepository<DayEntity, Long> {
    Optional<DayEntity> findByPtName(String ptName);
}
