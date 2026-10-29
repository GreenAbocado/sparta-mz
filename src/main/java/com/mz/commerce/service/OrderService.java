package com.mz.commerce.service;

import static com.mz.commerce.common.exception.ErrorCode.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.dto.order.OrderCreateRequest;
import com.mz.commerce.dto.order.OrderResponse;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.order.Order;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.OrderRepository;
import com.mz.commerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final AdminRepository adminRepository;
	private static final Random random = new Random();

	// 주문 번호 UK 중복으로 DB 예외 가능
	@Transactional
	public OrderResponse createOrder(OrderCreateRequest request, Long sessionId) {
		Admin admin = adminRepository.findById(sessionId)
			.orElseThrow(() -> new CustomException(ADMIN_NOT_FOUND));

		Product product = productRepository.findById(request.getProductId())
			.orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

		// Customer customer = 레포지토리 -> 예외

		// ProductStatus에서 상태, Product에서 재고 확인 후 차감 (예외 던짐)
		product.getStatus().validateOrderable();
		product.decreaseStock(request.getQuantity());

		Order order =
			orderRepository.save(new Order(admin, product, makeOrderNo(), request.getQuantity()));
		return new OrderResponse(order);
	}

	private String makeOrderNo() {
		String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		String ranNum = String.format("%06d", random.nextInt(1000000));
		return now + ranNum;
	}
}