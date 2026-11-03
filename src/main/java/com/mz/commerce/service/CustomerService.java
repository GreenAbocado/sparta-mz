package com.mz.commerce.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.customer.CustomerDeleteResponse;
import com.mz.commerce.dto.customer.CustomerOrderSummary;
import com.mz.commerce.dto.customer.CustomerResponse;
import com.mz.commerce.dto.customer.CustomerSearchRequest;
import com.mz.commerce.dto.customer.CustomerUpdateRequest;
import com.mz.commerce.dto.customer.CustomerUpdateStatusRequest;
import com.mz.commerce.entity.customer.Customer;
import com.mz.commerce.entity.customer.CustomerStatus;
import com.mz.commerce.entity.order.OrderStatus;
import com.mz.commerce.repository.CustomerRepository;
import com.mz.commerce.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;

	@Transactional(readOnly = true)
	public PageResponse<CustomerResponse> getAllCustomers(CustomerSearchRequest request) {
		return new PageResponse<>(
			customerRepository.findCustomerPageInDto(request.toPageable(), request.getKeyword(), request.getStatus(),
				OrderStatus.COMPLETED));
	}

	@Transactional(readOnly = true)
	public CustomerResponse getCustomer(Long customerId) {
		Customer customer = customerRepository.findByIdAndDeletedIsNull(customerId).orElseThrow(
			() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

		//총 주문 금액 + 총 주문 수
		CustomerOrderSummary summary = orderRepository.findTotalOrderSummary(customer.getId(), OrderStatus.COMPLETED);

		return new CustomerResponse(customer, summary);
	}

	@Transactional
	public CustomerResponse updateCustomer(Long customerId, CustomerUpdateRequest request) {
		Customer customer = customerRepository.findByIdAndDeletedIsNull(customerId).orElseThrow(
			() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

		//중복 email 검증
		boolean emailExist = customerRepository.existsByEmail(request.getEmail());
		if (emailExist) {
			throw new CustomException(ErrorCode.CUSTOMER_DUPLICATE_EMAIL);
		}

		customer.update(request.getName(), request.getEmail(), request.getPhone());

		//총 주문 금액 + 총 주문 수
		CustomerOrderSummary summary = orderRepository.findTotalOrderSummary(customer.getId(), OrderStatus.COMPLETED);

		return new CustomerResponse(customer, summary);
	}

	@Transactional
	public CustomerResponse updateCustomerStatus(Long customerId, CustomerUpdateStatusRequest request) {
		Customer customer = customerRepository.findByIdAndDeletedIsNull(customerId).orElseThrow(
			() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

		CustomerStatus customerStatus;
		try {
			customerStatus = CustomerStatus.valueOf(request.getStatus());
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.CUSTOMER_STATUS_INVALID);
		}

		customer.updateStatus(customerStatus);

		//총 주문 금액 + 총 주문 수
		CustomerOrderSummary summary = orderRepository.findTotalOrderSummary(customer.getId(), OrderStatus.COMPLETED);

		return new CustomerResponse(customer, summary);
	}

	@Transactional
	public CustomerDeleteResponse deleteCustomer(Long customerId) {
		Customer customer = customerRepository.findByIdAndDeletedIsNull(customerId).orElseThrow(
			() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

		//고개 정보 삭제
		customer.softDelete();

		return new CustomerDeleteResponse(customer.getId(), customer.getName(), customer.getDeleted());
	}
}
