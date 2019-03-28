package com.czj.blog.blogauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = {"com.czj.blog.blogauth.dao"})
@ServletComponentScan
public class BlogAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogAuthApplication.class, args);
	}

}
