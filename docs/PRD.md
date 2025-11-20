# Product Requirements Document for Blog Post Generator API

## Overview

**Blog Post Generator API** aims to provide an internal API for automatic blog post generation using Gemini. The client sends only a title, and the backend generates a SEO-friendly slug and post content by calling Gemini via Spring AI.

### Goals
- Automate blog post creation for internal tools and services.
- Ensure posts are SEO-friendly and well-structured.
- Integrate Gemini AI securely for content generation.

---

## Functional Requirements
### FR1 HTTP API for Blog Post Generation
An HTTP API endpoint that receives a blog post title and returns the generated post details (title, slug, file path, and optionally a summary).

### FR2 Slug Generation
Automatically generate a SEO-friendly slug from the provided title.

### FR3 Gemini Integration
Construct a prompt and call Gemini via Spring AI (spring-ai-starter-model-vertex-ai-gemini) using a securely configured API key.

### FR4 Markdown File Creation
Save the generated post as a Markdown (.md) file, including title, slug, and AI-generated content, in a configurable path.

### FR5 Error Handling & Logging
Return clear errors for missing title, Gemini failures, or file write issues. Log integration issues without exposing sensitive data.

---

## Technical Requirements

### Technical Stack
- Java, Spring Boot
- Spring AI (spring-ai-starter-model-vertex-ai-gemini)
- Markdown file output

### TR1 Secure API Key Management
Gemini API key must be set via environment variable or secure configuration, never exposed to clients or logs.

### TR2 Configurable Storage Path
Markdown files are stored in a path configurable per environment (dev, staging, prod).

### TR3 Error Handling
API must handle and report errors for invalid input, AI failures, and file system issues.

### TR4 Performance
Post generation should have reasonable response times for internal use.

### TR5 Logging
Sufficient logging for debugging Gemini integration, excluding sensitive information.

## System C4 Context diagram

```mermaid
C4Context
    Person(client, "Internal Service or Tool", "Requests blog post generation")
    System(api, "Blog Post Generator API", "Generates blog posts using Gemini via Spring AI")
    System_Ext(gemini, "Gemini AI", "AI model for content generation")
    SystemDb(storage, "Markdown File Storage", "Stores generated blog posts")
    client -> api: POST /posts {title}
    api -> gemini: Prompt for content
    api -> storage: Save .md file
    api -> client: Return post details
```

> End of PRD for Blog Post Generator API, last updated on 19 de noviembre de 2025.
