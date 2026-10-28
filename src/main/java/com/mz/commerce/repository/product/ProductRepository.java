package com.mz.commerce.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.commerce.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
