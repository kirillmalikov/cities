package com.helmes.cities.domain.city.repository;

import com.helmes.cities.domain.city.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Page<City> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
