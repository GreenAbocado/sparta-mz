package com.mz.commerce.service.common;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.mz.commerce.dto.customer.CustomerStatusCountResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductCategoryCountResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductRatingCountResponse;
import com.mz.commerce.entity.customer.CustomerStatus;
import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.repository.CustomerRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistrubutionUtil {
	private final ProductRepository productRepository;
	private final CustomerRepository customerRepository;
	private final ReviewRepository reviewRepository;

	//카테고리별 상품 통계
	public List<ProductCategoryCountResponse> getCategoryDistribution() {
		Map<ProductCategory, Long> categoryCounts = new EnumMap<>(ProductCategory.class);

		// initialize 0
		for (ProductCategory category : ProductCategory.values()) {
			categoryCounts.put(category, 0L);
		}

		for (ProductCategoryCountResponse result : productRepository.distibutionByCategory()) {
			categoryCounts.put(result.getCategory(), result.getCount());
		}

		//DTO 형식 반환
		return categoryCounts.entrySet().stream()
			.map(entry -> new ProductCategoryCountResponse(
				entry.getKey(),
				entry.getValue()
			))
			.toList();
	}

	// 고객 상태별 통계
	public List<CustomerStatusCountResponse> getCustomerStatusDistribution() {
		Map<CustomerStatus, Long> statusCounts = new EnumMap<>(CustomerStatus.class);

		// initialize 0
		for (CustomerStatus status : CustomerStatus.values()) {
			statusCounts.put(status, 0L);
		}

		for (CustomerStatusCountResponse result : customerRepository.distributionByStatus()) {
			statusCounts.put(result.getStatus(), result.getCount());
		}

		// DTO 형식 반환
		return statusCounts.entrySet().stream()
			.map(entry -> new CustomerStatusCountResponse(
				entry.getKey(),
				entry.getValue()
			))
			.toList();
	}

	// 평점별 리뷰 통계
	public List<ProductRatingCountResponse> getRatingDistribution(Long productId) {
		Map<Integer, Long> ratingCounts = new TreeMap<>(Comparator.reverseOrder());

		// initialize 0
		for (int rating = 1; rating <= 5; rating++) {
			ratingCounts.put(rating, 0L);
		}

		for (ProductRatingCountResponse result : reviewRepository.distributionByRatings(productId)) {
			ratingCounts.put(result.getRating(), result.getCount());
		}

		// DTO 형식 반환
		return ratingCounts.entrySet().stream()
			.map(entry -> new ProductRatingCountResponse(
				entry.getKey(),
				entry.getValue()
			))
			.toList();
	}
}
