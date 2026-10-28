package com.mz.commerce.entity.product;

import java.time.LocalDateTime;

import com.mz.commerce.entity.BaseEntity;
import com.mz.commerce.entity.admin.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(nullable = false, length = 50)
	private ProductCategory category;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private ProductStatus status;

	//soft delete
	@Column(name = "deleted")
	private LocalDateTime deleted;

	public void softDelete() {
		this.deleted = LocalDateTime.now();
	}

	public Product(Admin admin, String name, ProductCategory category, Long price, int stock, ProductStatus status) {
		this.registeredBy = admin;
		this.name = name;
		this.category = category;
		this.price = price;
		this.stock = stock;
		this.status = status;
	}

	public void update(String name, ProductCategory category, Long price) {
		this.name = name;
		this.category = category;
		this.price = price;
	}

	public void updateStock(int stock) {
		this.stock = stock;
	}

	public void updateStatus(ProductStatus status) {
		this.status = status;
	}
}
