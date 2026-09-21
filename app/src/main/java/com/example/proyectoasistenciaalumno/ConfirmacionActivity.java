package com.example.proyectoasistenciaalumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoasistenciaalumno.databinding.ActivityConfirmacionBinding;

public class ConfirmacionActivity extends AppCompatActivity {

    public static final String EXTRA_CURSO = "EXTRA_CURSO";
    public static final String EXTRA_CODIGO = "EXTRA_CODIGO";
    public static final String EXTRA_HORA = "EXTRA_HORA";

    private ActivityConfirmacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String curso = valorExtra(EXTRA_CURSO, getString(R.string.valor_no_disponible));
        String codigo = valorExtra(EXTRA_CODIGO, getString(R.string.valor_no_disponible));
        String hora = valorExtra(EXTRA_HORA, getString(R.string.valor_no_disponible));

        binding.tvCursoConfirmacion.setText(curso);
        binding.tvCodigoConfirmacion.setText(codigo);
        binding.tvHoraConfirmacion.setText(hora);

        binding.btnVolverInicio.setOnClickListener(view -> volverAlInicio());
        binding.btnRegistrarOtro.setOnClickListener(view -> {
            startActivity(new Intent(this, RegistroAsistenciaActivity.class));
            finish();
        });
    }

    private String valorExtra(String clave, String respaldo) {
        String valor = getIntent().getStringExtra(clave);
        return valor == null || valor.trim().isEmpty() ? respaldo : valor;
    }

    private void volverAlInicio() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
