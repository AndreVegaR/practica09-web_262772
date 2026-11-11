package com.example.GameVault.service;

import com.example.GameVault.model.Juego;
import com.example.GameVault.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class JuegoService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // aplicar singleton springboot ya hace la inyeccipon de dependencia
    @Autowired
    private JuegoRepository juegoRepository;

    public List<Juego> listarTodos(){
        return juegoRepository.findAll();
    }

    /**
     * Obtiene juego por ID
     *
     * @param id
     *
     * @return
     */
    public Juego obtenerJuegoPorId(Long id) {
        return juegoRepository.findById(id).orElse(null);
    }


    /**
     * Elimina un juego por ID
     *
     * @param id
     */
    public void eliminarJuego(Long id) {
        juegoRepository.deleteById(id);
    }


    /**
     *
     *
     * @param juego
     * @param portada
     */
    public void guardarJuego(Juego juego, MultipartFile portada){
        String urlImagen = juego.getPortadaUrl() != null ? juego.getPortadaUrl() : "default.png";

        // Verificamos si el archivo no está vacío
        if (!portada.isEmpty()) {
            try {
                // Tip para clase: Crear la carpeta si no existe
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generamos un nombre único para evitar sobreescribir archivos con el mismo nombre
                urlImagen = UUID.randomUUID().toString() + "_" + portada.getOriginalFilename();
                Path filePath = uploadPath.resolve(urlImagen);

                // Guardamos el archivo físicamente en disco
                Files.copy(portada.getInputStream(), filePath);

                System.out.println("Archivo guardado en: " + filePath.toAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                // En un proyecto real, manejaríamos esta excepción apropiadamente (ej. redirigir con error)
            }
        }

        // aqui manejar excpcion
        juego.setPortadaUrl(urlImagen);
        juegoRepository.save(juego);
    }

    public List<Juego> buscarPorTitulo(String palabra){
        if(palabra == null || palabra.trim().isEmpty()){
            return listarTodos();
        }
        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }


    /**
     *
     *
     * @param idUsuario
     *
     * @return
     */
    public List<Juego> obtenerJuegosPorUsuario(Long idUsuario) {
        return juegoRepository.findByUsuarioId(idUsuario);
    }
}