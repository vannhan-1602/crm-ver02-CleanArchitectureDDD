package com.crm.presentation.security;

import com.crm.domain.entities.CrmUser;
import com.crm.domain.repositories.CrmUserAuthRepository;
import com.crm.domain.services.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final CrmUserAuthRepository userRepository;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, CrmUserAuthRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (tokenProvider.validateToken(token)) {
                Integer userId = tokenProvider.getUserId(token);
                userRepository.findById(userId)
                        .filter(CrmUser::isActive)
                        .ifPresent(this::setAuthentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(CrmUser user) {
        AuthenticatedUserPrincipal principal = new AuthenticatedUserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getRoleCode()
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority(user.getRoleCode()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
