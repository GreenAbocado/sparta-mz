package com.mz.commerce.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.commerce.dto.review.ReviewResponse;
import com.mz.commerce.entity.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@Query("""
		SELECT new com.mz.commerce.dto.review.ReviewResponse(
			r.id,
			o.orderNo,
			o.customer.name,
			o.product.name,
			r.rating,
			r.content,
			r.createdAt
		)
		FROM Review r				
		JOIN r.order o
		WHERE r.id=:id AND r.deleted IS null
		""")
	Optional<ReviewResponse> findByReviewIdInDto(@Param("id") Long reviewId);

	@Query("""
		SELECT new com.mz.commerce.dto.review.ReviewResponse(
			r.id,
			o.orderNo,
			o.customer.name,
			o.product.name,
			r.rating,
			r.content,
			r.createdAt
		)
		FROM Review r
		JOIN r.order o
		WHERE r.deleted IS NULL
		    AND (
				:keyword IS NULL
		        OR LOWER(o.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
		        OR LOWER(o.product.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    )
		    AND (:rating IS NULL OR r.rating = :rating)
		""")
	Page<ReviewResponse> findReviewPageInDto(Pageable pageable, @Param("keyword") String keyword,
		@Param("rating") Integer rating);

	Optional<Review> findByIdAndDeletedIsNull(Long reviewId);
}
