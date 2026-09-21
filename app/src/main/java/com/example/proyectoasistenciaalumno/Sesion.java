package com.example.proyectoasistenciaalumno;

public class Sesion {

    private Boolean activa;
    private String codigo;
    private String curso;
    private String creadoEn;

    public Sesion() {
        // Constructor requerido por Firebase.
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(String creadoEn) {
        this.creadoEn = creadoEn;
    }

    public boolean estaActiva() {
        return Boolean.TRUE.equals(activa);
    }
}
