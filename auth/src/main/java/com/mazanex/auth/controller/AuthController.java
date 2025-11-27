package com.mazanex.auth.controller;

import com.mazanex.auth.dto.AuthResponse;
import com.mazanex.auth.dto.LoginRequest;
import com.mazanex.auth.dto.RegistroRequest;
import com.mazanex.auth.model.Usuario;
import com.mazanex.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegistroRequest request) {
        Usuario nuevo = authService.registrar(request);
        if (nuevo == null) {
            AuthResponse resp = new AuthResponse(false, "El correo ya está registrado", null, null, null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
        }

        AuthResponse resp = new AuthResponse(
                true,
                "Usuario registrado correctamente",
                nuevo.getId(),
                nuevo.getNombre(),
                nuevo.getEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = authService.login(request);
        if (usuario == null) {
            AuthResponse resp = new AuthResponse(false, "Credenciales inválidas", null, null, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        AuthResponse resp = new AuthResponse(
                true,
                "Login exitoso",
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public List<Usuario> listaUsuario() {
        return authService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable long id) {
        Usuario usuario = authService.getUsuarioID(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable long id, @RequestBody Usuario usuario) {
        Usuario actualizado = authService.updateUsuario(id, usuario);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable long id) {
        authService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroRequest request) {
    Usuario nuevo = authService.registrar(request);

    if (nuevo == null) {
        return ResponseEntity.status(400).body("El email ya está registrado");
    }

    return ResponseEntity.ok(nuevo);
    }   

}
