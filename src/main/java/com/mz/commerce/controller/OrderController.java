package com.mz.commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.Login;
import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.dto.order.OrderCreateRequest;
import com.mz.commerce.dto.order.OrderResponse;
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
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request,
		@Login SessionUser user) {
		OrderResponse response = orderService.createOrder(request, user.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrder(@Min(1) @PathVariable Long id) {
		return ResponseEntity.ok().body(orderService.getOrder(id));
	}
}