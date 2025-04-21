package com.yuliiaskrypnyk.backend.security;

import com.yuliiaskrypnyk.backend.model.user.AppUser;
import com.yuliiaskrypnyk.backend.model.user.AppUserRole;
import com.yuliiaskrypnyk.backend.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @DirtiesContext
    void getMe() throws Exception {
        AppUser testUser = AppUser.builder()
                .id("1")
                .name("Test")
                .role(AppUserRole.ADMIN)
                .avatarUrl("test")
                .build();
        appUserRepository.save(testUser);

        mvc.perform(MockMvcRequestBuilders.get("/api/auth/me")
                        .with(oidcLogin().idToken(token -> token.claim("sub", "1"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.avatarUrl").value("test"));
    }
}