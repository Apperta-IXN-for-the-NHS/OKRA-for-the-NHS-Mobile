package com.emis.emismobile.cases;

public class Case {
    private String id;
    private String title;
    private String date;
    private int priority;
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPriority() {
        switch (priority){
            case 1: return "Critical";
            case 2: return "High";
            case 3: return "Moderate";
            case 4: return "Low";
            default: return "Low";
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
