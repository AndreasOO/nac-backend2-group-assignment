package org.josandlin.library.mapper;


import org.josandlin.library.dto.ProductDTO;
import org.josandlin.library.dto.ProductSummaryDTO;
import org.josandlin.library.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final RatingMapper ratingMapper;

    @Autowired
    public ProductMapper(RatingMapper ratingMapper) {
        this.ratingMapper = ratingMapper;
    }

    public ProductSummaryDTO toProductSummaryDto(Product entity) {
        if (entity == null) {
            return null;
        }

        return new ProductSummaryDTO(entity.getId(), entity.title, entity.price);
    }

    public ProductDTO toProductDto(Product entity) {
        if (entity == null) {
            return null;
        }

        return new ProductDTO(entity.getId(), entity.getTitle(), entity.getPrice(),
                entity.getDescription(), entity.getCategory(), entity.getImage(),
                ratingMapper.toRatingDto(entity.getRating()));
    }

    public Product toProductEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Product(dto.getId(), dto.getTitle(), dto.getPrice(), dto.getDescription(), dto.getCategory(), dto.getImage(),
                ratingMapper.toRatingEntity(dto.getRating()));
    }
}
