package com.yuliiaskrypnyk.backend.model.user;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "users")
public record AppUser(
        @Id String id,
        String name,
        AppUserRole role,
        String avatarUrl
) {
}