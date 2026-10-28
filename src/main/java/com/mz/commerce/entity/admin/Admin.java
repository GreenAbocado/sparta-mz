package com.mz.commerce.entity.admin;

import java.time.LocalDateTime;

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
@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, length = 13)
	@Pattern(regexp = "^01(?:0|1|[6-9])-?\\d{3,4}-?\\d{4}$", message = "올바른 전화번호 형식이가 아닙니다.")
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private AdminStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private AdminRole role;

	@Column(name = "approved_at")
	private LocalDateTime approvedAt;

	@Column(name = "rejected_at")
	private LocalDateTime rejectedAt;

	@Column(name = "rejection_reason", length = 255)
	private String rejectionReason;

	@Column(nullable = false)
	private Boolean deleted;

	public Admin(String name, String email, String password, String phone, AdminStatus status,
		AdminRole role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.status = status != null ? status : AdminStatus.PENDING;
		this.role = role != null ? role : AdminRole.OPERATION;
		this.deleted = false;
	}
}
