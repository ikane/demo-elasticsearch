package com.example.demoelasticsearch;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.util.Arrays;

@Configuration
public class Config {

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterBean = new FilterRegistrationBean();
		filterBean.setFilter(new ShallowEtagHeaderFilter());
		filterBean.setUrlPatterns(Arrays.asList("*"));
		return filterBean;
	}
}
