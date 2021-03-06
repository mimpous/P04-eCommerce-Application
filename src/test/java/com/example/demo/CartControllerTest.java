package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.PlainUser;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CartControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private MockHttpServletRequest request;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	CartController cartController;
	// @Mock UserController userController;
	//
	@Autowired
	ItemRepository itemRepository;

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
	public void addToCartTest() throws URISyntaxException, IOException, Exception {
		init("username1");
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1);
		modifyCartRequest.setQuantity(2);
		modifyCartRequest.setUsername("username1");

		MvcResult result = mvc
				.perform(post(new URI("/api/cart/addToCart"))
						.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
						.content(objectMapper.writeValueAsString(modifyCartRequest))
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		Cart returnedCart = objectMapper.readValue(result.getResponse().getContentAsString(), Cart.class);

		Item queryItem = itemRepository.findById(1L).orElseThrow();

		assertEquals(returnedCart.getItems().size(), modifyCartRequest.getQuantity());
		assertEquals(returnedCart.getTotal(),
				(queryItem.getPrice().multiply(BigDecimal.valueOf(modifyCartRequest.getQuantity()))));
	}

	@Test
	public void removeFromCartTest() throws URISyntaxException, IOException, Exception {
		init("username2");
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setItemId(1);
		modifyCartRequest.setQuantity(1);
		modifyCartRequest.setUsername("username2");

		mvc.perform(post(new URI("/api/cart/addToCart"))
				.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
				.content(objectMapper.writeValueAsString(modifyCartRequest))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		modifyCartRequest.setItemId(2);
		modifyCartRequest.setQuantity(1);
		modifyCartRequest.setUsername("username2");

		mvc.perform(post(new URI("/api/cart/addToCart"))
				.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
				.content(objectMapper.writeValueAsString(modifyCartRequest))
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		ModifyCartRequest modifyCartRequest2 = new ModifyCartRequest();
		modifyCartRequest2.setItemId(1);
		modifyCartRequest2.setQuantity(1);
		modifyCartRequest2.setUsername("username2");

		MvcResult result = mvc
				.perform(post(new URI("/api/cart/removeFromCart"))
						.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
						.content(objectMapper.writeValueAsString(modifyCartRequest2))
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		Cart returnedCart = objectMapper.readValue(result.getResponse().getContentAsString(), Cart.class);

		Item queryItem = itemRepository.findById(2L).orElseThrow();

		assertEquals(returnedCart.getItems().size(), modifyCartRequest.getQuantity());
		assertEquals(returnedCart.getTotal(),
				(queryItem.getPrice().multiply(BigDecimal.valueOf(modifyCartRequest.getQuantity()))));
		assertEquals(returnedCart.getItems().get(0).getId(), queryItem.getId());

	}

}
