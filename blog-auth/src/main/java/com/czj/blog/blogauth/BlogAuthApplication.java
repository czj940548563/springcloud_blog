package com.czj.blog.blogauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BlogAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogAuthApplication.class, args);
	}

}
