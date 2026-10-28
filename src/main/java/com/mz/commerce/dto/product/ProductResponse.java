package com.mz.commerce.dto.product;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.entity.product.ProductStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonPropertyOrder({
	"id",
	"name",
	"category",
	"price",
	"stock",
	"status",
	"createdAt",
	"modifiedAt",
	"registeredBy"
})
@RequiredArgsConstructor
public class ProductResponse {
	private final Long id;
	private final String name;
	private final ProductCategory category;
	private final Long price;
	private final int stock;
	private final ProductStatus status;
	private final LocalDateTime createdAt;
	private final LocalDateTime modifiedAt;
	private final RegisteredByResponse registeredBy;

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.category = product.getCategory();
		this.price = product.getPrice();
		this.stock = product.getStock();
		this.status = product.getStatus();
		this.createdAt = product.getCreatedAt();
		this.modifiedAt = product.getModifiedAt();
		this.registeredBy = new RegisteredByResponse(
			product.getRegisteredBy().getId(),
			product.getRegisteredBy().getName(),
			product.getRegisteredBy().getEmail()
		);
	}

}
