package com.mz.commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.Login;
import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.dto.order.OrderCancelRequest;
import com.mz.commerce.dto.order.OrderCreateRequest;
import com.mz.commerce.dto.order.OrderDetailResponse;
import com.mz.commerce.dto.order.OrderSummaryResponse;
import com.mz.commerce.service.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Validated
public class OrderController {
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderSummaryResponse> createOrder(@Valid @RequestBody OrderCreateRequest request,
		@Login SessionUser user) {
		OrderSummaryResponse response = orderService.createOrder(request, user.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDetailResponse> getOrder(@Min(1) @PathVariable Long id) {
		return ResponseEntity.ok().body(orderService.getOrder(id));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderSummaryResponse> updateStatus(@Min(1) @PathVariable Long id) {
		return ResponseEntity.ok().body(orderService.updateStatus(id));
	}

	@PatchMapping("/{id}/cancel")
	public ResponseEntity<OrderSummaryResponse> cancel(@Min(1) @PathVariable Long id,
		@Valid @RequestBody OrderCancelRequest request) {
		return ResponseEntity.ok().body(orderService.cancel(request, id));
	}
}