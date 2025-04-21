package com.yuliiaskrypnyk.backend.dto;

import com.yuliiaskrypnyk.backend.model.user.AppUserRole;
import lombok.Builder;

@Builder
public record AppUserDTO(
        String id,
        String name,
        AppUserRole role
) {
}