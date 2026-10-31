package com.mz.commerce.entity.review;

import java.time.LocalDateTime;

import com.mz.commerce.entity.BaseEntity;
import com.mz.commerce.entity.order.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false, unique = true)
	private Order order;

	@Column(nullable = false)
	private int rating;

	@Column(nullable = false)
	private String content;

	private LocalDateTime deleted;

	public void softDelete() {
		this.deleted = LocalDateTime.now();
	}

	public Review(Order order, int rating, String content) {
		this.order = order;
		this.rating = rating;
		this.content = content;
	}
}
