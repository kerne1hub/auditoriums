package com.kernel.auditoriums.api.dto;

import com.kernel.auditoriums.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDto {
    private User user;
    private String token;
}
