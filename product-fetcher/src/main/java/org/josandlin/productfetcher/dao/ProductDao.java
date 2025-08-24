package org.josandlin.productfetcher.dao;

import org.josandlin.library.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {

}
