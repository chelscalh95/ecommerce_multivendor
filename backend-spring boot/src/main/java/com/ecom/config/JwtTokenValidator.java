package com.ecom.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		
		// Skip JWT validation for public endpoints
		String requestPath = request.getRequestURI();
		if (isPublicEndpoint(requestPath)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		if (jwt != null && jwt.startsWith("Bearer ")) {
			jwt = jwt.substring(7);
			
			// Check if token is empty or invalid after removing "Bearer "
			if (jwt.trim().isEmpty() || "null".equals(jwt) || "undefined".equals(jwt)) {
				filterChain.doFilter(request, response);
				return;
			}
			
			try {
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
				
				Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(jwt)
					.getBody();
				
				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities"));
				
				// Validate email is not null or empty
				if (email == null || email.trim().isEmpty() || "null".equals(email)) {
					throw new BadCredentialsException("Invalid token: missing email");
				}
				
				// Validate authorities
				if (authorities == null || "null".equals(authorities)) {
					authorities = "ROLE_CUSTOMER"; // Default role
				}
				
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			} catch (SecurityException e) {
				throw new BadCredentialsException("Invalid JWT signature", e);
			} catch (MalformedJwtException e) {
				throw new BadCredentialsException("Invalid JWT token", e);
			} catch (ExpiredJwtException e) {
				throw new BadCredentialsException("JWT token is expired", e);
			} catch (UnsupportedJwtException e) {
				throw new BadCredentialsException("JWT token is unsupported", e);
			} catch (IllegalArgumentException e) {
				throw new BadCredentialsException("JWT claims string is empty", e);
			} catch (Exception e) {
				throw new BadCredentialsException("JWT token validation failed", e);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	/**
	 * Check if the endpoint is public and doesn't require authentication
	 */
	private boolean isPublicEndpoint(String path) {
		return path.startsWith("/auth/") ||
			   path.startsWith("/api/products/search") ||
			   path.startsWith("/api/products/category/") ||
			   path.startsWith("/api/products/public/") ||
			   path.startsWith("/api/home/") ||
			   path.startsWith("/swagger-ui/") ||
			   path.startsWith("/v3/api-docs/") ||
			   path.equals("/actuator/health") ||
			   path.endsWith("/reviews") && path.contains("/api/products/");
	}
}
