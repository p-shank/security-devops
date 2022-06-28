package com.example.demo.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

public class AuthUtils {

    public static final String SECRET_KEY = "pSk@h4l!";
    public static final long EXPIRY = 432_000_000;
    public static final String AUTH_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";
	
	public static byte[] createSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}
	
    public static User createUser() {
        User user = new User();
        user.setId(Long.valueOf(1));
        user.setUsername("test");
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
