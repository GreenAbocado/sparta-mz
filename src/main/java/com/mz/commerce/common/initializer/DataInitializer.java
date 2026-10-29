package com.mz.commerce.common.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.repository.admin.AdminRepository;
import com.mz.commerce.repository.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final AdminRepository adminRepository;
	private final ProductRepository productRepository;
	//private final CustomerRepository customerRepository;
	//private final OrderRepository orderRepository;

	@Override
	public void run(String... args) {
		initializeAdmins();
		initializeCustomers();
		initializeProducts();
		initializeOrders();
	}

	private void initializeAdmins() {
		// 관리자 초기 데이터

		//그냥 테스트용입니다! 변경하셔도 됩니다
		if (adminRepository.existsByEmail("superadmin@example.com")) {
			return;
		}
		Admin superAdmin = new Admin(
			"superadmin",
			"superadmin@super.com",
			"admin123",
			"010-1234-5678",
			AdminStatus.ACTIVE,
			AdminRole.SUPER
		);
		adminRepository.save(superAdmin);
	}

	private void initializeCustomers() {
		// 고객 초기 데이터
	}

	private void initializeProducts() {
		// 상품 초기 데이터
	}

	private void initializeOrders() {
		// 주문 초기 데이터
	}
}
