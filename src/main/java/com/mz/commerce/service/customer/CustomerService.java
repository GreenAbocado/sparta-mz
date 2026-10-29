package com.mz.commerce.service.customer;

import org.springframework.stereotype.Service;

import com.mz.commerce.repository.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;

}
