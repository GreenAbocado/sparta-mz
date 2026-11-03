package com.mz.commerce.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.service.admin.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;
}
