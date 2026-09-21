package com.example.proyectoasistenciaalumno;

import java.util.Locale;

public final class ValidadorRegistro {

    private static final String PATRON_RUT = "^[0-9]{7,8}-[0-9K]$";
    private static final String PATRON_CODIGO = "^[0-9]{4}$";

    private ValidadorRegistro() {
    }

    public static boolean identificadorValido(String identificador) {
        return rutValido(identificador);
    }

    public static String normalizarRut(String rut) {
        if (rut == null) {
            return "";
        }

        String rutCompacto = rut
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("[.\\s-]", "");

        if (rutCompacto.length() < 2) {
            return rutCompacto;
        }

        int posicionDigitoVerificador = rutCompacto.length() - 1;
        return rutCompacto.substring(0, posicionDigitoVerificador)
                + "-"
                + rutCompacto.charAt(posicionDigitoVerificador);
    }

    public static boolean rutValido(String rut) {
        String rutNormalizado = normalizarRut(rut);
        if (!rutNormalizado.matches(PATRON_RUT)) {
            return false;
        }

        String[] partes = rutNormalizado.split("-");
        String cuerpo = partes[0];
        char digitoIngresado = partes[1].charAt(0);

        int suma = 0;
        int multiplicador = 2;
        for (int indice = cuerpo.length() - 1; indice >= 0; indice--) {
            suma += Character.getNumericValue(cuerpo.charAt(indice)) * multiplicador;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }

        int resultado = 11 - (suma % 11);
        char digitoCalculado;
        if (resultado == 11) {
            digitoCalculado = '0';
        } else if (resultado == 10) {
            digitoCalculado = 'K';
        } else {
            digitoCalculado = Character.forDigit(resultado, 10);
        }

        return digitoIngresado == digitoCalculado;
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
