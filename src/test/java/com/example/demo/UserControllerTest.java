package com.example.demo;
 
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.requests.CreateUserRequest; 

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.hamcrest.Matchers.hasSize; 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 


import java.net.URI;

import org.junit.Test;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {
	

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateUserRequest> json;

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
    
	//test1 create user with username
	
		//test2 create user with less password
		
		//test3 search username that not exists
		
		//test4 search by id
		//test 5 access denied with jwt

}
