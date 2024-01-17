package com.dj.gisapplication.repository;

import com.dj.gisapplication.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    // You can define custom query methods here
}
