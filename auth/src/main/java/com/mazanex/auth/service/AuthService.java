package com.mazanex.auth.service;

import com.mazanex.auth.dto.LoginRequest;
import com.mazanex.auth.dto.RegistroRequest;
import com.mazanex.auth.model.Usuario;
import com.mazanex.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ==============================
    // LISTAR USUARIOS
    // ==============================
    public List<Usuario> getUsers() {
        return usuarioRepository.findAll();
    }

    // ==============================
    // OBTENER USUARIO POR ID
    // ==============================
    public Usuario getUsuarioID(long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // ==============================
    // ACTUALIZAR USUARIO
    // ==============================
    public Usuario updateUsuario(Long id, Usuario userData) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(userData.getNombre());
            usuario.setEmail(userData.getEmail());
            usuario.setPassword(userData.getPassword());
            usuario.setRol(userData.getRol());
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }

    // ==============================
    // ELIMINAR USUARIO
    // ==============================
    public void eliminarUsuario(long id) {
        usuarioRepository.deleteById(id);
    }

    // ==============================
    // REGISTRO DE USUARIO (Con DTO)
    // ==============================
    public Usuario registrar(RegistroRequest request) {

        Optional<Usuario> existente = usuarioRepository.findByEmail(request.getEmail());
        if (existente.isPresent()) {
            return null; // ya existe
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre());
        nuevo.setEmail(request.getEmail());
        nuevo.setPassword(request.getPassword());
        nuevo.setRol("CLIENTE");

        return usuarioRepository.save(nuevo);
    }

    // ==============================
    // LOGIN
    // ==============================
    public Usuario login(LoginRequest request) {

        Optional<Usuario> opt = usuarioRepository.findByEmail(request.getEmail());
        if (opt.isEmpty()) {
            return null;
        }

        Usuario usuario = opt.get();

        if (!usuario.getPassword().equals(request.getPassword())) {
            return null;
        }

        return usuario;
    }
}
