package com.mz.commerce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.dto.dashboard.DashboardSummaryResponse;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.entity.customer.CustomerStatus;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.CustomerRepository;
import com.mz.commerce.repository.OrderRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
	private final AdminRepository adminRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final ReviewRepository reviewRepository;

	@Transactional
	public DashboardSummaryResponse getSummary() {
		//admin
		long totalAdminCount = adminRepository.count();
		long activeAdminCount = adminRepository.countByStatus(AdminStatus.ACTIVE);

		//customer
		long totalCustomerCount = customerRepository.count();
		long activeCustomerCount = customerRepository.countByStatus(CustomerStatus.ACTIVE);

		//product
		long totalProductCount = productRepository.count();
		long lowStockProductCount = productRepository.countLowStockProducts();

		//orders
		long totalOrderCount = orderRepository.count();
		long todayOrderCount = getTodayOrderCount();

		//review
		long totalReviewCount = reviewRepository.count();
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

	public long getTodayOrderCount() {
		LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay();

		return orderRepository.countTodayOrders(start, end);
	}
}
