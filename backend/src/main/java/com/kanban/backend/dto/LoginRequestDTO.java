package com.kanban.backend.dto;
import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String senha;
}
