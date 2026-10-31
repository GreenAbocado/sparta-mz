package com.mz.commerce.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.commerce.dto.customer.CustomerResponse;
import com.mz.commerce.entity.customer.Customer;
import com.mz.commerce.entity.customer.CustomerStatus;
import com.mz.commerce.entity.order.OrderStatus;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("""
			SELECT new com.mz.commerce.dto.customer.CustomerResponse(
				c.id,
				c.name,
				c.email,
				c.phone,
				c.status,
				(SELECT COUNT(o.id)
				 FROM Order o
				 WHERE o.customer.id = c.id
				 	AND o.status = :orderStatus
				  	AND o.deleted IS NULL),
				(SELECT COALESCE(SUM(o.totalPrice), 0)
				 FROM Order o
				 WHERE o.customer.id = c.id
				 	AND o.status = :orderStatus
				 	AND o.deleted IS NULL),
				c.createdAt,
				c.modifiedAt
			)
			FROM Customer c
			WHERE c.deleted IS NULL
			  AND (
				  :keyword IS NULL OR :keyword = ''
				  OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
				  OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			  )
			  AND (:status IS NULL OR c.status = :status)
		""")
	Page<CustomerResponse> searchCustomer(
		@Param("keyword") String keyword,
		@Param("status") CustomerStatus status,
		@Param("orderStatus") OrderStatus orderStatus,
		Pageable pageable);

	Optional<Customer> findByIdAndDeletedIsNull(Long customerId);

	boolean existsByEmail(String email);
}
