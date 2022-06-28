package com.example.demo.controllers;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.AuthUtils;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    private CartController cartController;

    private UserController userController;

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        userController = new UserController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        when(userRepository.findByUsername("shank")).thenReturn(AuthUtils.createUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(AuthUtils.createItem()));
        when(cartRepository.save(any())).thenReturn(AuthUtils.createCart());
    }
    
    
    @Test
    public void testCartAddition() throws Exception{
    	User user = createUser();
    	ModifyCartRequest cart = modifyCart(user);

        ResponseEntity<Cart> addCart = cartController.addTocart(cart);

        assertNotNull(cart);
        assertEquals(HttpStatus.OK, addCart.getStatusCode());
    }

    @Test
    public void testCartDeletion() throws Exception {
    	User user = createUser();
    	ModifyCartRequest cart = modifyCart(user);

        ResponseEntity<Cart> removeCart = cartController.removeFromcart(cart);

        assertNotNull(cart);
        assertEquals(HttpStatus.OK, removeCart.getStatusCode());
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
    
    private ModifyCartRequest modifyCart(User user) {
        ModifyCartRequest cart = new ModifyCartRequest();
        cart.setUsername(user.getUsername());
        cart.setItemId(1);
        cart.setQuantity(1);
        return cart;
    }
}
