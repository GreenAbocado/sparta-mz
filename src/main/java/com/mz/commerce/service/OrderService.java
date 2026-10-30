package com.mz.commerce.service;

import static com.mz.commerce.common.exception.ErrorCode.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.order.OrderCancelRequest;
import com.mz.commerce.dto.order.OrderCreateRequest;
import com.mz.commerce.dto.order.OrderDetailResponse;
import com.mz.commerce.dto.order.OrderSearchRequest;
import com.mz.commerce.dto.order.OrderSummaryResponse;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.customer.Customer;
import com.mz.commerce.entity.order.Order;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.OrderRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final AdminRepository adminRepository;
	private final CustomerRepository customerRepository;
	private static final Random RANDOM = new Random();

	// 주문 번호 UK 중복으로 DB 예외 가능
	@Transactional
	public OrderSummaryResponse createOrder(OrderCreateRequest request, Long sessionId) {
		Admin admin = adminRepository.findByIdAndDeletedIsNull(sessionId)
			.orElseThrow(() -> new CustomException(ADMIN_NOT_FOUND));

		Product product = productRepository.findByIdAndDeletedIsNull(request.getProductId())
			.orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

		Customer customer = customerRepository.findByIdAndDeletedIsNull(request.getCustomerId())
			.orElseThrow(()-> new CustomException(CUSTOMER_NOT_FOUND));

		customer.validateActive();

		// ProductStatus에서 상태, Product에서 재고 확인 후 차감 (예외 던짐)
		product.getStatus().validateOrderable();
		product.decreaseStock(request.getQuantity());

		Order order =
			orderRepository.save(new Order(customer, admin, product, makeOrderNo(), request.getQuantity()));
		return new OrderSummaryResponse(order);
	}

	@Transactional(readOnly = true)
	public PageResponse<OrderSummaryResponse> getOrders(OrderSearchRequest request) {
		return new PageResponse<>(
			orderRepository.findOrderPageInDto(request.toPageable(), request.getKeyword(), request.getStatus()));
	}

	// soft delete 여부 JPQL에서 직접 적용
	@Transactional(readOnly = true)
	public OrderDetailResponse getOrder(Long orderId) {
		return orderRepository.findByOrderIdInDto(orderId)
			.orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
	}

	@Transactional
	public OrderSummaryResponse updateStatus(Long orderId) {
		Order order = getOrderById(orderId);
		order.updateStatus();
		return new OrderSummaryResponse(order);
	}

	@Transactional
	public OrderSummaryResponse cancel(OrderCancelRequest request, Long orderId) {
		Order order = getOrderById(orderId);
		order.cancel(request.getCancelReason());
		return new OrderSummaryResponse(order);
	}

	private Order getOrderById(Long id) {
		return orderRepository.findByIdAndDeletedIsNull(id)
			.orElseThrow(() -> new CustomException(ORDER_NOT_FOUND));
	}

	private String makeOrderNo() {
		String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String ranNum = String.format("%06d", RANDOM.nextInt(1000000));
		return now + ranNum;
	}
}