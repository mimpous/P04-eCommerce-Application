package com.example.demo;
 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
     * @throws Exception when User creation fails in the system
     */
    @Test
    public void createUserWithUserName() throws Exception  {
    	CreateUserRequest createUser = new CreateUserRequest();
    	createUser.setUsername("MyName");
    	createUser.setPassword("123456789");
    	createUser.setConfirmPassword("123456789");
      	
        mvc.perform(
                post(new URI("/api/user/create"))
                        .content(json.write(createUser).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isOk());
        
    }
	
    
    /**
     * Test for create a User With UserName
     * @throws Exception when User creation fails in the system
     */
    @Test
    public void createUserWithUserNameNoPassword() throws Exception  {
    	CreateUserRequest createUser = new CreateUserRequest();
    	createUser.setUsername("MyName"); 
      	
        mvc.perform(
                post(new URI("/api/user/create"))
                        .content(json.write(createUser).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isBadRequest());
        
    }
    
    /**
     * Test for create a User With UserName
     * @throws Exception when User creation fails in the system
     */
    @Test
    public void createUserWithUserNamePasswordNotEqual() throws Exception  {
    	CreateUserRequest createUser = new CreateUserRequest();
    	createUser.setUsername("MyName");
    	createUser.setPassword("XXXXXXX"); 
       	createUser.setConfirmPassword("YYYYYYY"); 
             	
        mvc.perform(
                post(new URI("/api/user/create"))
                        .content(json.write(createUser).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isBadRequest());
        
    }
    
    /**
     * Test for create a User With UserName
     * @throws Exception when User creation fails in the system
     */
    @Test
    public void createUserWithUserNamePasswordSizeNotMatch() throws Exception  {
    	CreateUserRequest createUser = new CreateUserRequest();
    	createUser.setUsername("MyName");
    	createUser.setPassword("XXXXXXX"); 
       	createUser.setConfirmPassword("XXXXXXXXXX"); 
             	
        mvc.perform(
                post(new URI("/api/user/create"))
                        .content(json.write(createUser).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isBadRequest());
        
    }
     	
    private User user;

	@BeforeEach 
	public void init() throws URISyntaxException, IOException, Exception {
		
		CreateUserRequest createUser = new CreateUserRequest();
    	createUser.setUsername("MyName");
    	createUser.setPassword("123456789");
    	createUser.setConfirmPassword("123456789");
      	
//    	MvcResult postResult=  mvc.perform(
//    			MockMvcRequestBuilders.post( "/api/user/create")
//                        .content(json.write(createUser).getJson())
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                        .andExpect(status().isOk()).andReturn();
    	MvcResult entityResult=  mvc.perform(
    			MockMvcRequestBuilders.post( "/api/user/create")
                        .content(objectMapper.writeValueAsString(createUser)) 
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)) 
                        .andExpect(status().isOk()).andReturn();       
         
    	  
        
        user = objectMapper.readValue(entityResult.getResponse().getContentAsString(), User.class);
        
//        
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login").content(objectMapper.writeValueAsString(createUser)))
        		.andExpect( status().isOk()).andReturn();
//        
         request.addParameter("Authorization", entityResult.getResponse().getHeader("Authorization")); 
//		 
	}
     
	@Test
	 public void findByUserNameTest() throws Exception  {
		  
		 
		 MvcResult result=mvc.perform(
	                get(new URI("/api/user/Mike"))
	                        .contentType(MediaType.APPLICATION_JSON_UTF8)
	                        .accept(MediaType.APPLICATION_JSON_UTF8))
	                        .andExpect(status().isOk()).andReturn();
		  
	 }
	
//	@GetMapping("/{username}")
//	public ResponseEntity<User> findByUserName(@PathVariable String username) {
//		User user = userRepository.findByUsername(username);
//		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
//	}
 
    
	//test1 create user with username
	
		//test2 create user with less password
		
		//test3 search username that not exists
		
		//test4 search by id
		//test 5 access denied with jwt

}
