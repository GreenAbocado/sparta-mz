package com.mz.commerce.dto.customer;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.commerce.entity.customer.CustomerStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CustomerSearchRequest {
	private final String keyword;

	@Min(value = 1, message = "페이지는 {value} 이상이어야 합니다.")
	private final Integer page;

	@Min(value = 1, message = "개수는 {value}개 이상이어야 합니다.")
	@Max(value = 30, message = "개수는 {value}개 이하여야 합니다.")
	private final Integer size;

	@Pattern(regexp = "id|name|email|createdAt|modifiedAt", message = "정렬 기준은 id,name,createdAt,modifiedAt만 가능합니다.")
	private final String sortBy;

	@Pattern(regexp = "asc|desc", message = "정렬 순서는 asc, desc만 가능합니다.")
	private final String sortOrder;

	private final CustomerStatus status;

	public CustomerSearchRequest(String keyword, Integer page, Integer size, String sortBy, String sortOrder,
		CustomerStatus status) {
		this.keyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
		this.page = (page == null) ? 1 : page;
		this.size = (size == null) ? 10 : size;
		this.sortBy = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy.trim();
		this.sortOrder = (sortOrder == null || sortOrder.isBlank()) ? "asc" : sortOrder.trim();
		this.status = status;
	}

	public Pageable toPageable() {
		return PageRequest.of(page - 1, size, Sort.by(
			sortOrder.equals("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy),
			Sort.Order.desc("id")
		));
	}
}
