package com.example.demo;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.model.persistence.PlainUser;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest { 
	

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateUserRequest> json;
    
    @Autowired
    private MockHttpServletRequest request;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Mock
    private UserRepository userRepository;
    
	/**
	 * Test for create a User With UserName
	 * 
	 * @throws Exception when User creation fails in the system
	 */
	@Test
	public void createUserWithUserName() throws Exception {
		CreateUserRequest createUser = new CreateUserRequest();
		createUser.setUsername("MyName1");
		createUser.setPassword("123456789");
		createUser.setConfirmPassword("123456789");

		mvc.perform(post(new URI("/api/user/create")).content(json.write(createUser).getJson())
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

	}

	/**
	 * Test for create a User With UserName
	 * 
	 * @throws Exception when User creation fails in the system
	 */
	@Test
	public void createUserWithUserNameNoPassword() throws Exception {
		CreateUserRequest createUser = new CreateUserRequest();
		createUser.setUsername("MyName2");

		mvc.perform(post(new URI("/api/user/create")).content(json.write(createUser).getJson())
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());

	}

	/**
	 * Test for create a User With UserName
	 * 
	 * @throws Exception when User creation fails in the system
	 */
	@Test
	public void createUserWithUserNamePasswordNotEqual() throws Exception {
		CreateUserRequest createUser = new CreateUserRequest();
		createUser.setUsername("MyName3");
		createUser.setPassword("XXXXXXX");
		createUser.setConfirmPassword("YYYYYYY");

		mvc.perform(post(new URI("/api/user/create")).content(json.write(createUser).getJson())
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());

	}

	/**
	 * Test for create a User With UserName
	 * 
	 * @throws Exception when User creation fails in the system
	 */
	@Test
	public void createUserWithUserNamePasswordSizeNotMatch() throws Exception {
		CreateUserRequest createUser = new CreateUserRequest();
		createUser.setUsername("MyName4");
		createUser.setPassword("XXXXXXX");
		createUser.setConfirmPassword("XXXXXXXXXX");

		mvc.perform(post(new URI("/api/user/create")).content(json.write(createUser).getJson())
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());

	}
     	
    private User 	user; 
     
 	private void init(String userName ) throws URISyntaxException, IOException, Exception {
		CreateUserRequest createUser = new CreateUserRequest();
	  	createUser.setUsername(userName);
	  	createUser.setPassword("123456789");
	  	createUser.setConfirmPassword("123456789");
	    	 
	  	MvcResult entityResult=  mvc.perform(
	  			MockMvcRequestBuilders.post( "/api/user/create")
	                      .content(objectMapper.writeValueAsString(createUser)) 
	                      .contentType(MediaType.APPLICATION_JSON_UTF8)
	                      .accept(MediaType.APPLICATION_JSON_UTF8)) 
	                      .andExpect(status().isOk()).andReturn();       
	       
	  	  
	      
	      user = objectMapper.readValue(entityResult.getResponse().getContentAsString(), User.class);
	      
	      String jsonStr = objectMapper.writeValueAsString( new PlainUser(user.getUsername() , createUser.getPassword()) );
	      
	      
	      MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login").content(jsonStr))
	      		.andExpect( status().isOk()).andReturn();
	        
	      request.addParameter("Authorization", result.getResponse().getHeader("Authorization")); 	 
	}
     
	@Test
	public void findByUserNameTest() throws Exception  {
		init("MyName5");
		MvcResult result = mvc.perform(  get(new URI("/api/user/" + user.getUsername())
	                		).header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
	                        .contentType(MediaType.APPLICATION_JSON_UTF8)
	                        .accept(MediaType.APPLICATION_JSON_UTF8))
	                        .andExpect(status().isOk()).andReturn();
		 User returnedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		 assertEquals(user.getUsername(), returnedUser.getUsername());
		 
		 request.close();
		  
	 }
	
	@Test
	public void findByUserId() throws Exception  {
		init("MyName6");
		MvcResult result = mvc.perform(  get(new URI("/api/user/id/" + user.getId())
	                		).header(HttpHeaders.AUTHORIZATION, request.getParameter("Authorization"))
	                        .contentType(MediaType.APPLICATION_JSON_UTF8)
	                        .accept(MediaType.APPLICATION_JSON_UTF8))
	                        .andExpect(status().isOk()).andReturn();
		 User returnedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		 
		 assertEquals(user.getId(), returnedUser.getId());
		 request.close();
	 } 

}
