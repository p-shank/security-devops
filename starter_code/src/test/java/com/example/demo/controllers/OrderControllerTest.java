package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.AuthUtils;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);
	
    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        when(userRepository.findByUsername("shank")).thenReturn(AuthUtils.createUser());
        when(orderRepository.findByUser(any())).thenReturn(Arrays.asList(AuthUtils.createOrder()));    
    }
    
    @Test
    public void testOrdersByUserName() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("shank");
        assertEquals(HttpStatus.OK, ordersForUser.getStatusCode());
    }

}
