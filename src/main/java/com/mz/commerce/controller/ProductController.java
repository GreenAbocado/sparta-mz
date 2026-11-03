package com.mz.commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.common.authentication.Login;
import com.mz.commerce.common.authentication.RequiredRole;
import com.mz.commerce.common.authentication.SessionUser;
import com.mz.commerce.dto.common.PageResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductDetailResponse;
import com.mz.commerce.dto.product.ProductDeleteResponse;
import com.mz.commerce.dto.product.ProductRegisterRequest;
import com.mz.commerce.dto.product.ProductResponse;
import com.mz.commerce.dto.product.ProductSearchRequest;
import com.mz.commerce.dto.product.ProductUpdateRequest;
import com.mz.commerce.dto.product.ProductUpdateStatusRequest;
import com.mz.commerce.dto.product.ProductUpdateStockRequest;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	//상품 등록 (OP 관리자, SUPER 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@PostMapping
	public ResponseEntity<ProductResponse> registerProduct(
		@Login SessionUser sessionUser,
		@Valid @RequestBody ProductRegisterRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(productService.registerProduct(sessionUser.getId(), request));
	}

	//상품 전체 조회
	@GetMapping
	public ResponseEntity<PageResponse<ProductResponse>> getProducts(
		@Valid @ModelAttribute ProductSearchRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts(request));
	}

	//상품 단 건 조회
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDetailResponse> getProduct(
		@PathVariable Long productId
	) {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(productId));
	}

	//상품 정보 수정 (상품명, 카테고리, 가격) (OP 관리자, 슈퍼 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@PatchMapping("/{productId}")
	public ResponseEntity<ProductResponse> updateProduct(
		@PathVariable Long productId,
		@Valid @RequestBody ProductUpdateRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(productService.updateProduct(productId, request));
	}

	//상품 재고 변경 (OP 관리자, 슈퍼 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@PatchMapping("/{productId}/stock")
	public ResponseEntity<ProductResponse> updateProductStock(
		@PathVariable Long productId,
		@Valid @RequestBody ProductUpdateStockRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(productService.updateProductStock(productId, request));
	}

	//상품 상태 변경 (OP 관리자, 슈퍼 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@PatchMapping("/{productId}/status")
	public ResponseEntity<ProductResponse> updateProductStatus(
		@PathVariable Long productId,
		@Valid @RequestBody ProductUpdateStatusRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(productService.updateProductStatus(productId, request));
	}

	// 상품 삭제 (OP 관리자, 슈퍼 관리자)
	@RequiredRole({AdminRole.SUPER, AdminRole.OPERATION})
	@DeleteMapping("/{productId}")
	public ResponseEntity<ProductDeleteResponse> deleteProduct(
		@PathVariable Long productId
	) {
		return ResponseEntity.ok(productService.deleteProduct(productId));
	}
}