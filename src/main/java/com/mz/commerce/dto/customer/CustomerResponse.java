package com.mz.commerce.dto.customer;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mz.commerce.entity.customer.Customer;
import com.mz.commerce.entity.customer.CustomerStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonPropertyOrder({
	"id",
	"name",
	"email",
	"phone",
	"status",
	"createdAt",
	"modifiedAt",
})
@RequiredArgsConstructor
public class CustomerResponse {
	private final Long id;
	private final String name;
	private final String email;
	private final String phone;
	private final CustomerStatus status;
	private final Long totalOrderCount;
	private final Long totalPurchaseAmount;
	private final LocalDateTime createdAt;
	private final LocalDateTime modifiedAt;

	public CustomerResponse(Customer customer, CustomerOrderSummary customerOrderSummary) {
		this.id = customer.getId();
		this.name = customer.getName();
		this.email = customer.getEmail();
		this.phone = customer.getPhone();
		this.status = customer.getStatus();
		this.createdAt = customer.getCreatedAt();
		this.modifiedAt = customer.getModifiedAt();
		this.totalOrderCount = customerOrderSummary.getTotalOrderCount();
		this.totalPurchaseAmount = customerOrderSummary.getTotalPurchaseAmount();
	}
}
