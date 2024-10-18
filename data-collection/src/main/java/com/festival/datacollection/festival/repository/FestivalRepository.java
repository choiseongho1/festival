package com.festival.datacollection.festival.repository;

import com.festival.datacollection.festival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, String> {
}
