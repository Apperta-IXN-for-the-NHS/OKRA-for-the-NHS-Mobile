package com.emis.emismobile.knowledge;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Article {
    private String id;
    private String author;
    private String title;
    private String body;

    @SerializedName("net_votes")
    private double score;

    @SerializedName("view_count")
    private int views;

    @SerializedName("created")
    private String date;
    private List<Article> related = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody(){
        body = body.replaceAll("�","");
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        String[] parts = date.split("-");

        if (parts.length < 3) {
            return date;
        }

        return parts[2] + "/" + parts[1] + "/" + parts[0];
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Article> getRelated() {
        return related;
    }

    public void setRelated(List<Article> related) {
        this.related = related;
    }

    public int getScore() {
        return (int) score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}

