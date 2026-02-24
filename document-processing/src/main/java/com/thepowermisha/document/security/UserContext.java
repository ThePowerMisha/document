package com.thepowermisha.document.security;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserContext {
    private UUID userId;
}
