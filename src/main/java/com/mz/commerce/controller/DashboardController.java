package com.mz.commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mz.commerce.dto.dashboard.DashboardChartsResponse;
import com.mz.commerce.dto.dashboard.DashboardRecentOrdersResponse;
import com.mz.commerce.dto.dashboard.DashboardSummaryResponse;
import com.mz.commerce.dto.dashboard.DashboardWidgetResponse;
import com.mz.commerce.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
	private final DashboardService dashboardService;

	@GetMapping("/summary")
	public ResponseEntity<DashboardSummaryResponse> getSummary() {
		return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getSummary());
	}

	@GetMapping("/widget")
	public ResponseEntity<DashboardWidgetResponse> getWidgets() {
		return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getWidgets());
	}

	@GetMapping("/charts")
	public ResponseEntity<DashboardChartsResponse> getChartsData() {
		return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getChartsData());
	}

	@GetMapping("/recent")
	public ResponseEntity<List<DashboardRecentOrdersResponse>> getRecentOrders() {
		return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getRecentOrders());
	}
}
