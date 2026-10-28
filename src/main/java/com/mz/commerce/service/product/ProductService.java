package com.mz.commerce.service.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.Enumerator.product.ProductCategory;
import com.mz.commerce.Enumerator.product.ProductStatus;
import com.mz.commerce.common.CustomException;
import com.mz.commerce.common.ErrorCode;
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
		//category + status 검증
		ProductCategory productCategory;
		try {
			productCategory = ProductCategory.valueOf(request.getCategory());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.PRODUCT_CATEGORY_INVALID);
		}
		ProductStatus productStatus;
		try {
			productStatus = ProductStatus.valueOf(request.getStatus());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.PRODUCT_STATUS_INVALID);
		}

		Product product = new Product(request.getName(), productCategory, request.getPrice(), request.getStock(),
			productStatus);

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
