package com.mz.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.commerce.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}