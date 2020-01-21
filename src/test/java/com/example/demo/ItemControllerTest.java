package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.PlainUser;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper; 
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ItemControllerTest {

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
	
 
	@SuppressWarnings("unchecked")
	@Test
	public void getItemsTest() throws URISyntaxException, IOException, Exception {
		init("username11"); 

		MvcResult result = mvc
				.perform(get(new URI("/api/item"))
						.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization")) 
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		List<Item> returnedItems = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);

		assertEquals(returnedItems.size(), itemRepository.count());
	}

 
	@Test
	public void getItemTest() throws URISyntaxException, IOException, Exception {
		init("username12"); 

		MvcResult result = mvc
				.perform(get(new URI("/api/item/1"))
						.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization")) 
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		Item returnedItem = objectMapper.readValue(result.getResponse().getContentAsString(), Item.class);

		assertEquals(returnedItem  , itemRepository.findById(1L).orElseThrow());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getItemByNameTest() throws URISyntaxException, IOException, Exception {
		init("username13"); 

		
		MvcResult result = mvc
				.perform(get(new URI("/api/item/name/Square%20Widget"))
						.header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization")) 
						.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		List<Item> returnedItem = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);

		assertEquals(returnedItem.size(), itemRepository.findByName("Square Widget").size());
	}

}
