package com.example.GameVault.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JuegoDTO {
    private String titulo;
    private String descripcion;
    private Double precio;
    private String categoria;
    private MultipartFile portada;
}