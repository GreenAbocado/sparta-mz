package com.mz.commerce.service.admin;

import org.springframework.stereotype.Service;

import com.mz.commerce.repository.admin.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminRepository adminRepository;
}
