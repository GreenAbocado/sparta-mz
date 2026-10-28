package com.mz.commerce.entity.product;

import java.time.LocalDateTime;

import com.mz.commerce.entity.BaseEntity;
import com.mz.commerce.enumerator.product.ProductCategory;
import com.mz.commerce.enumerator.product.ProductStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
	//registered_by -> admin_id

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 50)
	private ProductCategory category;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private Long stock;

	@Column(nullable = false)
	private ProductStatus status;

	//soft delete
	@Column(name = "deleted")
	private LocalDateTime deleted;

	public void softDelete() {
		this.deleted = LocalDateTime.now();
	}

	public Product(String name, ProductCategory category, Long price, Long stock, ProductStatus status) {
		this.name = name;
		this.category = category;
		this.price = price;
		this.stock = stock;
		this.status = status;
	}
}
