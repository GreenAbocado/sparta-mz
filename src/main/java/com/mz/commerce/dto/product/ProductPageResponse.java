package com.mz.commerce.dto.product;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class ProductPageResponse {
	private final List<ProductResponse> products;
	private final int currentPage;
	private final int pageSize;
	private final long totalElements;
	private final int totalPages;

	public ProductPageResponse(Page<ProductResponse> page) {
		this.products = page.getContent();
		this.currentPage = page.getNumber() + 1;
		this.pageSize = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}

}
