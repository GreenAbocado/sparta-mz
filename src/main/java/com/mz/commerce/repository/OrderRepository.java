package com.mz.commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.commerce.dto.order.OrderDetailResponse;
import com.mz.commerce.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * 주문 단건 조회 시 사용되는 DTO 프로젝션입니다.
	 *
	 * <p>연관관계 사용으로 인한 3번의 추가 쿼리 제거 및 이후 목록 조회와의 통일성을 부여합니다.
	 * <p>createdBy(관리자)의 경우, nullable이므로 LEFT JOIN으로 관리자 FK가 null이어도 가져올 수 있도록 합니다.
	 */
	@Query("""
		SELECT new com.mz.commerce.dto.order.OrderDetailResponse(
			o.orderNo, c.name, c.email, p.name,
			o.productCount,o.unitPrice * o.productCount,
			o.createdAt, o.status, a.name, a.email, a.role)
		FROM Order o
		JOIN o.customer c
		JOIN o.product p
		LEFT JOIN o.createdBy a
		WHERE o.id = :id
		""")
	Optional<OrderDetailResponse> findByOrderIdInDto(@Param("id") Long id);

}