Producto: Blog Post Generator API

Objetivo:
Exponer una API interna que permita generar posts de blog automáticamente usando Gemini.  
El cliente solo envía un título, y el backend se encarga de generar el slug y el contenido del post llamando a Gemini a través de Spring AI.

Usuarios objetivo:
- Otros servicios internos (backends, pipelines de marketing, automatizaciones).
- Herramientas internas que necesiten generar contenido de blog.

Descripción de la funcionalidad principal:
- API HTTP que recibe un título de post como parámetro.
- A partir del título:
  - Se genera un slug SEO-friendly.
  - Se construye un prompt para la IA.
  - Se llama a Gemini mediante Spring AI (starter: spring-ai-starter-model-vertex-ai-gemini) usando una API key configurada en el servidor.
  - Se genera un contenido de post (texto largo).
  - Se crea un fichero de post (por ejemplo, .md) que incluya:
    - título
    - slug
    - contenido generado por la IA

- La API devuelve al cliente:
  - título
  - slug
  - ruta o nombre del fichero generado
  - opcionalmente, un resumen o los primeros N caracteres del contenido.

Flujo funcional (ejemplo):
1. Cliente hace POST /posts con JSON: { "title": "Mi primer post con IA" }.
2. El sistema valida que el título no esté vacío.
3. El sistema genera el slug (ej: "mi-primer-post-con-ia").
4. El sistema construye un prompt adecuado para Gemini (tono, longitud, idioma, etc.).
5. El sistema llama a Gemini usando Spring AI y el starter spring-ai-starter-model-vertex-ai-gemini.
6. La API key de Gemini se obtiene de una variable de entorno o configuración segura (no se expone nunca al cliente).
7. Se genera el contenido del post y se guarda en un fichero Markdown en una ruta configurable.
8. La API responde con título, slug y path del fichero.

Restricciones técnicas:
- Backend implementado con Spring Boot.
- Uso de Spring AI con el starter: spring-ai-starter-model-vertex-ai-gemini.
- La API key de Gemini debe configurarse vía variable de entorno o sistema de configuración seguro.
- Formato de fichero: Markdown (.md).
- Ruta de almacenamiento configurable por entorno (desarrollo, staging, producción).

Requisitos no funcionales:
- Tiempo de respuesta razonable para la generación del post.
- Manejo de errores claro:
  - Error si falta el título.
  - Error si falla la llamada a Gemini.
  - Error si no se puede escribir el fichero.
- Logs suficientes para depurar problemas con la integración de Gemini, sin exponer la API key en los logs.