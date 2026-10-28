package com.mz.commerce.repository.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.commerce.entity.product.Product;
import com.mz.commerce.enumerator.product.ProductCategory;
import com.mz.commerce.enumerator.product.ProductStatus;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("""
		SELECT p
		FROM Product p
		WHERE p.deleted IS NULL
		  AND (:keyword IS NULL OR :keyword = ''
			   OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
		  AND (:category IS NULL OR p.category = :category)
		  AND (:status IS NULL OR p.status = :status)
		""")
	Page<Product> searchProduct(
		@Param("keyword") String keyword,
		@Param("category") ProductCategory category,
		@Param("status") ProductStatus status,
		Pageable pageable
	);

	Optional<Product> findByIdAndDeletedIsNull(Long productId);
}
