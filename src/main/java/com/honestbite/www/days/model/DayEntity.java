package com.honestbite.www.days.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "days_of_week")
public class DayEntity {
    @Id
    private Integer id;
    private String ptName;
    private String enName;
}