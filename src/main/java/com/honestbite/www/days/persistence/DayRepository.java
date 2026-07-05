package com.honestbite.www.days.persistence;

import com.honestbite.www.days.model.DayEntity;
import com.honestbite.www.menu.model.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DayRepository extends JpaRepository<DayEntity, Long> {
}
