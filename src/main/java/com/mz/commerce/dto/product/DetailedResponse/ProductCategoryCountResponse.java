package com.mz.commerce.dto.product.DetailedResponse;

import com.mz.commerce.entity.product.ProductCategory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductCategoryCountResponse {
	private final ProductCategory category;
	private final Long count;
}
