package com.example.GameVault.controller;

import com.example.GameVault.model.Usuario;
import com.example.GameVault.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de autenticación: registro e inicio de sesión.
 */
@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("email") String email,
                                @RequestParam("password") String password,
                                Model model) {

        Usuario usuario = usuarioService.autenticarUsuario(email, password);

        if (usuario != null) {
            // Las credenciales son correctas, pero todavía no recordamos quién es:
            // en cuanto cambie de página, la aplicación lo olvida.
            model.addAttribute("mensaje", "Bienvenido, " + usuario.getNombre());
            return "login";
        } else {
            model.addAttribute("error", "Credenciales inválidas. Inténtalo de nuevo.");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        // Objeto vacío que el formulario llena con th:field
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            usuarioService.registrarUsuario(usuario);
            return "redirect:/login?registrado=true";
        } catch (IllegalArgumentException e) {
            // Muestra el mensaje de validación si el correo ya existe o faltan datos
            model.addAttribute("error", e.getMessage());
            return "registro";
        } catch (Exception e) {
            // Atrapa cualquier otro error de persistencia
            model.addAttribute("error", "Ocurrió un error al registrar el usuario.");
            return "registro";
        }
    }
}