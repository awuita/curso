package com.yako.ia.dto;

public class BlogPostResponse {
    private String title;
    private String slug;
    private String filePath;
    private String summary;

    public BlogPostResponse() {}
    public BlogPostResponse(String title, String slug, String filePath, String summary) {
        this.title = title;
        this.slug = slug;
        this.filePath = filePath;
        this.summary = summary;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
