package com.mz.commerce.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mz.commerce.entity.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
