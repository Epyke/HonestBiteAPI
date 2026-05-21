package com.honestbite.www.adress.persistence;

import com.honestbite.www.adress.model.AdressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdressRepository extends JpaRepository<AdressEntity, Long> {
}
