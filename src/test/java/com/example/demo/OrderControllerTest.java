package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.model.persistence.PlainUser;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class OrderControllerTest {

	
	@Autowired
	private MockMvc mvc;

	@Autowired
	private MockHttpServletRequest request;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private UserRepository userRepository;
 
	//
	@Autowired
	OrderRepository orderRepository;

	private User user;

	private void init(String userName) throws URISyntaxException, IOException, Exception {
		CreateUserRequest createUser = new CreateUserRequest();
		createUser.setUsername(userName);
		createUser.setPassword("123456789");
		createUser.setConfirmPassword("123456789");

		MvcResult entityResult = mvc
				.perform(MockMvcRequestBuilders.post("/api/user/create")
						.content(objectMapper.writeValueAsString(createUser))
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		user = objectMapper.readValue(entityResult.getResponse().getContentAsString(), User.class);

		String jsonStr = objectMapper.writeValueAsString(new PlainUser(user.getUsername(), createUser.getPassword()));

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login").content(jsonStr))
				.andExpect(status().isOk()).andReturn();

		request.addParameter("Authorization", result.getResponse().getHeader("Authorization"));
	}
	
	@Test
	public void getOrdersForUserTest() throws URISyntaxException, IOException, Exception {
		init("userName31");
		
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1);
		modifyCartRequest.setQuantity(2);
		modifyCartRequest.setUsername("userName31");

		 mvc.perform(post(new URI("/api/cart/addToCart"))
						.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
						.content(objectMapper.writeValueAsString(modifyCartRequest))
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
		 
		 MvcResult result =  mvc.perform(post(new URI("/api/order/submit/" + modifyCartRequest.getUsername() ))
					.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
					.content(objectMapper.writeValueAsString(modifyCartRequest))
					.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk()).andReturn();
		 
		 UserOrder returnedOrder = objectMapper.readValue(result.getResponse().getContentAsString(), UserOrder.class);
		 
		 List<UserOrder> orderList= orderRepository.findByUser(user);
		 
		 assertEquals(returnedOrder.getTotal() , orderList.get(0).getTotal());
		
	}
	
	@Test
	public void getOrdersHistoryTest() throws URISyntaxException, IOException, Exception {
		init("userName32");
		
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1);
		modifyCartRequest.setQuantity(2);
		modifyCartRequest.setUsername("userName32");

		 mvc.perform(post(new URI("/api/cart/addToCart"))
						.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
						.content(objectMapper.writeValueAsString(modifyCartRequest))
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
		 
		 mvc.perform(post(new URI("/api/order/submit/" + modifyCartRequest.getUsername() ))
					.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
					.content(objectMapper.writeValueAsString(modifyCartRequest))
					.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk());
		 
		 MvcResult result =mvc.perform(get(new URI("/api/order/history/" + modifyCartRequest.getUsername() ))
					.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization")) 
					.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk()).andReturn();
		 
		 
		List<UserOrder> returnedOrders = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		
		List<UserOrder> orderList= orderRepository.findByUser(user);
		 
		 assertEquals(returnedOrders.size(), orderList.size());
		
	}
	
 
}


