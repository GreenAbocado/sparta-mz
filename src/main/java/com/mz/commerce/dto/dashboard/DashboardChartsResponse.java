package com.mz.commerce.dto.dashboard;

import java.util.List;

import com.mz.commerce.dto.customer.CustomerStatusCountResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductCategoryCountResponse;
import com.mz.commerce.dto.product.DetailedResponse.ProductRatingCountResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashboardChartsResponse {
	private final List<ProductRatingCountResponse> reviewRatingDistribution;
	private final List<CustomerStatusCountResponse> customerStatusDistribution;
	private final List<ProductCategoryCountResponse> productCategoryDistribution;
}
