package com.example.demo.authentication;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.example.demo.utils.AuthUtils;

public class UserVerification extends BasicAuthenticationFilter{

	public UserVerification(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(AuthUtils.HEADER);

        if (header == null || !header.startsWith(AuthUtils.AUTH_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthenticationToken(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest req) {
        String token = req.getHeader(AuthUtils.HEADER);
        if (token != null) {
            String user = JWT.require(HMAC512(AuthUtils.SECRET_KEY.getBytes())).build()
                    .verify(token.replace(AuthUtils.AUTH_PREFIX, ""))
                    .getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

}
