package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
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
        when(userRepository.findByUsername("shank")).thenReturn(createUser());
        when(orderRepository.findByUser(any())).thenReturn(Arrays.asList(createOrder()));    
    }
    
    @Test
    public void testOrdersByUserName() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("shank");
        assertEquals(HttpStatus.OK, ordersForUser.getStatusCode());
    }

    public static User createUser() {
        User user = new User();
        user.setId(Long.valueOf(1));
        user.setUsername("shank");
        user.setPassword("testPassword");
        user.setCart(createCart());
        return user;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        cart.setId(Long.valueOf(1));
        cart.addItem(createItem());
        return cart;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setId(Long.valueOf(1));
        item.setDescription("shoes");
        item.setPrice(BigDecimal.valueOf(7,05));
        return item;
    }
	
    public static UserOrder createOrder() {
        return UserOrder.createFromCart(createCart());
    }
}
