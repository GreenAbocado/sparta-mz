package com.mz.commerce.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
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
import com.mz.commerce.dto.product.ProductSearchRequest;
import com.mz.commerce.dto.product.ProductUpdateRequest;
import com.mz.commerce.dto.product.ProductUpdateStatusRequest;
import com.mz.commerce.dto.product.ProductUpdateStockRequest;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.ReviewRepository;
import com.mz.commerce.service.common.DistrubutionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final AdminRepository adminRepository;
	private final ReviewRepository reviewRepository;
	private final DistrubutionUtil distrubutionUtil;

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
	public PageResponse<ProductResponse> getAllProducts(ProductSearchRequest request) {

		return new PageResponse<>(
			productRepository.searchProduct(request.toPageable(), request.getKeyword(), request.getCategory(),
				request.getStatus()));

	}

	@Transactional(readOnly = true)
	public ProductDetailResponse getProduct(Long productId) {
		Product product = productRepository.findByIdAndDeletedIsNull(productId).orElseThrow(
			() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		//리뷰 통계 정보
		ProductReviewSummaryResponse reviewSummary = reviewRepository.findReviewSummary(productId);
		List<ProductRatingCountResponse> ratingCounts = distrubutionUtil.getRatingDistribution(productId);

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
	public ProductResponse updateProductStatus(Long productId, ProductUpdateStatusRequest request) {

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
