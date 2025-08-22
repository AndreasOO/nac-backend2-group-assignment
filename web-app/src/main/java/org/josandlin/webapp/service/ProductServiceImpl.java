package org.josandlin.webapp.service;

import jakarta.transaction.Transactional;
import org.josandlin.webapp.dao.ProductDao;
import org.josandlin.webapp.dto.ProductDTO;
import org.josandlin.webapp.entity.Product;
import org.josandlin.webapp.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductMapper productMapper) {
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

    @Override
    public ProductDTO getProductById(Long id) {
        return productDao.findById(id).map(productMapper::toProductDto).orElse(null);
    }

//    @Override
//    public ProductDTO getProductById(Long id) {
//        Product product = productDao.findById(id).isPresent() ? productDao.findById(id).get() : null;
//        return productMapper.toProductDto(product);
//    }

    @Override
    public boolean saveAll(List<ProductDTO> productDTO) {
        try {
            productDTO.stream().map(productMapper::toProductEntity).forEach(productDao::save);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
