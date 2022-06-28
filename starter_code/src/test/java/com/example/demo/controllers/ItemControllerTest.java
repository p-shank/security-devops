package com.example.demo.controllers;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    private ItemController itemController;

    private UserController userController;
    
    private UserControllerTest userControllerTest;

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    
    @Before
    public void setUp(){
        itemController = new ItemController();
        userController = new UserController();
        userControllerTest = new UserControllerTest();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);    
    }
    
    public User createUser() throws Exception{
        when(encoder.encode("shank@1234")).thenReturn("thisIsHashed");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("shank");
        req.setPassword("shank@1234");
        req.setConfirmPassword("shank@1234");

        ResponseEntity<User> response = userController.createUser(req);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        User user = response.getBody();
        
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("shank", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
        return user;
    }

    @Test
    public void testGetItemsByUserName() throws Exception {
    	
    	User user = createUser();
    	modifyCart(user);
        ResponseEntity<List<Item>> items = itemController.getItemsByName(user.getUsername());
        assertEquals(HttpStatus.NOT_FOUND, items.getStatusCode());
    }
    
    private void modifyCart(User user) {
        ModifyCartRequest cart = new ModifyCartRequest();
        cart.setUsername(user.getUsername());
        cart.setItemId(1);
        cart.setQuantity(1);
    }

	
}
