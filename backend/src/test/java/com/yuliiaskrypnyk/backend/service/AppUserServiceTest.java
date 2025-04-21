package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.user.AppUser;
import com.yuliiaskrypnyk.backend.model.user.AppUserRole;
import com.yuliiaskrypnyk.backend.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository mockAppUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void getAppUserById_shouldReturnUser_whenUserExist() {
        AppUser user = AppUser.builder()
                .id("1")
                .role(AppUserRole.USER)
                .name("Test")
                .build();
        when(mockAppUserRepository.findById(user.id())).thenReturn(Optional.of(user));

        AppUser actual = appUserService.getAppUserById(user.id());

        assertEquals(user, actual);
        verify(mockAppUserRepository, times(1)).findById(user.id());
    }

    @Test
    void getAppUserById_shouldThrowException_whenUserNotFound() {
        String userId = "1";
        when(mockAppUserRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> appUserService.getAppUserById(userId));

        assertEquals("Requested User was not found.", exception.getMessage());
        verify(mockAppUserRepository, times(1)).findById(userId);
    }
}