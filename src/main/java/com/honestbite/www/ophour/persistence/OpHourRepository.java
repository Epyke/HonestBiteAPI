package com.honestbite.www.ophour.persistence;

import com.honestbite.www.ophour.model.OpHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OpHourRepository extends JpaRepository<OpHourEntity, Long> {
}
