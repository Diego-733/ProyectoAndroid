package com.example.proyectoasistenciaalumno;

public class RegistroExitoso {

    private final String curso;
    private final String codigo;
    private final String horaRegistro;

    public RegistroExitoso(String curso, String codigo, String horaRegistro) {
        this.curso = curso;
        this.codigo = codigo;
        this.horaRegistro = horaRegistro;
    }

    public String getCurso() {
        return curso;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }
}
