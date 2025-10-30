# Raven Hacker News

**Raven Hacker News** es una aplicación Android desarrollada en **Kotlin + Jetpack Compose**, que consume la API pública de **Hacker News** para mostrar noticias sobre tecnología, filtradas por la palabra clave **“android”**.

---

## Descripción general

La aplicación permite consultar, visualizar noticias provenientes de una fuente remota, almacenándolas localmente en una base de datos para acceso offline.  
Además, el usuario puede eliminar entradas de manera sin perder la persistencia en el almacenamiento local.

---

## Tecnologías principales

| Capa | Herramientas y librerías |
|------|---------------------------|
| **UI / Presentación** | Jetpack Compose, Material 3, Navigation Compose |
| **Dominio** | UseCases, DTOs, Repository Pattern |
| **Datos** | Room (local), Retrofit + Gson (remoto), RxJava 3 |
| **DI (Inyección de dependencias)** | Hilt |
| **Pruebas unitarias** | JUnit, Mockito, RxJava|

---


---

## Requerimientos funcionales

| Requerimiento | Descripción |
|----------------|--------------|
| **Consulta remota de noticias** | La app debe conectarse a la API de Hacker News y obtener artículos con el término “android”. |
| **Almacenamiento local** | Las noticias deben almacenarse en una base de datos para acceso offline. |
| **Visualización de listado** | La pantalla principal mostrará las noticias con título, autor y fecha. |
| **Detalle de noticia** | Al seleccionar una noticia, se abrirá un `WebView` con el contenido original. |
| **Eliminación lógica** | El usuario puede deslizar una noticia para eliminarla de la lista. |
| **Recarga mediante Pull Refresh** | Permitir al usuario actualizar el listado manualmente. |
| **Manejo de errores de red** | Mostrar un diálogo de error cuando falle la conexión o el recurso no esté disponible. |

---

## Requerimientos no funcionales

| Requerimiento | Descripción |
|----------------|--------------|
| **Arquitectura limpia (MVVM + Clean Architecture)** | Separación de capas y dependencias controladas mediante Hilt. |
| **Mantenibilidad** | Código modular y desacoplado mediante interfaces y casos de uso. |
| **Pruebas unitarias** | Cada capa posee cobertura de pruebas (repository, use cases, viewmodel). |
| **Gestión de errores centralizada** | Uso de `UseCaseException` para unificar la captura y propagación de errores. |
| **Seguridad básica** | Configuración de `network_security_config.xml` para definir dominios seguros HTTPS. |
| **Internacionalización** | Textos parametrizados mediante `strings.xml` para soporte multilenguaje. |

---

## Pruebas unitarias incluidas

| Archivo | Capa | Propósito |
|----------|------|-----------|
| `HackerNewsDataRepositoryTest.kt` | Data | Valida integración entre `remoteSource`, `localSource` y `mappers`. |
| `HackerNewsDataRemoteSourceTest.kt` | Remote | Asegura correcta respuesta del API y manejo de excepciones. |
| `HackerNewsDataLocalSourceTest.kt` | Local | Comprueba operaciones CRUD y soft delete en Room. |
| `RetrieveHackerNewsUseCaseTest.kt` | Domain | Valida flujo completo de obtención de noticias. |
| `HackerNewsViewModelTest.kt` | Presentation | Simula flujos de UI y verificación de estados `Success`, `Error`, `Loading`. |

---




