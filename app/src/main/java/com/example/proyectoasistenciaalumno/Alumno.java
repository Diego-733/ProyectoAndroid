package com.example.proyectoasistenciaalumno;

public class Alumno {

    private String nombre;
    private String horaRegistro;

    public Alumno() {
        // Constructor requerido por Firebase.
    }

    public Alumno(String nombre, String horaRegistro) {
        this.nombre = nombre;
        this.horaRegistro = horaRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }
}
