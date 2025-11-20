package com.yako.ia.dto;

public class BlogPostRequest {
    private String title;

    public BlogPostRequest() {}
    public BlogPostRequest(String title) { this.title = title; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
