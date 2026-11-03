package com.mz.commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mz.commerce.dto.product.DetailedResponse.ProductCategoryCountResponse;
import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.entity.product.ProductStatus;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("""
		SELECT new com.mz.commerce.dto.product.ProductResponse(
			p.id,
			p.name,
			p.category,
			p.price,
			p.stock,
			p.status,
			p.createdAt,
			p.modifiedAt,
			new com.mz.commerce.dto.product.RegisteredByResponse(
				p.registeredBy.id,
				p.registeredBy.name,
				p.registeredBy.email
			)
		)
		FROM Product p
		WHERE p.deleted IS NULL
		  AND (:keyword IS NULL OR :keyword = ''
			   OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
		  AND (:category IS NULL OR p.category = :category)
		  AND (:status IS NULL OR p.status = :status)
		""")
	Page<ProductResponse> searchProduct(
		Pageable pageable,
		@Param("keyword") String keyword,
		@Param("category") ProductCategory category,
		@Param("status") ProductStatus status
	);

	Optional<Product> findByIdAndDeletedIsNull(Long productId);

	@Query("""
		    SELECT COUNT(p)
		    FROM Product p
		    WHERE p.stock <= 5
			AND p.deleted IS NULL
		""")
	long countLowStockProducts();

	Long countByStatus(ProductStatus productStatus);

	@Query("""
			SELECT new com.mz.commerce.dto.product.DetailedResponse.ProductCategoryCountResponse(
				p.category,
				COUNT(p)
			)
			FROM Product p
			WHERE p.deleted IS NULL
			GROUP BY p.category
			ORDER BY p.category ASC
		""")
	List<ProductCategoryCountResponse> distibutionByCategory();
}
