package com.mz.commerce.entity.product;

import static com.mz.commerce.common.exception.ErrorCode.*;

import java.time.LocalDateTime;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.entity.BaseEntity;
import com.mz.commerce.entity.admin.Admin;

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

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "registered_by")
	private Admin registeredBy;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private ProductCategory category;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private Integer stock;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductStatus status;

	//soft delete
	@Column(name = "deleted")
	private LocalDateTime deleted;

	public void softDelete() {
		this.deleted = LocalDateTime.now();
	}

	public Product(Admin admin, String name, ProductCategory category, Long price, int stock, Boolean discontinued) {
		this.registeredBy = admin;
		this.name = name;
		this.category = category;
		this.price = price;
		this.stock = stock;

		if (Boolean.TRUE.equals(discontinued)) {
			this.status = ProductStatus.DISCONTINUED;
		} else {
			updateStatusByStock();
		}
	}

	public void update(String name, ProductCategory category, Long price) {
		this.name = name;
		this.category = category;
		this.price = price;
	}

	//재고에 따라 상태도 변경
	public void updateStock(int stock) {
		this.stock = stock;
		updateStatusByStock();
	}

	private void updateStatusByStock() {
		if (this.status == ProductStatus.DISCONTINUED) {
			return;
		}

		this.status = this.stock <= 0
			? ProductStatus.SOLD_OUT
			: ProductStatus.ON_SALE;
	}

	//상태만 변경
	public void updateStatus(Boolean setAsDiscontinued) {
		if (Boolean.TRUE.equals(setAsDiscontinued)) {
			this.status = ProductStatus.DISCONTINUED;
		} else {
			this.status = this.stock <= 0
				? ProductStatus.SOLD_OUT
				: ProductStatus.ON_SALE;
		}
	}



	public void decreaseStock(int amount) {
		if (stock < amount) {
			throw new CustomException(PRODUCT_OUT_OF_STOCK);
		}
		stock -= amount;

		if (stock == 0) {
			status = ProductStatus.SOLD_OUT;
		}
	}
}
