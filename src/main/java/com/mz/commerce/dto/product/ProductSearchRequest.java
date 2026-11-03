package com.mz.commerce.dto.product;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.entity.product.ProductStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ProductSearchRequest {
	private final String keyword;

	@Min(value = 1, message = "페이지는 {value} 이상이어야 합니다.")
	private final Integer page;

	@Min(value = 1, message = "개수는 {value}개 이상이어야 합니다.")
	@Max(value = 30, message = "개수는 {value}개 이하여야 합니다.")
	private final Integer size;

	@Pattern(regexp = "id|name|price|stock|createdAt", message = "정렬 기준은 id,name,price,stock,createdAt만 가능합니다.")
	private final String sortBy;

	@Pattern(regexp = "asc|desc", message = "정렬 순서는 asc, desc만 가능합니다.")
	private final String sortOrder;

	private final ProductCategory category;

	private final ProductStatus status;

	public ProductSearchRequest(String keyword, Integer page, Integer size, String sortBy, String sortOrder,
		ProductCategory category, ProductStatus status) {
		this.keyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
		this.page = (page == null) ? 1 : page;
		this.size = (size == null) ? 10 : size;
		this.sortBy = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy.trim();
		this.sortOrder = (sortOrder == null || sortOrder.isBlank()) ? "asc" : sortOrder.trim();
		this.category = category;
		this.status = status;
	}

	public Pageable toPageable() {
		return PageRequest.of(page - 1, size, Sort.by(
			sortOrder.equals("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy),
			Sort.Order.desc("id")
		));
	}
}
