package com.mz.commerce.service;

import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mz.commerce.common.exception.CustomException;
import com.mz.commerce.common.exception.ErrorCode;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.customer.CustomerDeleteResponse;
import com.mz.commerce.dto.customer.CustomerOrderSummary;
import com.mz.commerce.dto.customer.CustomerResponse;
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
	public PageResponse<CustomerResponse> getAllCustomers(String keyword, int page, int size, String sortBy,
		String sortOrder, String status) {

		// 1.status 검증
		CustomerStatus customerStatus = null;
		if (status != null && !status.isBlank()) {
			try {
				customerStatus = CustomerStatus.valueOf(status);
			} catch (IllegalArgumentException e) {
				throw new CustomException(ErrorCode.CUSTOMER_STATUS_INVALID);
			}
		}

		System.out.println(">>> reached");

		// 2. page + size 검증 -> controller에서 Bean validation

		// 3. 정렬 기준 검증
		String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
		Set<String> allowedSorts = Set.of("id", "name", "email", "phone", "status", "createdAt", "modifiedAt");

		if (!allowedSorts.contains(sortField)) {
			throw new CustomException(ErrorCode.CUSTOMER_SORT_FIELD_INVALID);
		}

		//4. 정렬 순서 검증
		String direction = (sortOrder == null || sortOrder.isBlank())
			? "desc"
			: sortOrder.toLowerCase(Locale.ROOT);
		System.out.println(direction);
		Sort sort;
		if (direction.equals("asc")) {
			sort = Sort.by(sortField).ascending();
		} else if (direction.equals("desc")) {
			sort = Sort.by(sortField).descending();
		} else {
			throw new CustomException(ErrorCode.SORT_ORDER_INVALID);
		}

		//5.Page
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<CustomerResponse> responsePage = customerRepository.searchCustomer(
			keyword,
			customerStatus,
			OrderStatus.COMPLETED,
			pageable
		);

		return new PageResponse<>(responsePage);
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
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
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
