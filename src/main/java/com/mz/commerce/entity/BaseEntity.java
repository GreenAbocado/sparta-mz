package com.mz.commerce.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass // 테이블로 생성되지 않고, 상속받는 엔티티에게 필드만 제공
@EntityListeners(AuditingEntityListener.class) // 자동으로 날짜를 매핑해주는 리스너
public abstract class BaseEntity {

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at") // 테이블에 따라 updated_at 등으로 이름 맞춤 조정 가능
	private LocalDateTime modifiedAt;
}
