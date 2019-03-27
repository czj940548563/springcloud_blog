package com.czj.blog.bloggateway;

import com.czj.blog.bloggateway.filter.RequestTimeGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogGatewayApplication.class, args);
	}

	@Bean
	public RequestTimeGatewayFilterFactory requestTimeGatewayFilterFactory() {
		return new RequestTimeGatewayFilterFactory();
	}

}
