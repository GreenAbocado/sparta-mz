package com.mz.commerce.service.product;

import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.product.ProductDeleteResponse;
import com.mz.commerce.dto.product.ProductRegisterRequest;
import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.ProductUpdateRequest;
import com.mz.commerce.dto.product.ProductUpdateStatusRequest;
import com.mz.commerce.dto.product.ProductUpdateStockRequest;
import com.mz.commerce.dto.product.RegisteredByResponse;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.entity.product.ProductStatus;
import com.mz.commerce.repository.admin.AdminRepository;
import com.mz.commerce.repository.product.ProductRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final AdminRepository adminRepository;

	@Transactional
	public ProductResponse registerProduct(Long adminId, ProductRegisterRequest request) {
		//관리자 검증
		Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

		//category + status 검증
		ProductCategory productCategory;
		try {
			productCategory = ProductCategory.valueOf(request.getCategory());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.PRODUCT_CATEGORY_INVALID);
		}

		//판매중과 품절은 현재 재고 기준으로 자동 결정
		ProductStatus productStatus;
		if (request.getSetAsDiscontinued()) {
			productStatus = ProductStatus.DISCONTINUED;
		} else if (request.getStock() <= 0) {
			productStatus = ProductStatus.SOLD_OUT;
		} else {
			productStatus = ProductStatus.ON_SALE;
		}

		Product product = new Product(admin, request.getName(), productCategory, request.getPrice(), request.getStock(),
			productStatus);
		Product savedProduct = productRepository.save(product);

		RegisteredByResponse registeredBy = new RegisteredByResponse(admin.getId(), admin.getName(), admin.getEmail());

		return new ProductResponse(
			savedProduct.getId(),
			savedProduct.getName(),
			savedProduct.getCategory(),
			savedProduct.getPrice(),
			savedProduct.getStock(),
			savedProduct.getStatus(),
			savedProduct.getCreatedAt(),
			savedProduct.getModifiedAt(),
			registeredBy
		);
	}

	@Transactional(readOnly = true)
	public PageResponse<ProductResponse> getAllProducts(String keyword, int page, int size, String sortBy,
		String sortOrder,
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
			? "id"
			: sortBy;

		Set<String> allowedSorts = Set.of("id", "name", "category", "price", "stock", "status", "createdAt",
			"modifiedAt");

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

		return new PageResponse<>(responsePage);

	}

	@Transactional(readOnly = true)
	public ProductResponse getProduct(Long productId) {
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//등록한 관리자 불러오기
		Admin admin = product.getRegisteredBy();
		RegisteredByResponse registeredBy = new RegisteredByResponse(admin.getId(), admin.getName(), admin.getEmail());

		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getCategory(),
			product.getPrice(),
			product.getStock(),
			product.getStatus(),
			product.getCreatedAt(),
			product.getModifiedAt(),
			registeredBy
		);
	}

	@Transactional
	public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {

		//상품 검증
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//category 검증
		ProductCategory productCategory;
		try {
			productCategory = ProductCategory.valueOf(request.getCategory());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.PRODUCT_CATEGORY_INVALID);
		}

		product.update(request.getName(), productCategory, request.getPrice());

		//등록한 관리자 불러오기
		Admin admin = product.getRegisteredBy();
		RegisteredByResponse registeredBy = new RegisteredByResponse(admin.getId(), admin.getName(), admin.getEmail());

		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getCategory(),
			product.getPrice(),
			product.getStock(),
			product.getStatus(),
			product.getCreatedAt(),
			product.getModifiedAt(),
			registeredBy
		);
	}

	@Transactional
	public ProductResponse updateProductStock(Long productId, ProductUpdateStockRequest request) {
		//상품 검증
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//재고가 0 이하이면 품절, 1 이상이면 판매중으로 전환, 단종 상태는 무시
		product.updateStock(request.getStock());
		if (product.getStatus() != ProductStatus.DISCONTINUED) {
			product.updateStatus(product.getStock() <= 0 ? ProductStatus.SOLD_OUT : ProductStatus.ON_SALE);
		}

		//등록한 관리자 불러오기
		Admin admin = product.getRegisteredBy();
		RegisteredByResponse registeredBy = new RegisteredByResponse(admin.getId(), admin.getName(), admin.getEmail());

		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getCategory(),
			product.getPrice(),
			product.getStock(),
			product.getStatus(),
			product.getCreatedAt(),
			product.getModifiedAt(),
			registeredBy
		);
	}

	@Transactional
	public ProductResponse updateProductStatus(Long productId, @Valid ProductUpdateStatusRequest request) {

		//상품 검증
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//상품 상태 변경
		ProductStatus productStatus;
		if (request.getSetAsDiscontinued()) {
			productStatus = ProductStatus.DISCONTINUED;
		} else if (product.getStock() <= 0) {
			productStatus = ProductStatus.SOLD_OUT;
		} else {
			productStatus = ProductStatus.ON_SALE;
		}

		product.updateStatus(productStatus);

		//등록한 관리자 불러오기
		Admin admin = product.getRegisteredBy();
		RegisteredByResponse registeredBy = new RegisteredByResponse(admin.getId(), admin.getName(), admin.getEmail());

		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getCategory(),
			product.getPrice(),
			product.getStock(),
			product.getStatus(),
			product.getCreatedAt(),
			product.getModifiedAt(),
			registeredBy
		);
	}

	@Transactional
	public ProductDeleteResponse deleteProduct(Long productId) {

		//상품 검증
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//해당 주문도 soft delete  orderRespository.findByProductId

		//상품 삭제
		product.softDelete();

		return new ProductDeleteResponse(product.getId(), product.getName(), product.getDeleted());
	}
}
