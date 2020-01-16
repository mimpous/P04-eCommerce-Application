package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.requests.CreateUserRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {
	
	private CreateUserRequest userRequest;

	@BeforeEach
	public void init() {
		userRequest=new CreateUserRequest();
		userRequest.setUsername("Mike");
		userRequest.setPassword("12345"); 
	}
	
	
}
