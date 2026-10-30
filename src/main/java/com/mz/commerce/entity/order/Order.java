package com.mz.commerce.entity.order;

import static com.mz.commerce.common.exception.ErrorCode.*;

import java.time.LocalDateTime;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.entity.BaseEntity;
import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.customer.Customer;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

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

	@Column(nullable = false)
	private long totalPrice;

	private LocalDateTime deleted;

	public Order(Customer customer, Admin createdBy, Product product, String orderNo, int productCount) {
		this.customer = customer;
		this.createdBy = createdBy;
		this.product = product;
		this.orderNo = orderNo;
		this.productCount = productCount;
		this.status = OrderStatus.PREPARING;
		this.unitPrice = product.getPrice();
		this.totalPrice = productCount * product.getPrice();
	}

	public void updateStatus() {
		this.status = status.next();
	}

	public void cancel(String cancelReason) {
		if (status != OrderStatus.PREPARING) {
			throw new CustomException(ORDER_STATUS_NOT_CANCEL);
		}

		status = OrderStatus.CANCEL;

		this.cancelReason = cancelReason;

		product.increaseStock(productCount);
	}
}