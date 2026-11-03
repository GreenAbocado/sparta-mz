package com.mz.commerce.dto.order;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mz.commerce.entity.order.OrderStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

/**
 * 쿼리 파라미터들을 캡슐화하기 위한 모델 애트리뷰트 객체
 *
 * <p>전체 생성자를 통해 기본값 세팅 및 이후 빈 밸리데이션을 통해 각 필드에 맞는 검증을 수행합니다.
 * 이후 {@link com.mz.commerce.repository.OrderRepository}에서 JPQL 쿼리를 날립니다.
 * <br> min, max, pattern 검증, OrderStatus 바인딩 실패
 * 모두 BindingResult에 담겨 GlobalExceptionHandler에서 BindException으로 잡혀 처리됩니다.
 *
 * <p>keyword와 status를 제외한 페이지네이션 관련 모든 필드에는 기본값이 있고 Pageable을 만드는데 사용됩니다.
 *
 * <p>keyword와 status의 경우 JPQL에서 필터링하는데에 사용됩니다. null일 경우는 필터링을 수행하지 않습니다.
 */
@Getter
public class OrderSearchRequest {

	private final String keyword;

	@Min(value = 1, message = "페이지는 {value} 이상이어야 합니다.")
	private final Integer page;

	@Min(value = 1, message = "개수는 {value}개 이상이어야 합니다.")
	@Max(value = 30, message = "개수는 {value}개 이하여야 합니다.")
	private final Integer size;

	@Pattern(regexp = "productCount|totalPrice|createdAt", message = "정렬 기준은 productCount, totalPrice, createdAt만 가능합니다.")
	private final String sortBy;

	@Pattern(regexp = "asc|desc", message = "정렬 순서는 asc, desc만 가능합니다.")
	private final String sortOrder;

	private final OrderStatus status;

	public OrderSearchRequest(String keyword, Integer page, Integer size, String sortBy, String sortOrder,
		OrderStatus status) {

		this.keyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
		this.page = (page == null) ? 1 : page;
		this.size = (size == null) ? 10 : size;
		this.sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy.trim();
		this.sortOrder = (sortOrder == null || sortOrder.isBlank()) ? "desc" : sortOrder.trim();
		this.status = status;
	}

	/**
	 * DTO 필드값들을 기반으로 Pageable을 생성하여 {@link com.mz.commerce.service.OrderService}에 전달
	 *
	 * <p>정렬조건의 값이 동일할 경우, id 내림차순으로 정렬 (최신순)
	 */
	public Pageable toPageable() {
		return PageRequest.of(page - 1, size, Sort.by(
			sortOrder.equals("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy),
			Sort.Order.desc("id")
		));
	}
}