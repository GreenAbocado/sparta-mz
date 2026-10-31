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
import com.mz.commerce.entity.review.Review;
import com.mz.commerce.repository.AdminRepository;
import com.mz.commerce.repository.CustomerRepository;
import com.mz.commerce.repository.OrderRepository;
import com.mz.commerce.repository.ProductRepository;
import com.mz.commerce.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final PasswordEncoder passwordEncoder;
	private final AdminRepository adminRepository;
	private final ProductRepository productRepository;
	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;
	private final ReviewRepository reviewRepository;

	@Override
	public void run(String... args) {
		initializeAdmins();
		initializeCustomers();
		initializeProducts();
		initializeOrders();
		initializeReviews();
	}

	private void initializeAdmins() {
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

		Admin superAdmin2 = new Admin(
			"superadmin2",
			"superadmin2@super.com",
			passwordEncoder.encode("admin123"),
			"010-1234-5578",
			AdminStatus.ACTIVE,
			AdminRole.SUPER
		);

		Admin operationAdmin1 = new Admin(
			"정도윤",
			"doyun.jung@company.com",
			passwordEncoder.encode("admin123"),
			"010-6789-1234",
			AdminStatus.ACTIVE,
			AdminRole.OPERATION
		);

		Admin operationAdmin2 = new Admin(
			"한예린",
			"yerin.han@company.com",
			passwordEncoder.encode("admin123"),
			"010-7890-1234",
			AdminStatus.ACTIVE,
			AdminRole.OPERATION
		);

		Admin csAdmin1 = new Admin(
			"오현우",
			"hyunwoo.oh@company.com",
			passwordEncoder.encode("admin123"),
			"010-8901-2345",
			AdminStatus.ACTIVE,
			AdminRole.CS
		);

		Admin csAdmin2 = new Admin(
			"윤가은",
			"gaeun.yoon@company.com",
			passwordEncoder.encode("admin123"),
			"010-9012-3456",
			AdminStatus.ACTIVE,
			AdminRole.CS
		);

		adminRepository.saveAll(List.of(
			superAdmin, superAdmin2, operationAdmin1, operationAdmin2, csAdmin1, csAdmin2
		));
	}

	private void initializeCustomers() {
		if (customerRepository.count() > 0) {
			return;
		}

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

		Product product1 = new Product(admin, "상품1", ProductCategory.ELECTRONICS, 1000L, 10, false);
		Product product2 = new Product(admin, "상품2", ProductCategory.ELECTRONICS, 50L, 10, true);
		Product product3 = new Product(admin, "상품3", ProductCategory.ELECTRONICS, 10L, 10, true);

		productRepository.save(product1);
		productRepository.save(product2);
		productRepository.save(product3);
	}

	private void initializeOrders() {
		Customer customer = customerRepository.findById(1L).orElseThrow();
		Customer customer2 = customerRepository.findById(2L).orElseThrow();
		Customer customer3 = customerRepository.findById(3L).orElseThrow();

		Admin admin = adminRepository.findById(1L).orElseThrow();
		Product product = productRepository.findById(1L).orElseThrow();
		Product product2 = productRepository.findById(2L).orElseThrow();
		Product product3 = productRepository.findById(3L).orElseThrow();

		Order order = new Order(customer, admin, product, "261030-010239", 5);
		Order order2 = new Order(customer, admin, product2, "261030-052191", 10);
		Order order3 = new Order(customer, admin, product3, "261030-152812", 30);
		Order order4 = new Order(customer2, admin, product, "260110-124921", 40);
		Order order5 = new Order(customer2, admin, product, "260110-124942", 40);

		// ================================== 배송완로된 주문 ======================================
		Order order6 = new Order(customer3, admin, product2, "261031-103821", 3);
		Order order7 = new Order(customer3, admin, product3, "261031-114532", 7);
		Order order8 = new Order(customer2, admin, product2, "261031-125641", 15);
		Order order9 = new Order(customer, admin, product3, "261031-142813", 2);
		Order order10 = new Order(customer2, admin, product, "261031-163924", 8);

		List<Order> completedOrders = List.of(order6, order7, order8, order9, order10);

		completedOrders.forEach(o -> {
			o.updateStatus(); // PREPARING → SHIPPING
			o.updateStatus(); // SHIPPING → COMPLETED
		});
		// ========================================================================================

		orderRepository.saveAll(
			List.of(order, order2, order3, order4, order5, order6, order7, order8, order9, order10));
	}

	private void initializeReviews() {
		Order order6 = orderRepository.findById(6L).orElseThrow();
		Order order7 = orderRepository.findById(7L).orElseThrow();
		Order order8 = orderRepository.findById(8L).orElseThrow();
		Order order9 = orderRepository.findById(9L).orElseThrow();
		Order order10 = orderRepository.findById(10L).orElseThrow();

		// 배송 완료된 주문만 리뷰 작성
		Review review1 = new Review(order6, 5, "배송도 빠르고 상품도 만족스럽습니다.");
		Review review2 = new Review(order7, 4, "상품 품질이 좋습니다.");
		Review review3 = new Review(order8, 5, "기대했던 것보다 훨씬 좋습니다.");
		Review review4 = new Review(order9, 2, "전체적으로 무난합니다.");
		Review review5 = new Review(order10, 4, "가격 대비 만족스러운 상품입니다.");

		reviewRepository.saveAll(List.of(
			review1, review2, review3, review4, review5
		));
	}

}
