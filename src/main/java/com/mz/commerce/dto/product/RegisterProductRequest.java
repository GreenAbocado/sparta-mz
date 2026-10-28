package com.mz.commerce.dto.product;

import com.mz.commerce.Enumerator.product.ProductStatus;

import lombok.Getter;

@Getter
public class RegisterProductRequest {
	private String name;
	private String category;
	private Long price;
	private Long stock;
	private ProductStatus status;
}
