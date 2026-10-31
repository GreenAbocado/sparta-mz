package com.mz.commerce.dto.review;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ReviewSearchRequest {

	private final String keyword;

	@Min(value = 1, message = "페이지는 {value} 이상이어야 합니다.")
	private final Integer page;

	@Min(value = 1, message = "개수는 {value}개 이상이어야 합니다.")
	@Max(value = 30, message = "개수는 {value}개 이하여야 합니다.")
	private final Integer size;

	@Pattern(regexp = "rating|createdAt", message = "정렬 기준은 rating, createdAt만 가능합니다.")
	private final String sortBy;

	@Pattern(regexp = "asc|desc", message = "정렬 순서는 asc, desc만 가능합니다.")
	private final String sortOrder;

	@Min(value = 1, message = "평점은 {value} 이상이어야 합니다.")
	@Max(value = 5, message = "평점은 {value} 이하여야 합니다.")
	private final Integer rating;

	public ReviewSearchRequest(String keyword, Integer page, Integer size, String sortBy, String sortOrder,
		Integer rating) {
		this.keyword = (keyword == null || keyword.isBlank())
			? null : keyword.trim();
		this.page = (page == null) ? 1 : page;
		this.size = (size == null) ? 10 : size;
		this.sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
		this.sortOrder = (sortOrder == null || sortOrder.isBlank()) ? "desc" : sortOrder.trim();
		this.rating = rating;
	}

	/**
	 * DTO 필드값을 기반으로 Pageable을 생성하여 ReviewService에 전달
	 *
	 * 정렬조건의 값이 동일할 경우 id 내림차순으로 정렬
	 */
	public Pageable toPageable() {
		return PageRequest.of(
			page - 1,
			size,
			Sort.by(
				sortOrder.equals("asc")
					? Sort.Order.asc(sortBy)
					: Sort.Order.desc(sortBy),
				Sort.Order.desc("id")
			)
		);
	}
}

