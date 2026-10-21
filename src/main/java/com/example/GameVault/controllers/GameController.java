package com.example.GameVault.controllers;

import com.example.GameVault.dto.JuegoDTO;
import com.example.GameVault.models.Juego;
import com.example.GameVault.service.JuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador que gestiona la página de fragmentos, el catálogo de juegos
 * y la subida de portadas al servidor
 *
 * @author Andre
 */
@Controller
public class GameController {
    private JuegoService juegoService;

    /**
     * Retorna la página independiente para enseñar las notaciones de fragmentos
     *
     * @return nombre de la vista fragments-demo
     */
    @GetMapping("/fragments-demo")
    public String fragmentsDemo() {
        return "fragments-demo";
    }



    /**
     * Retorna el catálogo de juegos utilizando el Layout maestro
     *
     * @param model modelo al que se agrega la lista de juegos
     *
     * @return nombre de la vista juegos
     */
    @GetMapping({"/", "/juegos"})
    public String listarJuegos(@RequestParam(name="buscar", required=false) String buscar, Model model) {
        String texto = "juegos";
        if (buscar != null && !buscar.isEmpty()) {
            model.addAttribute(texto, juegoService.buscarPorTitulo(buscar));
            model.addAttribute("busquedaActual");
        } else {
            model.addAttribute(texto, juegoService.listarTodos());
        }
        return texto;
    }



    /**
     * Retorna el formulario de subida de juegos
     *
     * @return nombre de la vista formulario
     */
    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario() {
        return "formulario";
    }



    /**
     * Guarda un juego con la información recibida
     *
     * @param juegoDTO que contiene la información
     *
     * @return redirección a juegos
     */
    @PostMapping("/juegos")
    public String guardarJuego(@ModelAttribute JuegoDTO juegoDTO) {
        Juego nuevoJuego = crearJuego(juegoDTO);
        juegoService.guardarJuego(nuevoJuego, juegoDTO.getPortada());
        return "redirect:/juegos";
    }



    /**
     * Edita un juego por su ID
     *
     * @param idJuego
     * @param model
     *
     * @return
     */
    @GetMapping("/juegos/editar/{id}")
    public String editarJuego(@PathVariable Long idJuego, Model model) {
        Juego juego = juegoService.obtenerJuegoPorId(idJuego);
        String regreso = "juegos";
        if (juego != null) {
            model.addAttribute("juego", juego);
            regreso = "formulario";
        }
        return regreso;
    }



    /**
     * Actualiza un juego usando su ID y mediante la notación @ModelAttribute,
     * que automatiza la declaración de @RequestParam en los parámetros basándose
     * en el DTO JuegoDTO
     *
     * @param id del juego
     * @param juegoDTO que guarda los datos
     *
     * @return redirección a juegos
     */
    @PostMapping("/juegos/editar/{id}")
    public String actualizarJuego(@PathVariable Long id, @ModelAttribute JuegoDTO juegoDTO) {
        Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
        if (juegoExistente != null) {
            juegoExistente.setTitulo(juegoDTO.getTitulo());
            juegoExistente.setDescripcion(juegoDTO.getDescripcion());
            juegoExistente.setPrecio(juegoDTO.getPrecio());
            juegoExistente.setCategoria(juegoDTO.getCategoria());
            juegoService.guardarJuego(juegoExistente, juegoDTO.getPortada());
        }
        return "redirect:/juegos";
    }



    /**
     * Elimina un videojuego por ID
     *
     * @param idJuego
     *
     * @return redirección a juegos
     */
    @PostMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long idJuego) {
        Juego juego = juegoService.obtenerJuegoPorId(idJuego);
        if (juego != null) {
            juegoService.eliminarJuego(idJuego);
        }
        return "redirect:/juegos";
    }



    /**
     * Método auxiliar que centraliza el mapeo de DTO a modelo
     *
     * @param juegoDTO que contiene la información
     *
     * @return el modelo de juego
     */
    private Juego crearJuego(JuegoDTO juegoDTO) {
        Juego nuevoJuego = new Juego();
        nuevoJuego.setTitulo(juegoDTO.getTitulo());
        nuevoJuego.setCategoria(juegoDTO.getCategoria());
        nuevoJuego.setDescripcion(juegoDTO.getDescripcion());
        nuevoJuego.setPrecio(juegoDTO.getPrecio());
        return nuevoJuego;
    }
}