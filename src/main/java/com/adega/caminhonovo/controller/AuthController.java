package com.adega.caminhonovo.controller;

import com.adega.caminhonovo.dto.RegistroForm;
import com.adega.caminhonovo.service.EmailJaCadastradoException;
import com.adega.caminhonovo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        @RequestParam(required = false) String cadastro,
                        Model model) {

        if (error != null) {
            model.addAttribute("erro", "E-mail ou senha inválidos. Confira os dados e tente novamente.");
        }
        if (logout != null) {
            model.addAttribute("sucesso", "Sessão encerrada com sucesso. Até a próxima!");
        }
        if (cadastro != null) {
            model.addAttribute("sucesso", "Cadastro realizado! Faça login para acessar sua conta.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(@RequestParam(required = false) String email, Model model) {
        RegistroForm registroForm = new RegistroForm();
        registroForm.setEmail(email);
        model.addAttribute("registroForm", registroForm);
        return "register";
    }

    @PostMapping("/register")
    public String processarRegistro(@Valid @ModelAttribute("registroForm") RegistroForm registroForm,
                                    BindingResult resultado,
                                    RedirectAttributes redirectAttributes) {

        if (!registroForm.senhasConferem()) {
            resultado.rejectValue("confirmacaoSenha", "senhas.diferentes",
                    "As senhas informadas não são iguais.");
        }

        if (resultado.hasErrors()) {
            return "register";
        }

        try {
            usuarioService.registrar(registroForm);
        } catch (EmailJaCadastradoException e) {
            resultado.addError(new FieldError("registroForm", "email",
                    registroForm.getEmail(), false, null, null,
                    "Este e-mail já está cadastrado. Tente fazer login."));
            return "register";
        }

        redirectAttributes.addAttribute("cadastro", "ok");
        return "redirect:/login";
    }

    @GetMapping("/recoverpassword")
    public String recoverPassword() {
        return "recoverpassword";
    }

    @PostMapping("/recoverpassword")
    public String processarRecoverPassword(@RequestParam("email") String email, Model model) {

        if (email == null || email.isBlank()) {
            model.addAttribute("erro", "Informe o e-mail cadastrado.");
            return "recoverpassword";
        }

        System.out.println("Recuperação de senha solicitada para: " + email
                + " (cadastrado: " + usuarioService.emailExiste(email) + ")");

        model.addAttribute("sucesso",
                "Se houver uma conta associada a " + email
                        + ", enviaremos as instruções de redefinição de senha.");
        return "recoverpassword";
    }
}
