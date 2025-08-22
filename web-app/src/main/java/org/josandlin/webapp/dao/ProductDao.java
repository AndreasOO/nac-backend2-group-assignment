package org.josandlin.nacbackend2groupjosandlin.dao;

import org.josandlin.nacbackend2groupjosandlin.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
}
