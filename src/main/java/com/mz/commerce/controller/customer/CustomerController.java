package com.mz.commerce.controller.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.HigherAdmin;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.customer.CustomerResponse;
import com.mz.commerce.dto.customer.CustomerUpdateRequest;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.service.customer.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/customers")
public class CustomerController {
	private final CustomerService customerService;

	//고객 등록 -> 생략

	//고객 리스트 조회
	@GetMapping
	public ResponseEntity<PageResponse<CustomerResponse>> getCustomers(
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size,
		@RequestParam(defaultValue = "id") String sortBy,
		@RequestParam(defaultValue = "asc") String sortOrder,
		@RequestParam(required = false) String status) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(customerService.getAllCustomers(keyword, page, size, sortBy, sortOrder, status));
	}

	//고객 상세 조회
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerResponse> getCustomer(
		@PathVariable Long customerId) {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomer(customerId));
	}

	//고객 정보 수정
	@HigherAdmin(AdminRole.OPERATION)
	@PatchMapping("/{customerId}")
	public ResponseEntity<CustomerResponse> updateCustomer(
		@PathVariable Long customerId,
		@Valid @RequestBody CustomerUpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(customerId, request));
	}

	//고객 상태 변경

	//고객 삭제
}
