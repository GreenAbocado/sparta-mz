package com.mz.commerce.dto.common;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

/**
 * 페이지네이션 공통 응답 DTO
 *
 * <p>Page를 기반으로 생성. 1-based 인덱스 적용
 * */

@Getter
public class PageResponse<T> {
	private final List<T> content;
	private final int currentPage;
	private final int pageSize;
	private final long totalElements;
	private final int totalPages;

	public PageResponse(Page<T> page) {
		this.content = page.getContent();
		this.currentPage = page.getNumber() + 1;
		this.pageSize = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}
}