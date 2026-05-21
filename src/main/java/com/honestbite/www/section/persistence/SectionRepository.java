package com.honestbite.www.section.persistence;

import com.honestbite.www.section.model.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Long> {
}
