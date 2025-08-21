package org.josandlin.nacbackend2groupjosandlin.service;

import org.josandlin.nacbackend2groupjosandlin.dao.ProductDao;
import org.josandlin.nacbackend2groupjosandlin.dto.ProductDTO;
import org.josandlin.nacbackend2groupjosandlin.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductDao productDao;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductDao productDao,  ProductMapper productMapper) {
        this.productDao = productDao;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDTO> getProducts() {
        return productDao.findAll()
                .stream()
                .map(productMapper::toProductDto)
                .collect(Collectors.toList());
    }
}
