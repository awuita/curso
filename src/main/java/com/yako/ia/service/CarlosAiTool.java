package com.yako.ia.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.ai.tool.annotation.Tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CarlosAiTool {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String WIKIPEDIA_URL = "https://es.wikipedia.org/api/rest_v1/page/";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String USER_AGENT_VALUE = "JavaIA/1.0 (+https://sprint.ai/)";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String ACCEPT_JSON = "application/json";

    /**
     * Obtiene el resumen de Wikipedia para un tema dado.
     * @param topic Tema a buscar en Wikipedia
     * @return Resumen del tema desde Wikipedia
     */
    @Tool(name = "WikipediaFetcher", description = "Fetches a summary from Wikipedia for a given topic.")
    public String fetchWikipediaSummary(String topic) {
        try {
            var encodedTopic = encodeTopic(topic);
            var url = WIKIPEDIA_URL + "summary/" + encodedTopic;
            var request = buildRequest(url);
            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return extractTextNode(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String encodeTopic(String topic) {
        return URLEncoder.encode(topic.replace(" ", "_"), StandardCharsets.UTF_8);
    }

    private HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder(URI.create(url))
            .header(ACCEPT_HEADER, ACCEPT_JSON)
            .header(USER_AGENT_HEADER, USER_AGENT_VALUE)
            .GET()
            .build();
    }

    private String extractTextNode(HttpResponse<String> response) throws IOException {
        if (response.statusCode() >= 400) {
            throw new IOException("Error fetching Wikipedia data: " + response.body());
        }
        JsonNode root = OBJECT_MAPPER.readTree(response.body());
        var node = root.path("extract");
        if (node.isMissingNode() || node.isNull()) {
            return "";
        }
        return node.asText();
    }
}
