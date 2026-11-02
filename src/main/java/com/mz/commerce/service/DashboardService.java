package com.mz.commerce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.dto.customer.CustomerStatusCountResponse;
import com.mz.commerce.dto.dashboard.DashboardChartsResponse;
import com.mz.commerce.dto.dashboard.DashboardSummaryResponse;
import com.mz.commerce.dto.dashboard.DashboardWidgetResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductCategoryCountResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductRatingCountResponse;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.entity.customer.CustomerStatus;
import com.mz.commerce.entity.order.OrderStatus;
import com.mz.commerce.entity.product.ProductStatus;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.CustomerRepository;
import com.mz.commerce.repository.OrderRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.ReviewRepository;
import com.mz.commerce.service.common.DistrubutionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
	private final AdminRepository adminRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final ReviewRepository reviewRepository;
	private final DistrubutionUtil distrubutionUtil;

	private final LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

	@Transactional
	public DashboardSummaryResponse getSummary() {
		//admin
		Long totalAdminCount = adminRepository.count();
		Long activeAdminCount = adminRepository.countByStatus(AdminStatus.ACTIVE);

		//customer
		Long totalCustomerCount = customerRepository.count();
		Long activeCustomerCount = customerRepository.countByStatus(CustomerStatus.ACTIVE);

		//product
		Long totalProductCount = productRepository.count();
		Long lowStockProductCount = productRepository.countLowStockProducts();

		//orders
		Long totalOrderCount = orderRepository.count();
		Long todayOrderCount = getTodayOrderCount();

		//review
		Long totalReviewCount = reviewRepository.count();
		Double averageRating = Math.round(reviewRepository.findAverageRating() * 10) / 10.0;

		return new DashboardSummaryResponse(
			totalAdminCount,
			activeAdminCount,
			totalCustomerCount,
			activeCustomerCount,
			totalProductCount,
			lowStockProductCount,
			totalOrderCount,
			todayOrderCount,
			totalReviewCount,
			averageRating
		);
	}

	@Transactional
	public DashboardWidgetResponse getWidgets() {
		Long totalSales = orderRepository.sumSalesByStatus(OrderStatus.COMPLETED);
		Long todaySales = sumTodaySales();
		Long preparingCount = orderRepository.countByStatus(OrderStatus.PREPARING);
		Long deliveringCount = orderRepository.countByStatus(OrderStatus.DELIVERING);
		Long completedCount = orderRepository.countByStatus(OrderStatus.COMPLETED);
		Long lowStockProductCount = productRepository.countLowStockProducts();
		Long soldOutproductCount = productRepository.countByStatus(ProductStatus.SOLD_OUT);

		return new DashboardWidgetResponse(
			totalSales,
			todaySales,
			preparingCount,
			deliveringCount,
			completedCount,
			lowStockProductCount,
			soldOutproductCount
		);
	}

	@Transactional
	public DashboardChartsResponse getChartsData() {
		List<ProductRatingCountResponse> reviewRatingDistribution = distrubutionUtil.getRatingDistribution(null);
		List<CustomerStatusCountResponse> customerStatusDistribution = distrubutionUtil.getCustomerStatusDistribution();
		List<ProductCategoryCountResponse> productCategoryDistribution = distrubutionUtil.getCategoryDistribution();

		return new DashboardChartsResponse(reviewRatingDistribution, customerStatusDistribution,
			productCategoryDistribution);
	}

	private Long getTodayOrderCount() {
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay();

		return orderRepository.countTodayOrders(start, end, null);
	}

	private Long sumTodaySales() {
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay();

		return orderRepository.sumTodaySales(OrderStatus.COMPLETED, start, end);
	}

}
