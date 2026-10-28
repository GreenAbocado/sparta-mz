package com.mz.commerce.entity;

import java.time.LocalDateTime;

import com.mz.commerce.Enumerator.ProductStatus;

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
public class Product {
	//registered_by -> admin_id

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 50)
	private String category;

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

	public Product(String name, String category, Long price, Long stock, ProductStatus status) {
		this.name = name;
		this.category = category;
		this.price = price;
		this.stock = stock;
		this.status = status;
	}
}
