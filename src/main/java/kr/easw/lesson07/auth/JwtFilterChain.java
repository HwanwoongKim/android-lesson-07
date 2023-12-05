package kr.easw.lesson07.auth;

import kr.easw.lesson07.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtFilterChain extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final JpaUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") == null) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("Jwt filter chain");
        String token = request.getHeader("Authorization");
        System.out.println("Token: " + token);

        switch (jwtService.validate(token)) {
            case VALID:
                String userName = jwtService.extractUsername(token);
                UserDetails details = userDetailsService.loadUserByUsername(userName);
                System.out.println(details.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        details,
                        details.getPassword(),
                        details.getAuthorities()
                ));
                System.out.println("Token validated / Role: " + details.getAuthorities());
                filterChain.doFilter(request, response);
                return;
            case EXPIRED:
                response.sendError(401, "Expired token");
                break;
            case UNSUPPORTED:
                response.sendError(401, "Unsupported token");
                break;
            case INVALID:
                response.sendError(401, "Invalid token");
                break;
        }
        System.out.println("Token invalid");
        System.out.println(jwtService.validate(token));
    }
}