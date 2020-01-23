package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	private static final Logger log = LoggerFactory.getLogger("splunk.logger");

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		
		User user = null;
		try {
				
			user=userRepository.findByUsername(username);
		} catch (Exception e) {
			log.error("An error occured to find user {}. Exception is : {}", username , e);
		}	
		if(user == null) {
			log.error( "User object is null");
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());

		try {
			orderRepository.save(order);
			log.info( "Order submited for user {}." , user.getUsername());
		} catch (Exception e) {
			log.error("Unable to add order for user {}. Exception is :" ,username , e );
		}
		
		
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		
		User user = null;
		try {
				
			user=userRepository.findByUsername(username);
		} catch (Exception e) {
			log.error("An error occured to find user {}. Exception is : {}", username , e);
		}
		
		if(user == null) {
			log.error( "User object is null");
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
