# AppUESAN

App Android en Kotlin + Jetpack Compose desarrollada para el curso de la UESAN. Integra Firebase, Supabase Storage y la API de API-Football.

## Funcionalidades

- **Login / Registro** con Firebase Authentication (el usuario se guarda en Firestore).
- **Home**: ranking FIFA de ejemplo.
- **Permissions**: selector de imágenes de la galería.
- **Real Time**: contador y mensaje sincronizados en tiempo real con Firestore.
- **Supabase Storage**: subida y listado de imágenes en el bucket `imagenes`.
- **API Football**: lista de países y equipos de fútbol por país (API-Sports v3).
- **Logout** desde el menú lateral.

## Requisitos

- Android Studio (con su JDK incluido)
- Android SDK 37, dispositivo o emulador con Android 14+ (minSdk 34)

## Configuración de credenciales de API Football

Las credenciales **no** están en el código: se leen desde `local.properties` (archivo que no se sube a git) y se exponen en `BuildConfig`.

1. Abre o crea el archivo `local.properties` en la raíz del proyecto.
2. Agrega estas líneas:

   ```properties
   API_BASE_URL=https://v3.football.api-sports.io/
   API_KEY=TU_API_KEY
   ```

   Puedes obtener una API key gratuita en [dashboard.api-football.com](https://dashboard.api-football.com/register).

3. Sincroniza Gradle (**File > Sync Project with Gradle Files**) y ejecuta la app.

Si falta alguna de las dos, la pantalla de API Football muestra el mensaje *"Falta API_BASE_URL o API_KEY en local.properties"*.

> El plan gratuito de API-Football permite 100 peticiones por día. Si se supera el límite, la app muestra el error devuelto por la API con un botón para reintentar.

## Firebase y Supabase

- **Firebase**: el proyecto usa `app/google-services.json`. Si Firestore muestra `PERMISSION_DENIED`, hay que revisar las reglas de seguridad en la consola de Firebase.
- **Supabase**: la URL y la key están en `SupabaseManager.kt`. El proyecto Supabase debe estar activo y tener un bucket público llamado `imagenes`.

## Estructura

```
app/src/main/java/dev/lchang/appuesan/
├── data/
│   ├── model/              # Modelos locales
│   └── remote/             # Firebase, Supabase y Retrofit (apifootball/)
├── presentation/
│   ├── apifootball/        # Pantalla y ViewModel de API Football
│   ├── auth/               # Login y registro
│   ├── home/
│   ├── navigation/         # NavGraph y menú lateral
│   ├── permissions/
│   ├── realtime/           # Firestore en tiempo real
│   └── supabase/
└── ui/theme/
```
