package com.mz.commerce.entity.order;

import java.time.LocalDateTime;

import com.mz.commerce.entity.BaseEntity;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 *
	 *
	 *
	 * CUSTOMER 연관 추가 필요
	 *
	 * <p>
	 *
	 * */

	// nullable인 관계로 optional, nullable true
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Admin createdBy;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(unique = true, nullable = false, length = 25)
	private String orderNo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private OrderStatus status;

	// nullable
	@Column(length = 255)
	private String cancelReason;

	@Column(nullable = false)
	private int productCount;

	@Column(nullable = false)
	private long unitPrice;

	private LocalDateTime deleted;

	public long getTotalPrice() {
		return productCount * unitPrice;
	}

	public Order( /* customer */Admin createdBy, Product product, String orderNo, int productCount) {
		// this.customer =
		this.createdBy = createdBy;
		this.product = product;
		this.orderNo = orderNo;
		this.productCount = productCount;
		this.status = OrderStatus.PREPARING;
		this.unitPrice = product.getPrice();
	}

	public void updateStatus() {
		this.status = status.next();
	}
}