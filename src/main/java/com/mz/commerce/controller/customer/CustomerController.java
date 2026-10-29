package com.mz.commerce.controller.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.service.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
	private CustomerService customerService;

	//고객 등록 -> 생략

	//고객 리스트 조회

	//고객 상세 조회

	//고객 정보 수정

	//고객 상태 변경

	//고객 삭제
}
