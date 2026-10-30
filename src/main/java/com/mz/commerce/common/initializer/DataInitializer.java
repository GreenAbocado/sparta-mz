package com.mz.commerce.common.initializer;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mz.commerce.entity.admin.Admin;
import com.mz.commerce.entity.admin.AdminRole;
import com.mz.commerce.entity.admin.AdminStatus;
import com.mz.commerce.entity.customer.Customer;
import com.mz.commerce.entity.customer.CustomerStatus;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.OrderRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.customer.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final PasswordEncoder passwordEncoder;
	private final AdminRepository adminRepository;
	private final ProductRepository productRepository;
	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;
	private final PasswordEncoder passwordEncoder;

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
		if (adminRepository.existsByEmail("superadmin@super.com")) {
			return;
		}
		Admin superAdmin = new Admin(
			"superadmin",
			"superadmin@super.com",
			passwordEncoder.encode("admin123"),
			"010-1234-5678",
			AdminStatus.ACTIVE,
			AdminRole.SUPER
		);
		adminRepository.save(superAdmin);
	}

	private void initializeCustomers() {
		if (customerRepository.count() > 0) {
			return;
		}

		// 고객 초기 데이터
		Customer customer1 = new Customer("김민준", "minjun.kim@gmail.com", "010-1234-5678", CustomerStatus.ACTIVE);
		Customer customer2 = new Customer("이서연", "seoyeon.lee@gmail.com", "010-2345-6789", CustomerStatus.ACTIVE);
		Customer customer3 = new Customer("박지훈", "jihoon.park@gmail.com", "010-3456-7890", CustomerStatus.ACTIVE);
		Customer customer4 = new Customer("최유나", "yuna.choi@gmail.com", "010-4567-8901", CustomerStatus.INACTIVE);
		Customer customer5 = new Customer("정하늘", "haneul.jung@gmail.com", "010-5678-9012", CustomerStatus.ACTIVE);
		Customer customer6 = new Customer("강도현", "dohyun.kang@gmail.com", "010-6789-0123", CustomerStatus.ACTIVE);
		Customer customer7 = new Customer("윤서준", "seojun.yoon@gmail.com", "010-7890-1234", CustomerStatus.INACTIVE);
		Customer customer8 = new Customer("한지원", "jiwon.han@gmail.com", "010-8901-2345", CustomerStatus.ACTIVE);
		Customer customer9 = new Customer("신채원", "chaewon.shin@gmail.com", "010-9012-3456", CustomerStatus.ACTIVE);
		Customer customer10 = new Customer("오승민", "seungmin.oh@gmail.com", "010-0123-4567", CustomerStatus.INACTIVE);

		customerRepository.saveAll(List.of(
			customer1, customer2, customer3, customer4, customer5,
			customer6, customer7, customer8, customer9, customer10
		));
	}

	private void initializeProducts() {
		// 상품 초기 데이터
	}

	private void initializeOrders() {
		// 주문 초기 데이터
	}
}
