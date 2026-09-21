package com.example.proyectoasistenciaalumno;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoasistenciaalumno.databinding.ActivityRegistroAsistenciaBinding;
import com.google.android.material.snackbar.Snackbar;

public class RegistroAsistenciaActivity extends AppCompatActivity {

    private ActivityRegistroAsistenciaBinding binding;
    private AsistenciaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroAsistenciaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new AsistenciaRepository();

        binding.btnVolver.setOnClickListener(view -> finish());
        binding.btnRegistrar.setOnClickListener(view -> intentarRegistro());
    }

    private void intentarRegistro() {
        limpiarErrores();

        String identificador = ValidadorRegistro.normalizarRut(
                texto(binding.etIdentificador)
        );
        String nombre = texto(binding.etNombre);
        String codigo = texto(binding.etCodigo);

        boolean formularioValido = true;
        if (!ValidadorRegistro.identificadorValido(identificador)) {
            binding.tilIdentificador.setError(getString(R.string.error_identificador));
            formularioValido = false;
        }
        if (!ValidadorRegistro.nombreValido(nombre)) {
            binding.tilNombre.setError(getString(R.string.error_nombre));
            formularioValido = false;
        }
        if (!ValidadorRegistro.codigoValido(codigo)) {
            binding.tilCodigo.setError(getString(R.string.error_codigo_formato));
            formularioValido = false;
        }

        if (!formularioValido) {
            return;
        }

        binding.etIdentificador.setText(identificador);
        binding.etIdentificador.setSelection(identificador.length());

        if (!hayConexionDisponible()) {
            mostrarMensaje(R.string.error_sin_conexion);
            return;
        }

        mostrarCarga(true);
        repository.registrarAsistencia(
                identificador,
                nombre,
                codigo,
                new AsistenciaRepository.RegistroCallback() {
                    @Override
                    public void onExito(RegistroExitoso resultado) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        mostrarCarga(false);
                        abrirConfirmacion(resultado);
                    }

                    @Override
                    public void onError(AsistenciaRepository.ErrorRegistro error) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        mostrarCarga(false);
                        mostrarError(error);
                    }
                }
        );
    }

    private String texto(EditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }

    private void limpiarErrores() {
        binding.tilIdentificador.setError(null);
        binding.tilNombre.setError(null);
        binding.tilCodigo.setError(null);
    }

    private void mostrarCarga(boolean cargando) {
        binding.progressRegistro.setVisibility(cargando ? View.VISIBLE : View.GONE);
        binding.btnRegistrar.setEnabled(!cargando);
        binding.btnVolver.setEnabled(!cargando);
        binding.etIdentificador.setEnabled(!cargando);
        binding.etNombre.setEnabled(!cargando);
        binding.etCodigo.setEnabled(!cargando);
        binding.btnRegistrar.setText(cargando
                ? R.string.registrando
                : R.string.registrar_asistencia);
    }

    private void mostrarError(AsistenciaRepository.ErrorRegistro error) {
        switch (error) {
            case SESION_NO_ENCONTRADA:
                binding.tilCodigo.setError(getString(R.string.error_sesion_no_encontrada));
                break;
            case SESION_CERRADA:
                binding.tilCodigo.setError(getString(R.string.error_sesion_cerrada));
                break;
            case ALUMNO_DUPLICADO:
                binding.tilIdentificador.setError(getString(R.string.error_alumno_duplicado));
                break;
            case SIN_CONEXION:
                mostrarMensaje(R.string.error_sin_conexion);
                break;
            case PERMISO_DENEGADO:
                mostrarMensaje(R.string.error_permiso_firebase);
                break;
            default:
                mostrarMensaje(R.string.error_desconocido);
                break;
        }
    }

    private void mostrarMensaje(int mensajeId) {
        Snackbar.make(binding.getRoot(), mensajeId, Snackbar.LENGTH_LONG).show();
    }

    private boolean hayConexionDisponible() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    private void abrirConfirmacion(RegistroExitoso resultado) {
        Intent intent = new Intent(this, ConfirmacionActivity.class);
        intent.putExtra(ConfirmacionActivity.EXTRA_CURSO, resultado.getCurso());
        intent.putExtra(ConfirmacionActivity.EXTRA_CODIGO, resultado.getCodigo());
        intent.putExtra(ConfirmacionActivity.EXTRA_HORA, resultado.getHoraRegistro());
        startActivity(intent);
        finish();
    }
}
