# Proyecto Asistencia — Alumno

Aplicación Android en Java para registrar la asistencia de un alumno en una sesión creada por la aplicación del profesor. Ambas aplicaciones se comunican mediante Firebase Realtime Database.

## Requisitos

- Android Studio con JDK integrado.
- Android SDK 29 o superior.
- Proyecto Firebase compartido con la aplicación Profesor.
- Realtime Database habilitada.
- `app/google-services.json` correspondiente al proyecto Firebase.

## Flujo de registro

1. El alumno ingresa identificador, nombre y código de cuatro dígitos.
2. La app consulta `sesiones` buscando una sesión cuyo campo `codigo` coincida.
3. Verifica que la sesión exista y tenga `activa: true`.
4. Ejecuta una transacción que vuelve a comprobar el estado y que el alumno no exista.
5. Escribe nombre y hora bajo `sesiones/{idSesion}/alumnos/{idAlumno}`.
6. La aplicación muestra la confirmación y la aplicación Profesor recibe el cambio mediante su listener.

## Estructura de datos

```text
sesiones/
  {idSesion}/
    activa: true|false
    codigo: "8392"
    curso: "Programación Android"
    creadoEn: "2026-09-20T15:00:00-03:00"
    alumnos/
      {idAlumno}/
        nombre: "María López"
        horaRegistro: "2026-09-20T15:05:23-03:00"
```

## Reglas de desarrollo

El archivo `database.rules.json` contiene reglas abiertas para la demostración académica e incluye el índice requerido para buscar por `codigo`. No deben usarse en producción porque el proyecto no implementa autenticación.

Para la demostración, copia el contenido del archivo en Firebase Console → Realtime Database → Reglas y publica los cambios.

## Casos de error implementados

- Campos incompletos o con formato inválido.
- Código inexistente.
- Sesión cerrada.
- Alumno ya registrado.
- Dispositivo sin conexión.
- Permisos Firebase incorrectos.

## Compilación

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug
```
