package com.mz.commerce.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.RegisterProductRequest;
import com.mz.commerce.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<ProductResponse> registerProduct(
		/* @SessionAttribute Long id*/
		/* Valid 추가 */ @RequestBody RegisterProductRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.registerProduct(request));
	}

}
