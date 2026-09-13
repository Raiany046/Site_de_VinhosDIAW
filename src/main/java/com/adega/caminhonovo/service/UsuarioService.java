package com.adega.caminhonovo.service;

import com.adega.caminhonovo.dto.RegistroForm;
import com.adega.caminhonovo.model.Usuario;
import com.adega.caminhonovo.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrar(RegistroForm form) {
        String email = normalizarEmail(form.getEmail());

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailJaCadastradoException(email);
        }

        // a senha so vai para o banco depois de virar hash
        Usuario usuario = new Usuario(
                form.getNome().trim(),
                email,
                passwordEncoder.encode(form.getSenha())
        );
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(normalizarEmail(email));
    }

    @Transactional(readOnly = true)
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmailIgnoreCase(normalizarEmail(email));
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
