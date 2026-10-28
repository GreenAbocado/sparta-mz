package com.mz.commerce.service.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.RegisterProductRequest;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.repository.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional
	public ProductResponse registerProduct(RegisterProductRequest request) {
		Product product = new Product(request.getName(), request.getCategory(), request.getPrice(), request.getStock(),
			request.getStatus());

		Product savedProduct = productRepository.save(product);

		//RegisteredByResponse registeredBy = new RegisteredByResponse()

		return new ProductResponse(
			savedProduct.getId(),
			savedProduct.getName(),
			savedProduct.getCategory(),
			savedProduct.getPrice(),
			savedProduct.getStock(),
			savedProduct.getStatus(),
			//registeredBy,
			savedProduct.getCreatedAt(),
			savedProduct.getModifiedAt()
		);
	}
}
