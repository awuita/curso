# Build Failure Report: 001-blogpost-generation

## Context
La implementación de la feature 001-blogpost-generation no supera el smoke test de compilación.

## Error principal
```
Vertex AI project-id must be set!
```

## Causa
Spring AI requiere la configuración de `vertexai.project-id` para inicializar Gemini. Sin este valor, la aplicación no puede arrancar.

## Siguiente paso sugerido
- Añadir la propiedad `vertexai.project-id` en `application.properties` o como variable de entorno.
- Proveer un valor dummy para desarrollo local si no se va a usar Gemini real.

> Última actualización: 19 de noviembre de 2025.
