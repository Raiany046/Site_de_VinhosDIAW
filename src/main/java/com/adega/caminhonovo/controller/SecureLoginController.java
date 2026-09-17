package com.adega.caminhonovo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.adega.caminhonovo.service.SendEmailService;
import com.adega.caminhonovo.service.UserService;

@Controller
public class SecureLoginController {

    private final SendEmailService sendEmailService;
    private final UserService userService;

    public SecureLoginController(SendEmailService sendEmailService,
                                 UserService userService) {
        this.sendEmailService = sendEmailService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        System.out.println("Usuário logado: " + authentication.getName());
        model.addAttribute("usuario", authentication.getName());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication, Model model) {
        System.out.println("Administrador logado: " + authentication.getName());
        model.addAttribute("usuario", authentication.getName());
        return "admin";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("cpf") String cpf,
            @RequestParam("rg") String rg,
            @RequestParam("endereco") String endereco,
            @RequestParam("senha") String senha,
            @RequestParam("confirmarSenha") String confirmarSenha,
            Model model) {

        model.addAttribute("nome", nome);
        model.addAttribute("email", email);
        model.addAttribute("cpf", cpf);
        model.addAttribute("rg", rg);
        model.addAttribute("endereco", endereco);

        if (nome.isBlank() || email.isBlank() || cpf.isBlank() || rg.isBlank()
                || endereco.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
            model.addAttribute("erro", "Preencha todos os campos.");
            return "register";
        }

        if (!email.contains("@") || !email.contains(".")) {
            model.addAttribute("erro", "Informe um e-mail válido.");
            return "register";
        }

        if (senha.length() < 8 || !senha.matches(".*[A-Za-z].*") || !senha.matches(".*\\d.*")) {
            model.addAttribute("erro", "A senha deve ter no mínimo 8 caracteres, com letras e números.");
            return "register";
        }

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas informadas não são iguais.");
            return "register";
        }

        if (userService.exists(email)) {
            System.out.println("Usuário já cadastrado: " + email);
            model.addAttribute("erro", "Este e-mail já está cadastrado.");
            return "register";
        }

        userService.createUser(email, senha);

        System.out.println("Usuário cadastrado: " + email);

        return "redirect:/login?cadastro=sucesso";
    }

    @GetMapping("/recoverpassword")
    public String recoverpassword() {
        return "recoverpassword";
    }

    @PostMapping("/recoverpassword")
    public String handleRecoverPassword(
            @RequestParam("email") String email) {

        sendEmailService.sendEmail(
                email,
                "Recuperação de Senha - Adega Caminho Novo",
                "Olá! Recebemos uma solicitação para recuperar a senha da sua conta na Adega Caminho Novo.\n\n"
                        + "Aqui está o link para recuperar sua senha: [link de recuperação]\n\n"
                        + "Se você não fez essa solicitação, ignore este e-mail."
        );

        System.out.println("Recuperação de E-mail: Redirecionado para a página de login.");

        return "redirect:/login?recuperacao=enviada";
    }
}
