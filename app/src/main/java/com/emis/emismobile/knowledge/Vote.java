package com.emis.emismobile.knowledge;

public class Vote {
    private String articleId;
    private int previous;
    private int current;

    public Vote() {
    }

    public Vote(String articleId, int previous, int current) {
        this.articleId = articleId;
        this.previous = previous;
        this.current = current;
    }

    public String getArticleId() {
        return articleId;
    }

    public int getPrevious() {
        return previous;
    }

    public int getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "articleId='" + articleId + '\'' +
                ", previous=" + previous +
                ", current=" + current +
                '}';
    }
}
