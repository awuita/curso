# 001-blogpost-generation Specification

API para generar posts de blog automáticamente usando Gemini y Spring AI.

- Relacionado: [PRD.md](../PRD.md)

## 1. 👔 Problem Specification

### Generación automática de posts
- **Como** servicio interno o herramienta
- **Quiero** enviar un título y recibir un post generado (con slug y contenido)
- **Para que** pueda automatizar la creación de contenido de blog sin intervención manual

## 2. 🧑‍💻 Solution Overview

### Data Models
- `BlogPostRequest`: { title: String }
- `BlogPostResponse`: { title: String, slug: String, filePath: String, summary?: String }
- `BlogPost`: { title: String, slug: String, content: String }

### Software Components
- `BlogPostController`: expone el endpoint POST /posts
- `SlugGeneratorService`: genera el slug SEO-friendly
- `GeminiService`: construye el prompt y llama a Gemini vía Spring AI
- `MarkdownWriterService`: guarda el post en un fichero .md en la ruta configurada
- `Config`: gestiona la API key y la ruta de almacenamiento

### User Interface
- API HTTP REST: POST /posts { "title": "..." }

### Aspects
- Seguridad: la API key nunca se expone ni se loguea
- Error handling: errores claros para título vacío, fallo Gemini, fallo de escritura
- Logging: suficiente para depuración, sin datos sensibles
- Configuración: ruta de almacenamiento y API key por entorno

## 3. 🧑‍⚖️ Acceptance Criteria
- [ ] SHALL validate that the title is not empty WHEN receiving a POST /posts request
- [ ] SHALL generate a SEO-friendly slug IF the title is valid
- [ ] SHALL construct a prompt and call Gemini via Spring AI WHEN generating content
- [ ] SHALL store the generated post as a Markdown file in the configured path IF content is generated
- [ ] SHALL return title, slug, and file path in the response WHEN post is successfully created
- [ ] SHALL return a summary or first N characters of content IF requested
- [ ] SHALL handle and return errors for missing title, Gemini failure, or file write issues
- [ ] SHALL log integration errors WITHOUT exposing the API key

> End of Feature Specification for 001-blogpost-generation, last updated 19 de noviembre de 2025.
