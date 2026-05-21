package com.honestbite.www.menu.persistence;

import com.honestbite.www.menu.model.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
