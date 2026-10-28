package com.mz.commerce.dto.product;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mz.commerce.Enumerator.product.ProductCategory;
import com.mz.commerce.Enumerator.product.ProductStatus;
import com.mz.commerce.entity.product.Product;

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
	"modifiedAt"
})
@RequiredArgsConstructor
public class ProductResponse {
	private final Long id;
	private final String name;
	private final ProductCategory category;
	private final Long price;
	private final Long stock;
	private final ProductStatus status;
	//private final RegisteredByResponse registeredBy;

	private final LocalDateTime createdAt;
	private final LocalDateTime modifiedAt;

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.category = product.getCategory();
		this.price = product.getPrice();
		this.stock = product.getStock();
		this.status = product.getStatus();
		this.createdAt = product.getCreatedAt();
		this.modifiedAt = product.getModifiedAt();
	}

}
