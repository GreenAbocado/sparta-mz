package com.mz.commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.RequiredRole;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.customer.CustomerDeleteResponse;
import com.mz.commerce.dto.customer.CustomerResponse;
import com.mz.commerce.dto.customer.CustomerSearchRequest;
import com.mz.commerce.dto.customer.CustomerUpdateRequest;
import com.mz.commerce.dto.customer.CustomerUpdateStatusRequest;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/customers")
public class CustomerController {
	private final CustomerService customerService;

	//고객 리스트 조회
	@GetMapping
	public ResponseEntity<PageResponse<CustomerResponse>> getCustomers(
		@Valid @ModelAttribute CustomerSearchRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers(request));
	}

	//고객 상세 조회
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerResponse> getCustomer(
		@PathVariable Long customerId) {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomer(customerId));
	}

	//고객 정보 수정 (SUPER 관리자, OP 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@PatchMapping("/{customerId}")
	public ResponseEntity<CustomerResponse> updateCustomer(
		@PathVariable Long customerId,
		@Valid @RequestBody CustomerUpdateRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(customerId, request));
	}

	//고객 상태 변경 (SUPER 관리자, OP 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@PatchMapping("/{customerId}/status")
	public ResponseEntity<CustomerResponse> updateCustomerStatus(
		@PathVariable Long customerId,
		@Valid @RequestBody CustomerUpdateStatusRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomerStatus(customerId, request));
	}

	//고객 삭제 (SUPER 관리자, OP 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@DeleteMapping("/{customerId}")
	public ResponseEntity<CustomerDeleteResponse> deleteCustomer(
		@PathVariable Long customerId) {
		return ResponseEntity.ok(customerService.deleteCustomer(customerId));
	}
}
