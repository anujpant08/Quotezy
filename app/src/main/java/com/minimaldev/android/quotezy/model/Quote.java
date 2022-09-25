package com.minimaldev.android.quotezy.model;

import java.util.List;

public class Quote {
    private String text;
    private String author;
    private List<String> tag;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
