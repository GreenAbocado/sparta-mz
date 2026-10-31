package com.mz.commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.review.ReviewDeleteResponse;
import com.mz.commerce.dto.review.ReviewResponse;
import com.mz.commerce.dto.review.ReviewSearchRequest;
import com.mz.commerce.service.ReviewService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	// 리뷰 리스트 조회
	@GetMapping
	public ResponseEntity<PageResponse<ReviewResponse>> getReviewsPage(
		@Valid @ModelAttribute ReviewSearchRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsPage(request));
	}

	// 리뷰 상세 조회
	@GetMapping("/{reviewId}")
	public ResponseEntity<ReviewResponse> getReview(@Min(1) @PathVariable Long reviewId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(reviewId));
	}

	// 리뷰 삭제
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<ReviewDeleteResponse> deleteReview(@Min(1) @PathVariable Long reviewId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.deleteReview(reviewId));
	}
}
