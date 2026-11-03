package com.mz.commerce.service;

import static com.mz.commerce.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.review.ReviewDeleteResponse;
import com.mz.commerce.dto.review.ReviewResponse;
import com.mz.commerce.dto.review.ReviewSearchRequest;
import com.mz.commerce.entity.review.Review;
import com.mz.commerce.repository.ReviewRepository;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public ReviewResponse getReview(Long reviewId) {
		return reviewRepository.findByReviewIdInDto(reviewId).orElseThrow(
			() -> new CustomException(REVIEW_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public PageResponse<ReviewResponse> getReviewsPage(ReviewSearchRequest request) {
		return new PageResponse<>(
			reviewRepository.findReviewPageInDto(request.toPageable(), request.getKeyword(), request.getRating()));
	}

	@Transactional
	public ReviewDeleteResponse deleteReview(@Min(1) Long reviewId) {
		Review review = reviewRepository.findByIdAndDeletedIsNull(reviewId).orElseThrow(
			() -> new CustomException(REVIEW_NOT_FOUND));

		review.softDelete();

		return new ReviewDeleteResponse(review.getId(), review.getDeleted());
	}
}


