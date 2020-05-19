package com.example.demoelasticsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/customers")
	public List<Customer> getCustomers() {
		return customerService.findCustomers();
	}

	@GetMapping("/aggregates/gender")
	public Object getGenderAggregates() {
		return customerService.getCustomerAggregateByGenderActive();
	}

	@GetMapping("/customers-by-tags")
	public Object findCustomersWithTags(@RequestParam List<String> tags) {
		return customerService.findCustomersWithTags(tags);
	}

	@GetMapping("/customers-having-tag")
	public Object getCustomerAggregateHavingTagAndEyeColor(@RequestParam String tag, @RequestParam String eyeColor) {
		return customerService.getCustomerAggregateHavingTagAndEyeColor(tag, eyeColor);
	}
}
