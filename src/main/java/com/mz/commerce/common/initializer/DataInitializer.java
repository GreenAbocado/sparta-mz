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
import com.mz.commerce.entity.order.Order;
import com.mz.commerce.entity.product.Product;
import com.mz.commerce.entity.product.ProductCategory;
import com.mz.commerce.entity.product.ProductStatus;
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
		Admin admin = adminRepository.findById(1L).orElseThrow();

		Product product1 = new Product(admin, "상품1", ProductCategory.ELECTRONICS, 1000L, 10, ProductStatus.ON_SALE);
		Product product2 = new Product(admin, "상품2", ProductCategory.ELECTRONICS, 50L, 10,
			ProductStatus.ON_SALE);
		Product product3 = new Product(admin, "상품3", ProductCategory.ELECTRONICS, 10L, 10, ProductStatus.ON_SALE);

		productRepository.save(product1);
		productRepository.save(product2);
		productRepository.save(product3);
	}

	private void initializeOrders() {
		Customer customer = customerRepository.findById(1L).orElseThrow();
		Customer customer2 = customerRepository.findById(2L).orElseThrow();

		Admin admin = adminRepository.findById(1L).orElseThrow();
		Product product = productRepository.findById(1L).orElseThrow();
		Product product2 = productRepository.findById(2L).orElseThrow();
		Product product3 = productRepository.findById(3L).orElseThrow();

		Order order = new Order(customer, admin, product, "261030-010239", 5);
		Order order2 = new Order(customer, admin, product2, "261030-052191", 10);
		Order order3 = new Order(customer, admin, product3, "261030-152812", 30);
		Order order4 = new Order(customer2, admin, product, "260110-124921", 40);
		Order order5 = new Order(customer2, admin, product, "260110-124942", 40);

		orderRepository.saveAll(List.of(order, order2, order3, order4, order5));
		// 주문 초기 데이터
	}
}
