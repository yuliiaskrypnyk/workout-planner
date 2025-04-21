package com.yuliiaskrypnyk.backend.controller;

import com.yuliiaskrypnyk.backend.model.user.AppUser;
import com.yuliiaskrypnyk.backend.model.user.AppUserRole;
import com.yuliiaskrypnyk.backend.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "ADMIN")
    void getAppUserById_shouldReturnUser_whenUserWithGivenIdExists() throws Exception {
        AppUser user = AppUser.builder()
                .id("1")
                .name("Test")
                .role(AppUserRole.USER)
                .build();
        appUserRepository.save(user);

        mvc.perform(MockMvcRequestBuilders.get("/api/users/" + user.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}