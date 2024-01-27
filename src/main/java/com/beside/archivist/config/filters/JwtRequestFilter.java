package com.beside.archivist.config.filters;

import com.beside.archivist.service.users.UserService;
import com.beside.archivist.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserService userServiceImpl;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(UserService userServiceImpl, JwtTokenUtil jwtTokenUtil) {
        this.userServiceImpl = userServiceImpl;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (StringUtils.startsWith(requestTokenHeader, "Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                if (StringUtils.isNoneEmpty(username) && null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        String role = "USER"; // Default role
                        if ("gudwls215@naver.com".equals(username)) {
                            role = "ADMIN";
                        }

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));

                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext()
                                .setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (SignatureException e) {
                throw new JwtException("잘못된 토큰입니다.");
            } catch (ExpiredJwtException e) {
                throw new JwtException("토큰이 만료되었습니다.");
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request,response);
    }
}
