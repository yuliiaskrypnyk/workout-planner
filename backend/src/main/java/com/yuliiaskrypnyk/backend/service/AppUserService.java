package com.yuliiaskrypnyk.backend.service;

import com.yuliiaskrypnyk.backend.exception.ResourceNotFoundException;
import com.yuliiaskrypnyk.backend.model.user.AppUser;
import com.yuliiaskrypnyk.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser getAppUserById(String id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
    }
}