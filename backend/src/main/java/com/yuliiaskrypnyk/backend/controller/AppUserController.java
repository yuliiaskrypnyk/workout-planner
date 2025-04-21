package com.yuliiaskrypnyk.backend.controller;

import com.yuliiaskrypnyk.backend.dto.AppUserDTO;
import com.yuliiaskrypnyk.backend.model.user.AppUser;
import com.yuliiaskrypnyk.backend.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getAppUserById(@PathVariable String id) {
        AppUser user = appUserService.getAppUserById(id);
        return ResponseEntity.ok(new AppUserDTO(user.id(), user.name(), user.role()));
    }
}