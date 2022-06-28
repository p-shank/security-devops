package com.example.demo.controllers;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);
    
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    
	private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    
    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }
    
    @Test
    public void testCreateUser() throws Exception{
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

    }
        
}
