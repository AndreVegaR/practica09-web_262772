package com.example.GameVault.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuración web que expone la carpeta de subidas para poder visualizar
 * las imágenes cargadas en tiempo real
 *
 * @author Andre
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtenemos la ruta absoluta de la carpeta de subidas
        Path uploadPath = Paths.get(UPLOAD_DIR);
        String ubicacion = uploadPath.toAbsolutePath().toUri().toString();

        // Publicamos la carpeta del disco bajo la ruta /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(ubicacion.endsWith("/") ? ubicacion : ubicacion + "/");
    }
}
