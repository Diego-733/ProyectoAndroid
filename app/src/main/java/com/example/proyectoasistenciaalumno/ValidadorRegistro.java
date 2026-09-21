package com.example.proyectoasistenciaalumno;

public final class ValidadorRegistro {

    private static final String PATRON_IDENTIFICADOR = "^[A-Za-z0-9-]{5,20}$";
    private static final String PATRON_CODIGO = "^[0-9]{4}$";

    private ValidadorRegistro() {
    }

    public static boolean identificadorValido(String identificador) {
        return identificador != null && identificador.matches(PATRON_IDENTIFICADOR);
    }

    public static boolean nombreValido(String nombre) {
        if (nombre == null) {
            return false;
        }
        int longitud = nombre.trim().length();
        return longitud >= 2 && longitud <= 80;
    }

    public static boolean codigoValido(String codigo) {
        return codigo != null && codigo.matches(PATRON_CODIGO);
    }
}
