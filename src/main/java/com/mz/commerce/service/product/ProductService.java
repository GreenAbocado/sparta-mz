package com.mz.commerce.service.product;

import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.CustomException;
import com.mz.commerce.common.ErrorCode;
import com.mz.commerce.dto.product.ProductPageResponse;
import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.RegisterProductRequest;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.enumerator.product.ProductCategory;
import com.mz.commerce.enumerator.product.ProductStatus;
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

	@Transactional(readOnly = true)
	public ProductPageResponse getAllProducts(String keyword, int page, int size, String sortBy, String sortOrder,
		String category, String status) {

		//1. category + status 검증
		ProductCategory productCategory = null;
		if (category != null && !category.isBlank()) {
			try {
				productCategory = ProductCategory.valueOf(category);
			} catch (IllegalArgumentException e) {
				throw new CustomException(ErrorCode.PRODUCT_CATEGORY_INVALID);
			}
		}
		ProductStatus productStatus = null;
		if (status != null && !status.isBlank()) {
			try {
				productStatus = ProductStatus.valueOf(status);
			} catch (IllegalArgumentException e) {
				throw new CustomException(ErrorCode.PRODUCT_STATUS_INVALID);
			}
		}

		// 2. page + size 검증 -> controller에서 Bean validation

		// 3. 정렬 기준 검증
		String sortField = (sortBy == null || sortBy.isBlank())
			? "price"
			: sortBy;

		Set<String> allowedSorts = Set.of("price", "stock", "createdAt");

		if (!allowedSorts.contains(sortField)) {
			throw new CustomException(ErrorCode.PRODUCT_SORT_FIELD_INVALID);
		}

		// 4. 정렬 순서 검증
		String direction = (sortOrder == null || sortOrder.isBlank())
			? "desc"
			: sortOrder.toLowerCase(Locale.ROOT);

		Sort sort;

		if (direction.equals("asc")) {
			sort = Sort.by(sortField).ascending();
		} else if (direction.equals("desc")) {
			sort = Sort.by(sortField).descending();
		} else {
			throw new CustomException(ErrorCode.PRODUCT_SORT_ORDER_INVALID);
		}
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		Page<Product> productPage = productRepository.searchProduct(keyword, productCategory, productStatus, pageable);
		Page<ProductResponse> responsePage = productPage.map(ProductResponse::new);

		return new ProductPageResponse(responsePage);

	}

	@Transactional(readOnly = true)
	public ProductResponse getProduct(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getCategory(),
			product.getPrice(),
			product.getStock(),
			product.getStatus(),
			product.getCreatedAt(),
			product.getModifiedAt()
		);
	}
}
