package com.mz.commerce.service;

import org.springframework.stereotype.Service;

import com.mz.commerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

}
