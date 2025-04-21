package com.yuliiaskrypnyk.backend.security;

import com.yuliiaskrypnyk.backend.model.user.AppUser;
import com.yuliiaskrypnyk.backend.model.user.AppUserRole;
import com.yuliiaskrypnyk.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        DefaultOAuth2UserService userService = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User githubUser = userService.loadUser(userRequest);

            AppUser appUser = appUserRepository.findById(githubUser.getName())
                    .orElseGet(() -> appUserRepository.save(
                                    new AppUser(
                                            githubUser.getName(),
                                            githubUser.getAttributes().get("login").toString(),
                                            AppUserRole.USER,
                                            githubUser.getAttributes().get("avatar_url").toString()
                                    )
                            )
                    );
            return new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(appUser.role().toString())), githubUser.getAttributes(), "id");
        };
    }

}