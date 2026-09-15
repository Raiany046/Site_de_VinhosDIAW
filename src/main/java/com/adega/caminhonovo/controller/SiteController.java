package com.adega.caminhonovo.controller;

import com.adega.caminhonovo.model.Usuario;
import com.adega.caminhonovo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class SiteController {

    private final UsuarioService usuarioService;

    public SiteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/minha-conta")
    public String minhaConta(Principal principal, Model model) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName()).orElseThrow();
        model.addAttribute("usuario", usuario);
        return "minha-conta";
    }
}
