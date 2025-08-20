package org.josandlin.nacbackend2groupjosandlin.service;

import org.josandlin.nacbackend2groupjosandlin.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductDao productDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao){
        this.productDao = productDao;
    }

    /* @Override
    public List<ProductDTO> getProducts() {
        return productDao.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }



     */

}
