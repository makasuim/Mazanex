package com.mazanex.auth.dto;

public class AuthResponse {

    private boolean exito;
    private String mensaje;
    private Long usuarioId;
    private String nombre;
    private String email;

    public AuthResponse() {
    }

    public AuthResponse(boolean exito, String mensaje, Long usuarioId, String nombre, String email) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
