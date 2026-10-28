package com.mz.commerce.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.dto.product.ProductPageResponse;
import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.RegisterProductRequest;
import com.mz.commerce.service.product.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<ProductResponse> registerProduct(
		/* @SessionAttribute Long id*/
		@Valid @RequestBody RegisterProductRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.registerProduct(request));
	}

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
}
