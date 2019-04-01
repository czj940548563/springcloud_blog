package com.czj.blog.blogauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = {"com.czj.blog.blogauth.dao"})
@ServletComponentScan//druid配置必须添加的注解，扫描druid监控和过滤的注解并注入的容器中
public class BlogAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogAuthApplication.class, args);
	}

	/*@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}*/
}
