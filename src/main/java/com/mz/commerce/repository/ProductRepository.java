package com.mz.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.commerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
