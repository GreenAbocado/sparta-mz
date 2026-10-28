package com.mz.commerce.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.dto.product.DeleteProductResponse;
import com.mz.commerce.dto.product.ProductPageResponse;
import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.RegisterProductRequest;
import com.mz.commerce.dto.product.UpdateProductRequest;
import com.mz.commerce.dto.product.UpdateProductStockRequest;
import com.mz.commerce.service.product.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	//상품 등록
	@PostMapping
	public ResponseEntity<ProductResponse> registerProduct(
		/* @SessionAttribute Long id*/
		@Valid @RequestBody RegisterProductRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.registerProduct(request));
	}

	//상품 전체 조회
	@GetMapping
	public ResponseEntity<ProductPageResponse> getProducts(
		@RequestParam(required = false) String keyword,
		@Valid @RequestParam(defaultValue = "1") @Min(1) int page,
		@Valid @RequestParam(defaultValue = "10") @Min(1) int size,
		@RequestParam(defaultValue = "price") String sortBy,
		@RequestParam(defaultValue = "desc") String sortOrder,
		@RequestParam(required = false) String category,
		@RequestParam(required = false) String status
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(productService.getAllProducts(keyword, page, size, sortBy, sortOrder, category, status));
	}

	//상품 단 건 조회
	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponse> getProduct(
		@PathVariable Long productId
	) {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(productId));
	}

	//상품 정보 수정 (상품명, 카테고리, 가격)
	@PatchMapping("/{productId}")
	public ResponseEntity<ProductResponse> updateProduct(
		@PathVariable Long productId,
		@Valid @RequestBody UpdateProductRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId, request));
	}

	//상품 재고 변경
	@PatchMapping("/{productId}/stock")
	public ResponseEntity<ProductResponse> updateProductStock(
		@PathVariable Long productId,
		@Valid @RequestBody UpdateProductStockRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK).body(productService.updateProductStock(productId, request));
	}

	// 상품 삭제
	@DeleteMapping("/{productId}")
	public ResponseEntity<DeleteProductResponse> deleteProduct(
		@PathVariable Long productId
	) {
		DeleteProductResponse response = productService.deleteProduct(productId);
		return ResponseEntity.ok(response);
	}
}
