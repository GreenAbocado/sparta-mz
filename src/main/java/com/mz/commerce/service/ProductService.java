package com.mz.commerce.service;

import java.util.List;
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
import com.mz.commerce.dto.product.DetailedResponse.ProductDetailResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductRatingCountResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductRecentReviewResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductReviewSummaryResponse;
import com.mz.commerce.dto.product.ProductDeleteResponse;
import com.mz.commerce.dto.product.ProductRegisterRequest;
import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.ProductUpdateRequest;
import com.mz.commerce.dto.product.ProductUpdateStatusRequest;
import com.mz.commerce.dto.product.ProductUpdateStockRequest;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.entity.product.ProductStatus;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.ReviewRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final AdminRepository adminRepository;
	private final ReviewRepository reviewRepository;

	@Transactional
	public ProductResponse registerProduct(Long adminId, ProductRegisterRequest request) {
		//관리자 검증
		Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

		//category 검증
		ProductCategory productCategory;
		try {
			productCategory = ProductCategory.valueOf(request.getCategory());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.PRODUCT_CATEGORY_INVALID);
		}

		//상품 생성
		Product product = new Product(admin, request.getName(), productCategory, request.getPrice(), request.getStock(),
			request.getSetAsDiscontinued());
		Product savedProduct = productRepository.save(product);

		return new ProductResponse(savedProduct);
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
			throw new CustomException(ErrorCode.SORT_ORDER_INVALID);
		}
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		Page<Product> productPage = productRepository.searchProduct(keyword, productCategory, productStatus, pageable);
		Page<ProductResponse> responsePage = productPage.map(ProductResponse::new);

		return new PageResponse<>(responsePage);

	}

	@Transactional(readOnly = true)
	public ProductDetailResponse getProduct(Long productId) {
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//리뷰 통계 정보
		ProductReviewSummaryResponse reviewSummary = reviewRepository.findReviewSummary(productId);
		List<ProductRatingCountResponse> ratingCounts = reviewRepository.countReviewsByRating(productId);

		//최신 리뷰 목록
		List<ProductRecentReviewResponse> recentReviews = reviewRepository.findRecentReviews(productId,
			PageRequest.of(0, 3));

		return new ProductDetailResponse(new ProductResponse(product), reviewSummary, ratingCounts, recentReviews);
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

		return new ProductResponse(product);
	}

	@Transactional
	public ProductResponse updateProductStock(Long productId, ProductUpdateStockRequest request) {
		//상품 검증
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//재고가 0 이하이면 품절, 1 이상이면 판매중으로 전환, 단종 상태는 무시
		product.updateStock(request.getStock());

		return new ProductResponse(product);
	}

	@Transactional
	public ProductResponse updateProductStatus(Long productId, @Valid ProductUpdateStatusRequest request) {

		//상품 검증
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//상품 상태 변경
		product.updateStatus(request.getSetAsDiscontinued());

		return new ProductResponse(product);
	}

	@Transactional
	public ProductDeleteResponse deleteProduct(Long productId) {

		//상품 검증
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//상품 삭제
		product.softDelete();

		return new ProductDeleteResponse(product.getId(), product.getName(), product.getDeleted());
	}
}
