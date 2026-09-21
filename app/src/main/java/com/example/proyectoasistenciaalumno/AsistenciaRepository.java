package com.example.proyectoasistenciaalumno;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class AsistenciaRepository {

    public enum ErrorRegistro {
        SESION_NO_ENCONTRADA,
        SESION_CERRADA,
        ALUMNO_DUPLICADO,
        SIN_CONEXION,
        PERMISO_DENEGADO,
        DESCONOCIDO
    }

    public interface RegistroCallback {
        void onExito(RegistroExitoso resultado);

        void onError(ErrorRegistro error);
    }

    private final DatabaseReference sesionesRef;

    public AsistenciaRepository() {
        sesionesRef = FirebaseDatabase.getInstance().getReference("sesiones");
    }

    public void registrarAsistencia(
            String identificador,
            String nombre,
            String codigo,
            RegistroCallback callback
    ) {
        Query consulta = sesionesRef.orderByChild("codigo").equalTo(codigo);
        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(ErrorRegistro.SESION_NO_ENCONTRADA);
                    return;
                }

                DataSnapshot sesionActiva = null;
                for (DataSnapshot sesionSnapshot : snapshot.getChildren()) {
                    Boolean activa = sesionSnapshot.child("activa").getValue(Boolean.class);
                    if (Boolean.TRUE.equals(activa)) {
                        sesionActiva = sesionSnapshot;
                        break;
                    }
                }

                if (sesionActiva == null) {
                    callback.onError(ErrorRegistro.SESION_CERRADA);
                    return;
                }

                String idSesion = sesionActiva.getKey();
                String curso = sesionActiva.child("curso").getValue(String.class);
                if (idSesion == null) {
                    callback.onError(ErrorRegistro.DESCONOCIDO);
                    return;
                }

                ejecutarRegistroAtomico(idSesion, identificador, nombre, codigo, curso, callback);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(convertirError(error));
            }
        });
    }

    private void ejecutarRegistroAtomico(
            String idSesion,
            String identificador,
            String nombre,
            String codigo,
            String curso,
            RegistroCallback callback
    ) {
        String horaRegistro = crearFechaIso8601();
        Alumno alumno = new Alumno(nombre, horaRegistro);
        DatabaseReference sesionRef = sesionesRef.child(idSesion);
        AtomicReference<ErrorRegistro> motivoAborto =
                new AtomicReference<>(ErrorRegistro.DESCONOCIDO);

        sesionRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() == null) {
                    motivoAborto.set(ErrorRegistro.SESION_NO_ENCONTRADA);
                    return Transaction.abort();
                }

                Boolean activa = currentData.child("activa").getValue(Boolean.class);
                if (!Boolean.TRUE.equals(activa)) {
                    motivoAborto.set(ErrorRegistro.SESION_CERRADA);
                    return Transaction.abort();
                }

                MutableData alumnoData = currentData
                        .child("alumnos")
                        .child(identificador);
                if (alumnoData.getValue() != null) {
                    motivoAborto.set(ErrorRegistro.ALUMNO_DUPLICADO);
                    return Transaction.abort();
                }

                alumnoData.setValue(alumno);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(
                    DatabaseError error,
                    boolean committed,
                    DataSnapshot currentData
            ) {
                if (error != null) {
                    callback.onError(convertirError(error));
                    return;
                }

                if (!committed) {
                    callback.onError(motivoAborto.get());
                    return;
                }

                callback.onExito(new RegistroExitoso(
                        curso == null ? "Curso sin nombre" : curso,
                        codigo,
                        horaRegistro
                ));
            }
        }, false);
    }

    private ErrorRegistro convertirError(DatabaseError error) {
        if (error.getCode() == DatabaseError.DISCONNECTED
                || error.getCode() == DatabaseError.NETWORK_ERROR
                || error.getCode() == DatabaseError.UNAVAILABLE) {
            return ErrorRegistro.SIN_CONEXION;
        }
        if (error.getCode() == DatabaseError.PERMISSION_DENIED) {
            return ErrorRegistro.PERMISO_DENEGADO;
        }
        return ErrorRegistro.DESCONOCIDO;
    }

    private String crearFechaIso8601() {
        SimpleDateFormat formato = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                Locale.US
        );
        formato.setTimeZone(TimeZone.getDefault());
        return formato.format(new Date());
    }
}
