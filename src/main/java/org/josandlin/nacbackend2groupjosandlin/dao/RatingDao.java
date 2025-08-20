package org.josandlin.nacbackend2groupjosandlin.dao;

import org.josandlin.nacbackend2groupjosandlin.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RatingDao extends JpaRepository<Rating, Long> {
}
