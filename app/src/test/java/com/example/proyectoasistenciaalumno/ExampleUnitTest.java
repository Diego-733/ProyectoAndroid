package com.example.proyectoasistenciaalumno;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExampleUnitTest {

    @Test
    public void formularioCompleto_esValido() {
        assertTrue(ValidadorRegistro.identificadorValido("21.599.246-2"));
        assertTrue(ValidadorRegistro.nombreValido("María López"));
        assertTrue(ValidadorRegistro.codigoValido("8392"));
    }

    @Test
    public void formatosEquivalentes_generanLaMismaClave() {
        assertEquals("21599246-2", ValidadorRegistro.normalizarRut("21.599.246-2"));
        assertEquals("21599246-2", ValidadorRegistro.normalizarRut("21599246-2"));
        assertEquals("21599246-2", ValidadorRegistro.normalizarRut("215992462"));
    }

    @Test
    public void rutInvalido_esRechazado() {
        assertFalse(ValidadorRegistro.identificadorValido("123"));
        assertFalse(ValidadorRegistro.identificadorValido("21.599.246-3"));
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
