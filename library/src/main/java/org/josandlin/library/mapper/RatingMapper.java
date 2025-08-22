package org.josandlin.library.mapper;

import org.josandlin.library.dto.RatingDTO;
import org.josandlin.library.entity.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public RatingDTO toRatingDto(Rating entity) {
        if (entity == null) {
            return null;
        }

        return new RatingDTO(entity.getRate(), entity.getCount());
    }

    public Rating toRatingEntity(RatingDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Rating(dto.getRate(), dto.getCount());
    }
}
