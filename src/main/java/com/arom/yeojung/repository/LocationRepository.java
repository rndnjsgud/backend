package com.arom.yeojung.repository;

import com.arom.yeojung.object.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
