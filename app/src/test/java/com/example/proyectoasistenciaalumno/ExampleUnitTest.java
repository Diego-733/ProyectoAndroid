package com.example.proyectoasistenciaalumno;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExampleUnitTest {

    @Test
    public void formularioCompleto_esValido() {
        assertTrue(ValidadorRegistro.identificadorValido("2026001"));
        assertTrue(ValidadorRegistro.nombreValido("María López"));
        assertTrue(ValidadorRegistro.codigoValido("8392"));
    }

    @Test
    public void identificadorInvalido_esRechazado() {
        assertFalse(ValidadorRegistro.identificadorValido("123"));
        assertFalse(ValidadorRegistro.identificadorValido("12.345.678"));
        assertFalse(ValidadorRegistro.identificadorValido("alumno/01"));
    }

    @Test
    public void nombreInvalido_esRechazado() {
        assertFalse(ValidadorRegistro.nombreValido(""));
        assertFalse(ValidadorRegistro.nombreValido("A"));
    }

    @Test
    public void codigoInvalido_esRechazado() {
        assertFalse(ValidadorRegistro.codigoValido("123"));
        assertFalse(ValidadorRegistro.codigoValido("12A4"));
        assertFalse(ValidadorRegistro.codigoValido("12345"));
    }
}
