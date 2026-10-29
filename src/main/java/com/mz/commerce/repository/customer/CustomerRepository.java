package com.mz.commerce.repository.customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.commerce.entity.customer.Customer;
import com.mz.commerce.entity.customer.CustomerStatus;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("""
		SELECT c
		FROM Customer c
		WHERE c.deleted IS NULL
		    AND (
		        :keyword IS NULL OR :keyword = ''
		        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
		        OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    )
		    AND (:status IS NULL OR c.status = :status)
		""")
	Page<Customer> searchCustomer(
		@Param("keyword") String keyword,
		@Param("status") CustomerStatus status,
		Pageable pageable);

	Optional<Customer> findByIdAndDeletedIsNull(Long customerId);

	boolean existsByEmailAndDeletedIsNull(String email);
}
