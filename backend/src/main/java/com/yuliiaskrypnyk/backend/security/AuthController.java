package com.yuliiaskrypnyk.backend.security;

import com.yuliiaskrypnyk.backend.model.user.AppUser;
import com.yuliiaskrypnyk.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository appUserRepository;

    @GetMapping("/me")
    public AppUser getMe(@AuthenticationPrincipal OAuth2User user) {
        return appUserRepository.findById(user.getName()).orElseThrow();
    }
}