package com.example.demo;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; 
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
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
    private JacksonTester<CreateUserRequest> json;
    
    @Autowired
    private MockHttpServletRequest request;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Mock
    private UserRepository userRepository;
    
    
 
    

    @InjectMocks 
    CartController cartController; 
	@Mock UserController userController; 
	 
	  
	@Mock ItemRepository itemRepository;
	
	@Mock CartRepository cartRepository;
	
	
	
	@Mock ModifyCartRequest modifyCartRequest;
	@Mock User usr;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
//		when(spy(User))
		 
		Mockito.doReturn(1L).when(usr.getId());
		Mockito.doReturn("XXXXXXX").when(usr.getPassword());
		Mockito.doReturn("Mike").when(usr.getUsername());
		 
		Mockito.when(userRepository.findByUsername("Mike")).thenReturn(usr);
	 	
//		Mockito.doReturn("Mike").when(modifyCartRequest.getUsername());
//		Mockito.doReturn(5).when(modifyCartRequest.getItemId());
//		Mockito.doReturn(50).when(modifyCartRequest.getQuantity());
//		Mockito.doReturn("Mike").when(modifyCartRequest.getUsername());
		
		 
		
//		 DeviceConfigurationRepository vehicleIdRepo = Mockito.mock(DeviceConfigurationRepository.class);
//	        Mockito.doReturn("123").when(vehicleIdRepo).getVehicleId();
//	        O
		 
//		injectObjects(cartController, "cartRepository", cartRepository);
//		injectObjects(cartController, "itemRepository", itemRepository);
//		injectObjects(cartController, "userRepository", userRepository);
//		
//		CreateUserRequest request = new CreateUserRequest();
//		request.setUsername("test");
//		request.setPassword("testPassword");
//		request.setConfirmPassword("testPassword");
//
//        ResponseEntity<User> response = userController.createUser(request);
//
//        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
//        modifyCartRequest.setUsername("test");
//        modifyCartRequest.setItemId(1);
//        modifyCartRequest.setQuantity(1);

        
	}
	 

 
	 
	@Test
	public void addToCartTest() throws URISyntaxException, IOException, Exception {
//		
//		mvc.perform(post(new URI("/api/cart/addToCart")).content(json.write(modifyCartRequest).getJson())
// 				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
// 				.andExpect(status().isOk());
//				
				
//		 ResponseEntity<Cart> response2 =  cartController.addTocart(modifyCartRequest);
//	        assertNotNull(response2);
//	        assertEquals(200,response.getStatusCodeValue());
		
	}
	@Test
	public void removeFromCartTest() {
		
	}
	
	
//	/**
//	 * Test for create a User With UserName
//	 * 
//	 * @throws Exception when User creation fails in the system
//	 */
//	@Test
//	public void createUserWithUserNamePasswordSizeNotMatch() throws Exception {
//		CreateUserRequest createUser = new CreateUserRequest();
//		createUser.setUsername("MyName4");
//		createUser.setPassword("XXXXXXX");
//		createUser.setConfirmPassword("XXXXXXXXXX");
//
//		mvc.perform(post(new URI("/api/user/create")).content(json.write(createUser).getJson())
//				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(status().isBadRequest());
//
//	}
     	
	
	
//	@PostMapping("/addToCart")
//	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
//		User user = userRepository.findByUsername(request.getUsername());
//		if(user == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		}
//		Optional<Item> item = itemRepository.findById(request.getItemId());
//		if(!item.isPresent()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		}
//		Cart cart = user.getCart();
//		IntStream.range(0, request.getQuantity())
//			.forEach(i -> cart.addItem(item.get()));
//		cartRepository.save(cart);
//		return ResponseEntity.ok(cart);
//	}
}
