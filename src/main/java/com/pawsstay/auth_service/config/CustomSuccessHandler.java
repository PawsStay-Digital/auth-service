package com.pawsstay.auth_service.config;

import com.pawsstay.auth_service.dto.OwnerCreateRequest;
import com.pawsstay.auth_service.dto.OwnerResponse;
import com.pawsstay.auth_service.util.JwtUtils;
import com.pawsstay.auth_service.util.OwnerClient;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final OwnerClient ownerClient;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User googleUser = (OAuth2User) authentication.getPrincipal();
        log.warn("login:{}", googleUser);
        String email = googleUser.getAttribute("email");
        String name = googleUser.getAttribute("name");
        log.warn("login email:{}", email);
        // 1. 透過 Feign 檢查 Owner Service 是否已有此用戶
        OwnerResponse owner;
        try{
            owner =ownerClient.getOwnerByEmail(email);
        }
        catch (FeignException.NotFound e) {
            log.info("owner not found" , e);
            OwnerCreateRequest newOwner = new OwnerCreateRequest();
            newOwner.setEmail(email);
            newOwner.setName(name);
            owner = ownerClient.createOwner(newOwner);

        }
        log.warn("login owner:{}", owner);


        // 3. 取得 ownerId 並產出 JWT
        String token = jwtUtils.generateToken(owner.getId(), email);

        // 4. 重導向至前端，並附上 Token
        String targetUrl = "http://localhost:3000/login-success?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
