package com.mz.commerce.entity.customer;

import static com.mz.commerce.common.exception.ErrorCode.*;

import java.time.LocalDateTime;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false, length = 13)
	@Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private CustomerStatus status;

	//soft delete
	@Column(name = "deleted")
	private LocalDateTime deleted;

	public void softDelete() {
		this.deleted = LocalDateTime.now();
	}

	public Customer(String name, String email, String phone, CustomerStatus status) {
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.status = status;
	}

	public void update(String name, String email, String phone) {
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public void updateStatus(CustomerStatus status) {
		this.status = status;
	}

	public void validateActive() {
		if (this.status != CustomerStatus.ACTIVE) {
			throw new CustomException(CUSTOMER_STATUS_INVALID);
		}
	}
}
