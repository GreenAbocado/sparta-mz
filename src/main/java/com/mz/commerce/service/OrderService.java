package com.mz.commerce.service;

import org.springframework.stereotype.Service;

import com.mz.commerce.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
}
